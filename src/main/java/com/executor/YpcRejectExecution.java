package com.executor;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * say some thing
 *
 * @version v1.0
 * @author angyang
 * @date 2018/12/11
 */
@Slf4j
public class YpcRejectExecution extends ThreadPoolExecutor.DiscardPolicy {

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
        log.info("线程池已满, 线程{} 被丢弃", r.toString());
        super.rejectedExecution(r, e);
    }
}
