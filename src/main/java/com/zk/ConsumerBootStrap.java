package com.zk;

import org.springframework.beans.factory.InitializingBean;

import java.util.List;
import java.util.Objects;

/**
 * say some thing
 *
 * @version v1.0
 * @author angyang
 * @date 2018/11/28
 */
public class ConsumerBootStrap implements InitializingBean {
    private YpcZkServer zkServer;
    private String loadBalance;
    private List<String> classes;

    @Override
    public void afterPropertiesSet() throws Exception {
        if(Objects.nonNull(zkServer))
            zkServer.initConsumers(classes);//init 初始化消费者
            //register
            //ypcZkServer.initServer(classes);
    }

    public YpcZkServer getZkServer() {
        return zkServer;
    }

    public void setZkServer(YpcZkServer zkServer) {
        this.zkServer = zkServer;
    }

    public String getLoadBalance() {
        return loadBalance;
    }

    public void setLoadBalance(String loadBalance) {
        this.loadBalance = loadBalance;
    }

    public List<String> getClasses() {
        return classes;
    }

    public void setClasses(List<String> classes) {
        this.classes = classes;
    }
}
