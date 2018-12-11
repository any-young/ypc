package com.proxy.server;

import com.netty.message.Result;
import com.netty.message.YpcInvocation;
import lombok.extern.slf4j.Slf4j;

/**
 * say some th
 * ing
 *
 * @version v1.0
 * @author angyang
 * @date 2018/12/11
 */
@Slf4j
public abstract class AbstractServerProxy implements ServerProxy {
    protected Object object;

    public AbstractServerProxy(Object object) {
        this.object = object;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    @Override
    public Result invoke(YpcInvocation ypcInvocation) {
        if (ypcInvocation.getClass().isAssignableFrom(object.getClass())){
            return doInvoke(ypcInvocation);
        }
        return null;
    }

    abstract Result doInvoke(YpcInvocation ypcInvocation);
}
