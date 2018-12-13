package com.service;

import com.annotation.YpcService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * say some thing
 *
 * @version v1.0
 * @author angyang
 * @date 2018/12/10
 */
@Component
@YpcService
public class MockClient {

    public String backClient(String msg){
        System.out.println("test is ok! client say: " + msg);
        return "客户端：好了我收到消息了...消息是："+msg;
    }
}
