package com.filter;

import com.balance.LoadBalanceSelector;
import com.balance.net.YpcURI;
import com.netty.message.YpcInvocation;
import com.proxy.ProxySelector;
import com.proxy.YpcProxy;

import java.lang.reflect.Method;

/**
 * say some thing
 *
 * @version v1.0
 * @author angyang
 * @date 2018/12/10
 */
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

    public String getLoadBalance() {
        return loadBalance;
    }

    public void setLoadBalance(String loadBalance) {
        this.loadBalance = loadBalance;
    }

    public void setProxy(String proxy) {
        this.proxy = proxy;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }
}
