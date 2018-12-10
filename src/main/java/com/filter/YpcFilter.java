package com.filter;

import com.netty.message.YpcInvocation;
import com.proxy.YpcProxy;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * say some thing
 *
 * @version v1.0
 * @author angyang
 * @date 2018/12/10
 */
public interface YpcFilter {
    //组装远程请求参数
    YpcInvocation doParamter(Method method, Object[] objects);
    //返回默认的客户端代理
    YpcProxy getProxy();
}
