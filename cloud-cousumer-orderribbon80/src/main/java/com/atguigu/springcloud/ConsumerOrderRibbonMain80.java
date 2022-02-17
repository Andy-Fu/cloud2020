package com.atguigu.springcloud;

import com.atguigu.irule.MySelfRule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;

@SpringBootApplication
@EnableEurekaClient
//@RibbonClient(configuration = MySelfRule.class,name ="CLOUD-PAYMENT-EUREKA-SERVICE" ) //自定义ribbon的轮询方式，ribbon默认为轮询，自定义可以改成随机或者其他方式
public class ConsumerOrderRibbonMain80 {
    public static void main(String[] args) {
        SpringApplication.run(ConsumerOrderRibbonMain80.class,args);
    }
}
