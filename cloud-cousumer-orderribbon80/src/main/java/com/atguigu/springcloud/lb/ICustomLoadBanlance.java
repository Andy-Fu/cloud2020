package com.atguigu.springcloud.lb;

import org.springframework.cloud.client.ServiceInstance;

import java.util.List;

public interface ICustomLoadBanlance {
    public ServiceInstance getApplyService(List<ServiceInstance> serviceInstanceList);
}
