package com.executor;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * say some thing
 *
 * @version v1.0
 * @author angyang
 * @date 2018/12/11
 */
@Slf4j
public class YpcThreadFactory implements ThreadFactory {

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
