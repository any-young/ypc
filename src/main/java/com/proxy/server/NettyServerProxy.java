package com.proxy.server;

import com.netty.message.Const;
import com.netty.message.Result;
import com.netty.message.YpcInvocation;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

/**
 * say some thing
 *
 * @version v1.0
 * @author angyang
 * @date 2018/12/11
 */
@Slf4j
public class NettyServerProxy extends AbstractServerProxy {
    public NettyServerProxy(Object object) {
        super(object);
    }

    @Override
    Result doInvoke(YpcInvocation ypcInvocation) {
        try{
            Method method = object.getClass().getMethod(ypcInvocation.getMethodName(), ypcInvocation.getParameterTypes());
            return new Result(method.invoke(object, ypcInvocation.getParameters()));
        }catch (Exception e){
            log.error("本地执行方法失败，执行方法");
            return new Result(null, e, "服务端方法执行失败。。。", Const.ERROR_CODE);
        }
    }
}
