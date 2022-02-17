package com.atguigu.springcloud.controller;

import com.atguigu.springcloud.entity.CommonResult;
import com.atguigu.springcloud.entity.Payment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@Slf4j
@RequestMapping("/consumer/order")
public class OrderController {
    @Autowired
    private RestTemplate restTemplate;
    private String PAYMENT_URI="http://localhost:8001";

    @RequestMapping(value = "/create")
    public CommonResult create(Payment payment){
        CommonResult paymentRab = restTemplate.postForObject(PAYMENT_URI + "/payment/create", payment, CommonResult.class);
       return paymentRab;
    }

    @GetMapping("/get/{id}")
    public CommonResult getPaymentById(@PathVariable("id")Long id){
        CommonResult forObject = restTemplate.getForObject(PAYMENT_URI + "/payment/get/" + id, CommonResult.class);

        return forObject;
    }
}
