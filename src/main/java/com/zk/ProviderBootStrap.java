package com.zk;

import lombok.Data;
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
@Data
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
}
