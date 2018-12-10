package com.proxy;

import com.proxy.cglib.CglibProxyClient;

/**
 * say some thing
 *
 * @version v1.0
 * @author angyang
 * @date 2018/12/10
 */
public enum ProxyEnum {
    CGLIB(new CglibProxyClient());

    private String name;
    private YpcProxy ypcProxy;
    ProxyEnum(YpcProxy ypcProxy){
        this.ypcProxy = ypcProxy;
        this.name = ypcProxy.getProxyName();
    }

    public String getName() {
        return name;
    }

    public YpcProxy getYpcProxy() {
        return ypcProxy;
    }
}
