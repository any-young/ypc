package com.netty.client;

import com.balance.net.YpcURI;
import com.netty.message.Result;
import com.netty.message.YpcInvocation;
import io.netty.channel.ChannelFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;

/**
 * say some thing
 *
 * @author angyang
 * @version v1.0
 * @date 2018/12/11
 */
@Slf4j
public abstract class AbstractNettyClient {

    private ConcurrentHashMap<String, ChannelFuture> channels = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, Callback> call_backs = new ConcurrentHashMap<>();

    private String getRemoteAddress(YpcURI uri) {
        return new StringBuilder(uri.getRemoteAddress()).append(":").append(uri).append(uri.getPort()).toString();
    }

    public Callback doInvoke(YpcInvocation invocation) {
        if (Objects.nonNull(invocation)) {
            String remoteAddress = getRemoteAddress(invocation.getYpcURI());
            ChannelFuture channelFuture = channels.get(remoteAddress);
            if (channelFuture == null) {
                channelFuture = getChannel(invocation.getYpcURI());
            }
            return sendMessage(invocation, channelFuture);
        }
        return null;
    }

    public final void clearChannels(){
        if (!CollectionUtils.isEmpty(channels)) {
            for (String key : channels.keySet()) {
                ChannelFuture channelFuture = channels.get(key);
                if (Objects.nonNull(channelFuture) && !channelFuture.channel().isActive()) {
                    channels.remove(key);
                }
            }
        }
    }

    private ChannelFuture getChannel(YpcURI uri) {
        String remoteAddress = getRemoteAddress(uri);
        try {
            if (uri.tryLock()) {
                ChannelFuture channelFuture = channels.get(remoteAddress);
                if (channelFuture == null) {
                    channelFuture = connect(uri);
                    if (channelFuture.isSuccess()) {
                        channels.putIfAbsent(remoteAddress, channelFuture);
                        uri.countDown();
                        return channelFuture;
                    } else {
                        uri.releaseLock();
                        throw new RuntimeException("connection to netty server failed ");
                    }
                }
            }
            //如果有一个线程获取了锁，其他线程等待改线程连接服务端结束
            uri.await(0);
            ChannelFuture channelFuture = channels.get(remoteAddress);
            if (channelFuture != null) {
                return channelFuture;
            }
        } catch (Exception e) {
            log.error("connect to server failed ... server is {}:{}", uri.getRemoteAddress(), uri.getPort() , e);
        }
        return null;
    }

    public abstract ChannelFuture connect(YpcURI uri) throws InterruptedException;

    private Callback sendMessage(YpcInvocation invocation, ChannelFuture channelFuture) {
        Callback callback = null;
        if (invocation.isReturnType()){
            callback = initCallBack(invocation);
        }
        channelFuture.channel().writeAndFlush(invocation);
        return callback;
    }

    /**
     * 初始化callback
     * @param invocation
     * @return
     */
    private Callback initCallBack(YpcInvocation invocation) {
        String uuid = UUID.randomUUID().toString();
        Callback callback = new ResultCallBack(3000, invocation.getClassName());
        call_backs.putIfAbsent(uuid, callback);
        return callback;
    }

    protected void setCallBack(Result result){
        if (Objects.nonNull(result)){
            String serialNo = result.getSerialNo();
            Callback callback = call_backs.get(serialNo);
            if (Objects.nonNull(callback)){
                callback.putResult(result);
                call_backs.remove(serialNo);
            }else{
                log.warn("未找到对应的callback", result);
            }
        }
    }
}
