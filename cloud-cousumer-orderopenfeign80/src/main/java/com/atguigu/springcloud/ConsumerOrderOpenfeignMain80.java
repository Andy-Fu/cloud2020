package com.atguigu.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ConsumerOrderOpenfeignMain80 {
    public static void main(String[] args) {
        SpringApplication.run(ConsumerOrderOpenfeignMain80.class,args);
    }
}
