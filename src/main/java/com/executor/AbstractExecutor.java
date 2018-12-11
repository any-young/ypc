package com.executor;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * say some thing
 *
 * @version v1.0
 * @author angyang
 * @date 2018/12/11
 */
public abstract class AbstractExecutor implements Executor {

    ExecutorService executor;
    private static final int corePoolSize = 100;
    private static final int maximumPoolSize = 300;

    public AbstractExecutor(String name) {
            executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, 2, TimeUnit.SECONDS, new ArrayBlockingQueue<>(200), new YpcThreadFactory(name), new YpcRejectExecution());
    }

    public AbstractExecutor(String name, int corePoolSize, int maximumPoolSize){
        executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, 2 ,TimeUnit.SECONDS, new ArrayBlockingQueue<>(200), new YpcThreadFactory(name), new YpcRejectExecution());
    }
}
