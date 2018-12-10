package com.proxy.cglib;



import com.filter.YpcFilter;
import com.filter.YpcParamterFilter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * say some thing
 *
 * @version v1.0
 * @author angyang
 * @date 2018/12/10
 */
public class CglibMethodIntercept implements MethodInterceptor {
    private YpcFilter filter;
    private Enhancer enhancer = new Enhancer();

    public <T> T getProxy(Class<T> clazz, YpcFilter filter) {
        this.filter = filter;
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(this);
        return (T)enhancer.create();
    }
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        return proxy.invokeSuper(obj, args);
    }
}
