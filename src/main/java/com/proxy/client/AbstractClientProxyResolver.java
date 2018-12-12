package com.proxy.client;

import com.filter.YpcFilter;
import com.filter.YpcParamterFilter;
import com.netty.client.Callback;
import com.netty.client.DefaultNettyClient;
import com.netty.message.Const;
import com.netty.message.Result;
import com.netty.message.YpcInvocation;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * say some thing
 *
 * @author angyang
 * @version v1.0
 * @date 2018/12/12
 */
public abstract class AbstractClientProxyResolver {
    public Object invokeYpcMethod(YpcFilter filter, Method method, Object[] args) {
        YpcParamterFilter parameterFilter = (YpcParamterFilter) filter;
        YpcInvocation invocation = parameterFilter.doParamter(method, args);
        Callback callback = DefaultNettyClient.getInstance(10).doInvoke(invocation);
        if (Objects.isNull(callback)) {
            return null;
        }
        Result result = callback.getObject();
        if (result.getResultCode().endsWith(Const.ERROR_CODE)) {
            throw new RuntimeException("远程方法执行异常.." + Const.ERROR_CODE+callback.getObject());
        }
        return result.getMsg();
    }

}
