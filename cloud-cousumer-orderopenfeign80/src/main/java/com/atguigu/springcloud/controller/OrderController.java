package com.atguigu.springcloud.controller;

import com.atguigu.springcloud.entity.CommonResult;
import com.atguigu.springcloud.entity.Payment;
import com.atguigu.springcloud.sercice.PaymentFeignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Slf4j
@RequestMapping("/consumer/order")
public class OrderController {
    @Resource
    private PaymentFeignService paymentFeignService;


    @RequestMapping(value = "/create")
    public CommonResult create(Payment payment) {
        return paymentFeignService.create(payment);
    }

    @GetMapping("/get/{id}")
    public CommonResult getPaymentById(@PathVariable("id") Long id) {
        return paymentFeignService.getPaymentById(id);
    }

    @GetMapping(value = "/testOpenFeignTimeOut")
    public CommonResult getOpenFeignTimeOut(){
        try {
            String result = paymentFeignService.testOpenFeignTimeOut();
            return new CommonResult(200,"测试OpenFeign超时调用成功",result);
        }catch (Exception e){
            return new CommonResult(400,"测试OpenFeign超时调用出问题，需要配置时间。");
        }

    }
}
