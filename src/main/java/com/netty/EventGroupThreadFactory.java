package com.netty;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * say some thing
 *
 * @version v1.0
 * @author angyang
 * @date 2018/12/12
 */
public class EventGroupThreadFactory implements ThreadFactory {
    private String name;
    AtomicInteger atomicInteger;

    public EventGroupThreadFactory(String name) {
        this.name = name;
        atomicInteger = new AtomicInteger(0);
    }


    @Override
    public Thread newThread(Runnable r) {
        return new Thread(r, name + "-Thread-" + atomicInteger.incrementAndGet());
    }
}
