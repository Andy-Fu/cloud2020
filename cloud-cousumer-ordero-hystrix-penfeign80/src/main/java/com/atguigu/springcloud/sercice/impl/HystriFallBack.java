package com.atguigu.springcloud.sercice.impl;

import com.atguigu.springcloud.entity.CommonResult;
import com.atguigu.springcloud.entity.Payment;
import com.atguigu.springcloud.sercice.PaymentHystrixFeignService;
import org.springframework.stereotype.Component;

@Component
public class HystriFallBack implements PaymentHystrixFeignService {
    @Override
    public CommonResult create(Payment payment) {
        return new CommonResult(400,"系统暂时繁忙，请稍后访问： create");
    }

    @Override
    public CommonResult getPaymentById(Long id) {
        return new CommonResult(400,"系统暂时繁忙，请稍后访问： getPaymentById");
    }

    @Override
    public String testOpenFeignTimeOut() {
        return "系统暂时繁忙，请稍后访问： testOpenFeignTimeOut";
    }

    @Override
    public String paymentHystrixOk(Integer id) {
        return "系统暂时繁忙，请稍后访问： paymentHystrixOk";
    }

    @Override
    public String paymentHystrixTimeout(Integer id) {
        return "系统暂时繁忙，请稍后访问： paymentHystrixTimeout";
    }
}
