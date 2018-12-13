package com.executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * say some thing
 *
 * @version v1.0
 * @author angyang
 * @date 2018/12/11
 */
public class YpcThreadFactory implements ThreadFactory {
    private static final Logger log = LoggerFactory.getLogger(YpcThreadFactory.class);

    String name;
    public YpcThreadFactory(String name) {
        super();
        this.name = name;
    }

    AtomicInteger atomicInteger = new AtomicInteger(0);
    @Override
    public Thread newThread(Runnable r) {
        log.info("创建新线程=> Thread-"+name+"-"+atomicInteger.incrementAndGet());
        String threadName = name+"-"+atomicInteger.intValue();
        return new Thread(r, threadName);
    }
}
