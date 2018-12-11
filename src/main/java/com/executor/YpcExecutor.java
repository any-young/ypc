package com.executor;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * say some thing
 *
 * @version v1.0
 * @author angyang
 * @date 2018/12/11
 */
public class YpcExecutor extends AbstractExecutor{
    public YpcExecutor(String name) {
        super(name);
    }

    public YpcExecutor(String name, int corePoolSize, int maximumPoolSize){
        super(name, corePoolSize, maximumPoolSize);
    }

    @Override
    public void execute(Runnable runnable) {
        executor.execute(runnable);
    }

    @Override
    public <T> Future<T> submit(Callable<T> callable) {
        return executor.submit(callable);
    }
}
