package com.balance.net;

import lombok.Data;

import java.io.Serializable;
import java.util.StringJoiner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * say some thing
 *
 * @version v1.0
 * @author angyang
 * @date 2018/12/5
 */
@Data
public class YpcURI implements Serializable {
    private static final long MIN_WAIT_TIME = 3000l;
    private String remoteAddress;
    private String port;
    private String timeout;
    private CountDownLatch count = new CountDownLatch(1);
    private Lock lock = new ReentrantLock();
    public YpcURI(String remoteAddress, String port, String timeout){
        this.remoteAddress = remoteAddress;
        this.port = port;
        this.timeout = timeout;
    }

    public boolean tryLock() throws InterruptedException {
        return lock.tryLock(MIN_WAIT_TIME, TimeUnit.MILLISECONDS);
    }

    public void releaseLock(){
        lock.unlock();
    }

    public YpcURI(String remoteAddress, String port){
        this(remoteAddress, port, "3000");
    }

    public void await(long time) throws InterruptedException {
        time = time==0?MIN_WAIT_TIME:time;
        count.await(time, TimeUnit.MILLISECONDS);
    }

    public void countDown(){
        count.countDown();
    }


    @Override
    public String toString() {
        return new StringJoiner(", ", YpcURI.class.getSimpleName() + "[", "]")
                .add("remoteAddress='" + remoteAddress + "'")
                .add("port='" + port + "'")
                .add("timeout='" + timeout + "'")
                .toString();
    }
}
