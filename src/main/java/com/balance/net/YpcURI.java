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
public class YpcURI implements Serializable {
    /**
     * 序列化号
     */
    private static final long serialVersionUID = 1L;

    private transient static final long MIN_WAIT_TIME = 3000l;
    private String remoteAddress;
    private String port;
    private String timeout = "3000";
    private transient CountDownLatch count = new CountDownLatch(1);
    private transient Lock lock = new ReentrantLock();

    public String getRemoteAddress() {
        return remoteAddress;
    }

    public void setRemoteAddress(String remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }

    public YpcURI() {
        super();
    }
    public YpcURI(String remoteAddress, String port, String timeout){
        super();
        this.remoteAddress = remoteAddress;
        this.port = port;
        this.timeout = timeout;
    }

    public boolean tryLock() throws InterruptedException {
        return this.lock.tryLock(MIN_WAIT_TIME, TimeUnit.MILLISECONDS);
    }

    public void releaseLock(){
        this.lock.unlock();
    }

    public YpcURI(String remoteAddress, String port){
        super();
        this.remoteAddress = remoteAddress;
        this.port = port;
    }

    public void await(long time) throws InterruptedException {
        time = time==0?MIN_WAIT_TIME:time;
        this.count.await(time, TimeUnit.MILLISECONDS);
    }

    public void countDown(){
        this.count.countDown();
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
