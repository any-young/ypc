package com.zk;

import com.protocol.Serializer;
import com.zk.YpcZkServer;
import lombok.Data;
import org.springframework.beans.factory.InitializingBean;

import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * say some thing
 *
 * @version v1.0
 * @author angyang
 * @date 2018/11/28
 */
@Data
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
}
