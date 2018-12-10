package com.proxy;

import com.filter.YpcFilter;
/**
 * say some thing
 *
 * @version v1.0
 * @author angyang
 * @date 2018/12/10
 */
public interface YpcProxy {
    String getProxyName();
    <T> T getProxyBean(Class<T> tClass, YpcFilter filter);
}
