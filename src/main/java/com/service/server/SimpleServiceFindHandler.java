package com.service.server;

import com.annotation.YpcService;
import com.collection.ConcurrentCache;
import com.exception.NotFoundServiceException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;

import java.util.Map;

/**
 * 主要实现的功能是根据给定的serviceName在本地的服务中找到符合要求的服务
 * @version v1.0
 * @author angyang
 * @date 2018/11/27
 */
public class SimpleServiceFindHandler implements ServiceFindHandler, BeanFactoryAware, BeanClassLoaderAware , InitializingBean {
    private ClassLoader classLoader;
    private ListableBeanFactory beanFactory;
    private ConcurrentCache<String, Object> objects = new ConcurrentCache<>();
    @Override
    public Object findObject(String className) throws NotFoundServiceException {
        if (className == null) {
            return null;
        }
        Object bean = objects.getCache(className);
        if (bean != null) {
            return bean;
        } else {
            try {
                Class<?> clazz = this.classLoader.loadClass(className);
                if (clazz != null) {
                    Map<String, ?> services = beanFactory.getBeansOfType(clazz);
                    if (services != null) {
                        for (Map.Entry<String, ?> entry : services.entrySet()) {
                            Object object = entry.getValue();
                            if (object != null) {
                                objects.putIfAbsentCache(className, object);
                                Class<?> beanType = object.getClass();
                                if (beanType.isAnnotationPresent(YpcService.class)) {
                                    return object;
                                }
                            }
                        }
                    }
                }
            } catch (ClassNotFoundException e) {
                throw new NotFoundServiceException(e.getMessage());
            }
        }
        throw new NotFoundServiceException("not found service:" + className + " in app");
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (ListableBeanFactory) beanFactory;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("初始化到这里");
    }
}
