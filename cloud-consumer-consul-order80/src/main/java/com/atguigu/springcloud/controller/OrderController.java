package com.atguigu.springcloud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Value("${server.port}")
    private String port;
    @Autowired
    private RestTemplate restTemplate;

    private String PAYMENT_URI = "http://order-provider-consul-service/";
    @GetMapping(value = "/payment")
    public String paymentZkInfo(){
        return restTemplate.getForObject(PAYMENT_URI+"/payment/consul",String.class);
       // return "SpringCloud with Zookeeper: " + port + UUID.randomUUID().toString();
    }
    @GetMapping(value = "/consul")
    public String orderZkInfo(){
         return "SpringCloud with Consul: " + port + "\t" + UUID.randomUUID().toString();
    }
}
