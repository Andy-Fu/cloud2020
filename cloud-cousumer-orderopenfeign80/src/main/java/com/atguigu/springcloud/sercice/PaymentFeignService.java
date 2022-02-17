package com.atguigu.springcloud.sercice;

import com.atguigu.springcloud.entity.CommonResult;
import com.atguigu.springcloud.entity.Payment;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Component
@FeignClient(value = "CLOUD-PAYMENT-EUREKA-SERVICE")

public interface PaymentFeignService {
    @RequestMapping(value = "/payment/create")
    public CommonResult create(Payment payment);
    @GetMapping(value = "/payment/get/{id}")
    public CommonResult getPaymentById(@PathVariable("id") Long id);

    /*
     * 测试OpenFeign的超时调用。
     *
     * */
    @GetMapping(value = "/payment/testOpenFeignTimeOut")
    public String testOpenFeignTimeOut();
}
