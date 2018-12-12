package com.netty.client;

import com.balance.net.YpcURI;
import com.netty.EventGroupThreadFactory;
import com.netty.message.HeartBeat;
import com.netty.message.Result;
import com.netty.message.YpcInvocation;
import com.protocol.ProtocolSelector;
import com.protocol.Serializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * say some thing
 *
 * @author angyang
 * @version v1.0
 * @date 2018/12/11
 */
@Slf4j
public class DefaultNettyClient extends AbstractNettyClient {
    ScheduledExecutorService executorService;
    private NioEventLoopGroup eventLoopGroup;
    private DefaultEventLoopGroup defaultEventLoopGroup;
    private final int TOP_LENGTH = 129 >> 1 | 34; // 数据协议头
    private final int TOP_HEARTBEAT = 129 >> 1 | 36; // 心跳协议头
    private Bootstrap bootstrap;
    private static int threads;

    public static class InitialBean {
        public static DefaultNettyClient defaultNettyClient = new DefaultNettyClient();
    }

    public static DefaultNettyClient getInstance(int threads) {
        DefaultNettyClient.threads = threads;
        return InitialBean.defaultNettyClient;
    }

    private DefaultNettyClient() {
        executorService = Executors.newScheduledThreadPool(2);
        /*executorService.scheduleAtFixedRate(() -> clearChannels()
                , 10, 10, TimeUnit.SECONDS);*/
        eventLoopGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors(), new EventGroupThreadFactory("client-event-group"));
        defaultEventLoopGroup = new DefaultEventLoopGroup(threads, new EventGroupThreadFactory("defaultEventLoopGroup-thread"));
        bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, false)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .option(ChannelOption.SO_SNDBUF, 10 * 1024 * 1024)
                .option(ChannelOption.SO_RCVBUF, 10 * 1024 * 1024)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(defaultEventLoopGroup);
                        ch.pipeline().addLast(new ClientEnCodeHandler());
                        ch.pipeline().addLast(new ClientDecodeHandler());
                        ch.pipeline().addLast(new IdleStateHandler(10, 0, 0));
                        ch.pipeline().addLast(new NettyClientInBoundHandler());
                    }
                });
    }

    class ClientDecodeHandler extends ByteToMessageDecoder{

        @Override
        protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
            int begin;
            if (in.readableBytes()<12){
                return;
            }
            while(true){
                begin = in.readerIndex();
                in.markReaderIndex();
                int header = in.readInt();
                if (header == TOP_LENGTH){
                    break;
                }
                in.resetReaderIndex();
                in.readByte();
            }
            int protocol =in.readInt();
            int length = in.readInt();
            int readable = in.readableBytes();
            if (length > readable){
                in.readerIndex(begin);
                return;
            }
            byte[] bytes = new byte[length];
            in.readBytes(bytes);
            Result result = ProtocolSelector.getProtocol(protocol).transToObject(Result.class, bytes);
            out.add(result);
        }
    }

    class ClientEnCodeHandler extends MessageToByteEncoder<Object>{

        @Override
        protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
            if (msg instanceof YpcInvocation){
                ByteBuffer byteBuffer = getByteBuffer((YpcInvocation)msg);
                out.writeBytes(byteBuffer);
            }else if(msg instanceof HeartBeat){
                Serializer serializer = ProtocolSelector.getProtocol(ProtocolSelector.DEFAULT_PROTOCOL);
                byte[] bytes = serializer.transToByte(msg);
                out.writeInt(TOP_HEARTBEAT);
                out.writeInt(ProtocolSelector.DEFAULT_PROTOCOL);
                out.writeInt(bytes.length);
                out.writeBytes(bytes);
            }
        }

        private ByteBuffer getByteBuffer(YpcInvocation msg) throws IOException {
            Integer protocol = Integer.valueOf(msg.getProtocol());
            Serializer serializer = ProtocolSelector.getProtocol(protocol);
            byte[] bytes = serializer.transToByte(msg);
            ByteBuffer byteBuffer = ByteBuffer.allocate(4+4+4+bytes.length);
            byteBuffer.putInt(TOP_LENGTH);
            byteBuffer.putInt(protocol);
            byteBuffer.putInt(bytes.length);
            byteBuffer.put(bytes);
            byteBuffer.flip();
            return byteBuffer;
        }
    }

    class NettyClientInBoundHandler extends SimpleChannelInboundHandler<Result>{
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
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
            IdleStateEvent e = (IdleStateEvent) evt;
            if(e.state().equals(IdleState.READER_IDLE)
                    ||e.state().equals(IdleState.WRITER_IDLE)
                    ||e.state().equals(IdleState.ALL_IDLE)
            ){
                ctx.writeAndFlush(new HeartBeat());
            }
        }

        @Override
        protected void messageReceived(ChannelHandlerContext ctx, Result result) throws Exception {
            setCallBack(result);
        }
    }

    @Override
    public ChannelFuture connect(YpcURI uri) throws InterruptedException {
        return bootstrap.connect(uri.getRemoteAddress(), Integer.valueOf(uri.getPort())).sync();
    }
}
