package com.executor;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 *
 *
 * @version v1.0
 * @author angyang
 * @date 2018/12/11
 */
public interface Executor {

    void execute(Runnable runnable);

    <T> Future<T> submit(Callable<T> callable);
}
