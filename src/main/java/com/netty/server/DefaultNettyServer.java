package com.netty.server;

import com.netty.EventGroupThreadFactory;
import com.netty.message.HeartBeat;
import com.netty.message.Result;
import com.netty.message.YpcInvocation;
import com.protocol.ProtocolSelector;
import com.protocol.Serializer;
import com.proxy.server.NettyServerProxy;
import com.service.server.ServiceFindHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * say some thing
 *
 * @author angyang
 * @version v1.0
 * @date 2018/12/11
 */
public class DefaultNettyServer extends AbstractNettyServer implements InitializingBean {
    private static final Logger log = LoggerFactory.getLogger(DefaultNettyServer.class);
    @Resource
    private ServiceFindHandler serviceFindHandler;

    public ServiceFindHandler getServiceFindHandler() {
        return serviceFindHandler;
    }

    public void setServiceFindHandler(ServiceFindHandler serviceFindHandler) {
        this.serviceFindHandler = serviceFindHandler;
    }

    ChannelFuture channelFuture;
    int port;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    NettyServerProxy nettyServerProxy;
    private DefaultEventLoopGroup defaultEventGroup;
    private NioEventLoopGroup slaveGroup;
    private NioEventLoopGroup mastGroup;
    private ServerBootstrap serverBootstrap;
    private static final int TOP_LENGTH = 129 >> 1 | 34; // 数据协议头
    private static final int TOP_HEARTBEAT = 129 >> 1 | 36; // 心跳协议头


    public DefaultNettyServer() {
        defaultEventGroup = new DefaultEventLoopGroup(Runtime.getRuntime().availableProcessors(), new EventGroupThreadFactory("defaultEventGroup"));

        slaveGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors(), new EventGroupThreadFactory("slaveGroup"));

        mastGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors(), new EventGroupThreadFactory("masterGroup"));

    }


    private void init() {
        log.info("初始化netty服务端... ...");
            serverBootstrap = new ServerBootstrap().group(mastGroup, slaveGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    .option(ChannelOption.SO_SNDBUF, 10 * 1024 * 1024)
                    .option(ChannelOption.SO_RCVBUF, 10 * 1024 * 1024)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(defaultEventGroup);
                            ch.pipeline().addLast(new EnCodeHandler());
                            ch.pipeline().addLast(new DecodeHandler());
                            ch.pipeline().addLast(new IdleStateHandler(60, 60, 60, TimeUnit.SECONDS));
                            ch.pipeline().addLast(new YpcInboundHandler());
                        }
                    })
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
            try {
                channelFuture = serverBootstrap.bind(port).sync();
                log.info("bind port success! .... at port: "+port);
            } catch (InterruptedException e) {
                log.error("bind port error!", e);
            }
    }

    @Override
    protected Runnable getSubmitTask(Channel channel, YpcInvocation invocation) {
        return () -> {
            Result result;
            try {
                Object object = serviceFindHandler.findObject(invocation.getClassName());
                nettyServerProxy = new NettyServerProxy(object);
                if (invocation.isReturnType()) {
                    result = nettyServerProxy.invoke(invocation);
                    result.setProtocol(invocation.getProtocol());
                    result.setSerialNo(invocation.getSerialNo());
                    channel.writeAndFlush(result);
                }
            } catch (Exception e) {
                log.error("远程服务执行失败！", e);
            }
        };
    }

    class EnCodeHandler extends MessageToByteEncoder<Object> {

        @Override
        protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
            if (msg instanceof Result){
                Result result = ((Result) msg);
                int protocol = Integer.valueOf(result.getProtocol());
               Serializer serializer =  ProtocolSelector.getProtocol(protocol);
                byte[] bytes = serializer.transToByte(result);
                ByteBuffer byteBuffer = ByteBuffer.allocate(4*4*4+bytes.length);
                byteBuffer.putInt(TOP_LENGTH);
                byteBuffer.putInt(protocol);
                byteBuffer.putInt(bytes.length);
                byteBuffer.put(bytes);
                byteBuffer.flip();
                out.writeBytes(byteBuffer);
            }
        }
    }

    class DecodeHandler extends ByteToMessageDecoder {

        @Override
        protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
            int begin;
            int header;
            if (in.readableBytes() < 12) {
                return;
            }
            while (true) {
                begin = in.readerIndex();
                in.markReaderIndex();
                header = in.readInt();
                if (header == TOP_LENGTH || header == TOP_HEARTBEAT) {
                    break;
                }
                in.resetReaderIndex();
                in.readByte();
            }
            int protocol = in.readInt();
            int bodyLength = in.readInt();
            if (bodyLength > in.readableBytes()) {
                in.readerIndex(begin);
                return ;
            }
            byte[] bytes = new byte[bodyLength];
            in.readBytes(bytes);
            in.discardReadBytes();//丢弃掉已经处理的字节
            Serializer serializer;
            switch(header){
                case TOP_LENGTH:
                    serializer = ProtocolSelector.getProtocol(protocol);
                    YpcInvocation invocation = serializer.transToObject(YpcInvocation.class, bytes);
                    out.add(invocation);
                    break;
                case TOP_HEARTBEAT:
                    serializer = ProtocolSelector.getProtocol(ProtocolSelector.DEFAULT_PROTOCOL);
                    HeartBeat heartBeat = serializer.transToObject(HeartBeat.class, bytes);
                    out.add(heartBeat);
                    break;
            }
        }
    }

    class YpcInboundHandler extends SimpleChannelInboundHandler<Object> {
        private ConcurrentHashMap<SocketAddress, AtomicInteger> heartBeatCache = new ConcurrentHashMap<>();

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            super.exceptionCaught(ctx, cause);
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            super.channelActive(ctx);
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            super.channelInactive(ctx);
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            super.channelRead(ctx, msg);
        }

        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            AtomicInteger count = heartBeatCache.get(ctx.channel().remoteAddress());
            if (count.get() < 10) {
                if (IdleState.READER_IDLE.equals(idleStateEvent.state())) {
                    log.info("未收到来自客户端的心跳检测， 第{}次", count.incrementAndGet());
                }
            } else {
                log.info("心跳检测无回应，即将关闭连接");
                ctx.close();
            }
            super.userEventTriggered(ctx, evt);
        }

        @Override
        protected void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {
            if (msg instanceof YpcInvocation) {
                invoke(ctx.channel(), (YpcInvocation) msg);
            } else if (msg instanceof HeartBeat) {
                log.info("收到来自客户端 {} 的心跳...", ctx.channel().remoteAddress(), msg);
                //收到心跳之后对相应的客户端清空心跳检查的缓存
                if (Objects.nonNull(heartBeatCache.get(msg))) {
                    heartBeatCache.get(msg).getAndAdd(0);
                } else {
                    heartBeatCache.putIfAbsent(ctx.channel().remoteAddress(), new AtomicInteger(0));
                }
            }
            log.info("什么都没走...");
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        init();
    }
}
