package com.atguigu.springcloud.setvices.impl;

import cn.hutool.core.util.IdUtil;
import com.atguigu.springcloud.dao.PaymentDao;
import com.atguigu.springcloud.entity.Payment;
import com.atguigu.springcloud.setvices.PaymentService;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@DefaultProperties(defaultFallback = "globalFallBack")
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    private PaymentDao paymentDao;
    @Override
    public int create(Payment payment) {
        return paymentDao.create(payment);
    }

    @Override
    @HystrixCommand
    public Payment getPaymentById(Long id) {
        int a=10/0;
        return paymentDao.getPaymentById(id);
    }



    /*
     * 以下是测试Hystrix的
     * */
    @Override
    public String paymentHystrixOk(Integer id) {
        return "当前线程： " + Thread.currentThread().getId() + "   ;paymentHystrixOk: " + id;
    }

    @Override
    @HystrixCommand(fallbackMethod = "fallBackHandler",commandProperties = {
            @HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds",value="3000")
    })
    public String paymentHystrixTimeout(Integer id) {
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "当前线程： " + Thread.currentThread().getId() + "   ;paymentHystrixTimeout" + id;
    }
    public String fallBackHandler(Integer id){
        return "服务器繁忙，请稍后重试,\t" + id;
    }
    public Object globalFallBack(){
        return "全局服务器繁忙，请稍后重试,\t" ;
    }





    /*=========以下是Hystrix服务垄断的代码*/
    /*
     * 服务垄断：服务降级 （直接返回fallback信息） ---》熔断（类似保险丝跳闸，不接受访问）--》服务恢复调用链路
     * 服务垄断时垄断状态由：开启-》半开-》关闭
     *
     * 触发服务垄断：定时间窗口期内 --》达到访问阈值、服务失败率达到一定的值
     *
     * @HystrixProperty的属性配置参照HystrixCommandProperties这个类
     * */
    @Override
    @HystrixCommand(fallbackMethod = "circuitBreakHandler",commandProperties = {
            @HystrixProperty(name="circuitBreaker.enabled",value ="true" ),//开启断路器
            @HystrixProperty(name="circuitBreaker.requestVolumeThreshold",value ="10" ),//请求阈值
            @HystrixProperty(name="circuitBreaker.sleepWindowInMilliseconds",value ="10000" ),//窗口时间期
            @HystrixProperty(name="circuitBreaker.errorThresholdPercentage",value ="60" )//错误率百分率
    })
    public String circuitBreak(Integer id) {
        // 当传入的参数小于0时手动抛出一个运行时异常，手动触发异常让Hystrix处理
        if (id<0) {
            throw new RuntimeException("******id 不能负数");
        }
        String simpleUUID = IdUtil.simpleUUID();
        return "当前线程： " + Thread.currentThread().getId() + "   ;circuitBreak访问成功：" + id + "\t" + simpleUUID;
    }

    private String circuitBreakHandler(Integer id){
        return "circuitBreakHandler服务器繁忙，请稍后重试,\t" + id;
    }
}

