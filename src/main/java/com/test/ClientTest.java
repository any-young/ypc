package com.test;

import com.service.MockClient;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * say some thing
 *
 * @version v1.0
 * @author angyang
 * @date 2018/12/13
 */
public class ClientTest {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("ypc-consumer.xml");
        final MockClient client = applicationContext.getBean(MockClient.class);
        String result = client.backClient("测试！");
        System.out.println("result... "+result);
    }
}
