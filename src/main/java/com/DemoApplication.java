package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource(locations= {"classpath:ypc-provider.xml","classpath:ypc-consumer.xml"})
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);

        try {
            Thread.sleep(10000000000l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
