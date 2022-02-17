package com.atguigu.springcloud.controller;

import com.atguigu.springcloud.entity.CommonResult;
import com.atguigu.springcloud.entity.Payment;
import com.atguigu.springcloud.lb.impl.CustomLoadBanlanceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/consumer/order")
public class OrderController {
    @Autowired
    private RestTemplate restTemplate;
    private String PAYMENT_URI="http://CLOUD-PAYMENT-EUREKA-SERVICE";
    //单机版端口可以写死，集群版需要用服务名
    //private String PAYMENT_URI="http://localhost:8001/payment";
    @Autowired
    private DiscoveryClient discoveryClient;

    /*
    * 自定义负载均衡的轮询方法
    * */
    @Autowired
    private CustomLoadBanlanceImpl customLoadBanlance;

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
        List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-CONSUMER-ORDER");
        for (ServiceInstance element : instances) {
            log.info(element.getServiceId() + "\t" + element.getHost() + "\t" + element.getPort() + "\t" + element.getUri());
        }
        return instances;
    }

    /*
    * 自定义负载均衡算法轮询算法测试
    *
    * 使用自定义轮询时需要把@LoadBalanced注释掉
    * */
    @GetMapping(value = "/testCustomLoadBanlance")
    public String testCustomLoadBanlance(){
        List<ServiceInstance> instances = discoveryClient.getInstances("cloud-payment-eureka-service");
        ServiceInstance applyService = customLoadBanlance.getApplyService(instances);
        URI uri = applyService.getUri();
        String resultInfo = restTemplate.getForObject(uri + "/payment/testCustomLoadBanlance", String.class);
        if (resultInfo == null) {
            return "请求数据为空";
        }
        return resultInfo;
    }
}
