package com.atguigu.springcloud.controller;


import com.atguigu.springcloud.entity.CommonResult;
import com.atguigu.springcloud.entity.Payment;
import com.atguigu.springcloud.setvices.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/payment")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;
    @Value("${server.port}")
    private String prot;
    @Autowired
    private DiscoveryClient discoveryClient;

    @RequestMapping(value = "/create")
    public CommonResult create(@RequestBody Payment payment) {
        int result = paymentService.create(payment);
        log.info("****插入数据成功="+result);
        if (result > 0) {
            return new CommonResult(200,"success,server port:"+prot, result );
        }else {
            return new CommonResult(400,"fail,server port:"+prot);
        }
    }

    @GetMapping(value = "/get/{id}")
    public CommonResult getPaymentById(@PathVariable("id") Long id) {
        Payment payment = paymentService.getPaymentById(id);
        log.info("****查询数据成功="+payment);
        if (payment != null) {
            return new CommonResult(200,"success,server port:"+prot,payment);
        }else {
            return new CommonResult(400,"fail，查询id="+id+" ,server port:"+prot);
        }
    }

    @GetMapping("/discovery")
    public Object getDisCoveryInfo(){
        List<String> services = discoveryClient.getServices();
        for (String serverName : services) {
            log.info("*****注册服务名称为："+serverName);
            List<ServiceInstance> instances = discoveryClient.getInstances(serverName);
            for (ServiceInstance element : instances) {
                log.info("serverName : " + serverName + "\t" + element.getServiceId() + "\t" + element.getHost() + "\t" + element.getPort() + "\t" + element.getUri());
            }
        }
        // 根据servicei获取特定的服务信息
        List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMENT-EUREKA-SERVICE");
        for (ServiceInstance element : instances) {
            log.info(element.getServiceId() + "\t" + element.getHost() + "\t" + element.getPort() + "\t" + element.getUri());
        }
        return instances;
    }

    /*
     * 测试自定义的负载均衡算法
     * */
    @GetMapping(value = "/testCustomLoadBanlance")
    public String testCustomLoadBanlance(){
        return "由端口为： " + prot +"\t的服务响应";
    }


    /*
    * 测试OpenFeign的超时调用。
    *
    * */
    @GetMapping(value = "/testOpenFeignTimeOut")
    public String testOpenFeignTimeOut(){
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            log.info("thread sleep expectioon: " + e.getMessage());
        }
        return "由端口为： " + prot +"\t的服务响应";
    }
}
