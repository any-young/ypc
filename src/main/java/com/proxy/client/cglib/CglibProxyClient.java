package com.proxy.client.cglib;


import com.filter.YpcFilter;
import com.proxy.YpcProxy;

/**
 * say some thing
 *
 * @version v1.0
 * @author angyang
 * @date 2018/12/10
 */
public class CglibProxyClient implements YpcProxy {
    private final String name = "CGLIB";


    private CglibMethodIntercept intercept = new CglibMethodIntercept();

    @Override
    public String getProxyName() {
        return name;
    }

    @Override
    public <T> T getProxyBean(Class<T> tClass, YpcFilter filter) {
        return intercept.getProxy(tClass, filter);
    }
}
