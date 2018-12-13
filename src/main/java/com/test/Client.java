package com.test;

import com.service.MockClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * say some thing
 *
 * @version v1.0
 * @author angyang
 * @date 2018/11/28
 */
@Service
public class Client {
    private static final Logger log = LoggerFactory.getLogger(Client.class);
    @Resource(name = "mockClient")
    private MockClient mockClient;

    public String testClient(String msg){
        log.info("进入测试方法");
        String string = mockClient.backClient("发给客户端！...: "+msg);
        System.out.println("收到的消息是："+string);
        return "收到的消息是："+string;
    }
}
