package com.netty.server;

import com.executor.YpcExecutor;
import com.netty.message.YpcInvocation;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

/**
 * say some thing
 *
 * @version v1.0
 * @author angyang
 * @date 2018/12/11
 */
@Slf4j
public abstract class AbstractNettyServer {

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
