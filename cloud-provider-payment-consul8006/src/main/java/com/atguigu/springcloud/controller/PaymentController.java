package com.atguigu.springcloud.controller;

import com.atguigu.springcloud.entity.CommonResult;
import com.atguigu.springcloud.entity.Payment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/payment")
public class PaymentController {
    @Value("${server.port}")
    private String port;
    @GetMapping(value = "/consul")
    public String paymentZk(){
        return "SpringCloud With Consul: " + port +"\t" + UUID.randomUUID().toString();
    }

    @RequestMapping(value = "/create")
    public CommonResult create(@RequestBody Payment payment) {
        return null;
    }

    @GetMapping(value = "/get/{id}")
    public CommonResult getPaymentById(@PathVariable("id") Long id) {
        return null;
    }

    @GetMapping("/discovery")
    public Object getDisCoveryInfo(){
        return null;
    }
}
