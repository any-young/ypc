package com.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * say some thing
 *
 * @version v1.0
 * @author angyang
 * @date 2018/12/13
 */
public class ServerDemo {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("ypc-provider.xml");
    }
}
