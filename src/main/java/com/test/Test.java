package com.test;

import com.service.MockClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * say some thing
 *
 * @version v1.0
 * @author angyang
 * @date 2018/11/28
 */
@RestController
@CrossOrigin
@Slf4j
public class Test {
    @Resource
    private MockClient mockClient;

    @RequestMapping(value = "/test/client", method = RequestMethod.POST)
    public void testClient(){
        log.info("进入测试方法");
        mockClient.backClient("发消息给客户端！...");
    }
}
