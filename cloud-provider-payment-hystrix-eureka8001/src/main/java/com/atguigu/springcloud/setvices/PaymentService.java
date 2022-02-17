package com.atguigu.springcloud.setvices;

import com.atguigu.springcloud.entity.Payment;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;


public interface PaymentService {
    public int create(Payment payment);
    public Payment getPaymentById(@Param("id") Long id);

    /*
    * 以下是测试Hystrix的
    * */
    public String paymentHystrixOk(Integer id);
    public String paymentHystrixTimeout(Integer id);


    /*=========以下是Hystrix服务垄断的代码*/
    public String circuitBreak(Integer id);
}
