package com.filter;

import com.balance.LoadBalanceSelector;
import com.balance.net.YpcURI;
import com.netty.message.YpcInvocation;
import com.proxy.ProxySelector;
import com.proxy.YpcProxy;
import lombok.Data;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * say some thing
 *
 * @version v1.0
 * @author angyang
 * @date 2018/12/10
 */
@Data
public class YpcParamterFilter implements YpcFilter {
    private String loadBalance;
    private String proxy;
    private String protocol;
    @Override
    public YpcProxy getProxy(){
        return ProxySelector.getYpcProxy(proxy);
    }

    @Override
    public YpcInvocation doParamter(Method method, Object[] objects) {
        Class declareClass = method.getDeclaringClass();
        YpcInvocation invocation = new YpcInvocation();
        invocation.setClassName(declareClass.getName());
        invocation.setIntf(declareClass);
        invocation.setProtocol(protocol);
        invocation.setMethodName(method.getName());
        invocation.setParameters(objects);
        invocation.setTimeout("3000");
        invocation.setParameterTypes(method.getParameterTypes());
        if(!"void".equals(method.getReturnType().getName()))
            invocation.setReturnType(true);
        YpcURI uri = LoadBalanceSelector.getLoadBalance(loadBalance).getUri(declareClass.getName());
        invocation.setYpcURI(uri);
        return invocation;
    }
}
