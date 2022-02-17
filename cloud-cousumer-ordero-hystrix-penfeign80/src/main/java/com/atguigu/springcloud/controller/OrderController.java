package com.atguigu.springcloud.controller;

import com.atguigu.springcloud.entity.CommonResult;
import com.atguigu.springcloud.entity.Payment;
import com.atguigu.springcloud.sercice.PaymentHystrixFeignService;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Slf4j
@RequestMapping("/consumer/order")
@DefaultProperties(defaultFallback = "globalFallBack")
public class OrderController {
    @Resource
    private PaymentHystrixFeignService paymentHystrixFeignService;


    @RequestMapping(value = "/create")
    public CommonResult create(Payment payment) {
        return paymentHystrixFeignService.create(payment);
    }

    @GetMapping("/get/{id}")
    public CommonResult getPaymentById(@PathVariable("id") Long id) {
        return paymentHystrixFeignService.getPaymentById(id);
    }

    @GetMapping(value = "/testOpenFeignTimeOut")
    public CommonResult getOpenFeignTimeOut(){
        try {
            String result = paymentHystrixFeignService.testOpenFeignTimeOut();
            return new CommonResult(200,"测试OpenFeign超时调用成功",result);
        }catch (Exception e){
            return new CommonResult(400,"测试OpenFeign超时调用出问题，需要配置时间。");
        }
    }

    /*
     * 以下是测试Hystrix的
     * */
    @GetMapping(value = "/paymentHystrixOk/{id}")
    @HystrixCommand
    public String paymentHystrixOk(@PathVariable("id") Integer id) {
        int a=10/0;
        return paymentHystrixFeignService.paymentHystrixOk(id);
    }
    @GetMapping(value = "/paymentHystrixTimeout/{id}")
    @HystrixCommand(fallbackMethod = "paymentTimeOutFallbackMethod",commandProperties = {
            @HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds",value="1500")
    })
    public String paymentHystrixTimeout(@PathVariable("id") Integer id) {
        return paymentHystrixFeignService.paymentHystrixTimeout(id);
    }

    public String paymentTimeOutFallbackMethod(Integer id){
        return "服务器繁忙，请稍后重试,\t" + id;
    }
    public String globalFallBack(){
        return "全局服务器繁忙，请稍后重试,\t" ;
    }
}
