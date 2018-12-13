package com.proxy.server;

import com.netty.message.Result;
import com.netty.message.YpcInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * say some th
 * ing
 *
 * @version v1.0
 * @author angyang
 * @date 2018/12/11
 */
public abstract class AbstractServerProxy implements ServerProxy {
    private static final Logger log = LoggerFactory.getLogger(AbstractServerProxy.class);
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
        if (ypcInvocation.getIntf().isAssignableFrom(object.getClass())){
            return doInvoke(ypcInvocation);
        }
        return null;
    }

    abstract Result doInvoke(YpcInvocation ypcInvocation);
}
