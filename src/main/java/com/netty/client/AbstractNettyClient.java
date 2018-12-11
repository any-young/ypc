package com.netty.client;

import com.balance.net.YpcURI;
import com.netty.message.YpcInvocation;
import io.netty.channel.ChannelFuture;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * say some thing
 *
 * @author angyang
 * @version v1.0
 * @date 2018/12/11
 */
public abstract class AbstractNettyClient {

    private ConcurrentHashMap<String, ChannelFuture> channels = new ConcurrentHashMap<>();

    private String getRemoteAddress(YpcURI uri) {
        return new StringBuilder(uri.getRemoteAddress()).append(":").append(uri).append(uri.getPort()).toString();
    }

    Callback doInvoke(YpcInvocation invocation) {
        if (Objects.nonNull(invocation)) {
            String remoteAddress = getRemoteAddress(invocation.getYpcURI());
            ChannelFuture channelFuture = channels.get(remoteAddress);
            if (channelFuture == null) {
                getChannel(invocation.getYpcURI());
            }
            return sendMessage(invocation, channelFuture);
        }
        return null;

    }


    private ChannelFuture getChannel(YpcURI uri) {
        try {
            String remoteAddress = getRemoteAddress(uri);
            try {
                ChannelFuture channelFuture = channels.get(remoteAddress);
                if (channelFuture == null) {
                    channelFuture = connect();
                    if (channelFuture.isSuccess()) {
                        channels.putIfAbsent(remoteAddress, channelFuture);
                        uri.countDown();
                        return channelFuture;
                    } else {
                        throw new RuntimeException("");
                    }
                }
                uri.await(0);
                ChannelFuture channelFuture1 = channels.get(remoteAddress);
                if (channelFuture1 != null) {
                    return channelFuture1;
                }
            } finally {
                uri.releaseLock();
            }
        } catch (Exception e) {

        }
        return null;
    }

    public abstract ChannelFuture connect();

    private Callback sendMessage(YpcInvocation invocation, ChannelFuture channelFuture) {
        return null;
    }
}
