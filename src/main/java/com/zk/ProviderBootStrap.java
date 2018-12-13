package com.zk;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Objects;

/**
 * say some thing
 *
 * @version v1.0
 * @author angyang
 * @date 2018/12/7
 */
public class ProviderBootStrap implements InitializingBean {
    private YpcZkServer zkServer;
    private HashSet<String> providers = new HashSet<>();
    private String port;
    private String proxy;


    @Override
    public void afterPropertiesSet() throws Exception {
        if (Objects.nonNull(zkServer)
                && Objects.nonNull(providers)
                    && !StringUtils.isEmpty(port)){
            zkServer.initProviders(providers, port);
        }
    }

    public YpcZkServer getZkServer() {
        return zkServer;
    }

    public void setZkServer(YpcZkServer zkServer) {
        this.zkServer = zkServer;
    }

    public HashSet<String> getProviders() {
        return providers;
    }

    public void setProviders(HashSet<String> providers) {
        this.providers = providers;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getProxy() {
        return proxy;
    }

    public void setProxy(String proxy) {
        this.proxy = proxy;
    }
}
