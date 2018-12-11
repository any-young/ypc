package com.proxy;

import com.proxy.client.cglib.CglibProxyClient;

import java.util.HashMap;
import java.util.Map;

/**
 * say some thing
 *
 * @version v1.0
 * @author angyang
 * @date 2018/12/10
 */
public class ProxySelector {
    private static final Map<String, YpcProxy> proxyMap = new HashMap<>();

    static{
        putProxy(new CglibProxyClient());
    }

    private static void putProxy(YpcProxy ypcProxy){
        proxyMap.put(ypcProxy.getProxyName(), ypcProxy);
    }

    public static YpcProxy getYpcProxy(String proxy){
        return proxyMap.get(proxy);
    }
}
