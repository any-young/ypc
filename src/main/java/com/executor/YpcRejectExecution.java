package com.executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * say some thing
 *
 * @version v1.0
 * @author angyang
 * @date 2018/12/11
 */
public class YpcRejectExecution extends ThreadPoolExecutor.DiscardPolicy {
    private static final Logger log = LoggerFactory.getLogger(YpcRejectExecution.class);
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
        log.info("线程池已满, 线程{} 被丢弃", r.toString());
        super.rejectedExecution(r, e);
    }
}
