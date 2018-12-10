package com.service.client;

import com.filter.YpcFilter;
import com.proxy.YpcProxy;
import lombok.Data;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 代替实际的client服务作为代理在spring容器中
 * @version v1.0
 * @author angyang
 * @date 2018/12/10
 */
@Data
public class SpringConsumeBeanProxy implements FactoryBean, InitializingBean, DisposableBean {
    private Map<String, Object> cache= new ConcurrentHashMap<>();
    private Object object;
    private YpcFilter filter;
    private String className;
    private Class<?> clazz;

    @Override
    public Object getObject() {
        return object;
    }

    @Override
    public Class<?> getObjectType(){
        return object==null?Object.class:object.getClass();
    }

    @Override
    public void afterPropertiesSet() {
        object = cache.get(className);
        if (Objects.isNull(object)){
            YpcProxy proxy = filter.getProxy();
            object = proxy.getProxyBean(clazz, filter);
            cache.put(className, object);
        }
    }

    @Override
    public void destroy() throws Exception {
        cache.clear();
    }
}
