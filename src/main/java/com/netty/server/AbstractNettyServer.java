package com.netty.server;

import com.executor.YpcExecutor;
import com.netty.message.YpcInvocation;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * say some thing
 *
 * @version v1.0
 * @author angyang
 * @date 2018/12/11
 */
public abstract class AbstractNettyServer {
    private static final Logger log = LoggerFactory.getLogger(AbstractNettyServer.class);

    protected final YpcExecutor ypcExecutor = new YpcExecutor("nio-netty-thread");

    /**
     * 线程池执行服务端本地方法
     * @param channel
     * @param invocation
     */
    protected void invoke(Channel channel, YpcInvocation invocation){
        try{
            ypcExecutor.execute(getSubmitTask(channel, invocation));
        }catch (Exception e){
            log.error(this.getClass().getName()+"线程池执行失败，执行类{}, 方法{}, 参数{}");
        }
    }

    protected abstract Runnable getSubmitTask(Channel channel, YpcInvocation invocation);


}
