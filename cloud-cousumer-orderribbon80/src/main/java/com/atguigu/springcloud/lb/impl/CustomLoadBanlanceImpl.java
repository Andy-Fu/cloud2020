package com.atguigu.springcloud.lb.impl;

import com.atguigu.springcloud.lb.ICustomLoadBanlance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自定义负载均衡轮询算法
 *
 * */
@Component
public class CustomLoadBanlanceImpl implements ICustomLoadBanlance {
    // 初始进入方法次数，掉一次方法将加1
    private AtomicInteger currIndex = new AtomicInteger(0);
     /*
     * 获取当前该接口的访问次数
     * 初始访问值为1，每访问一次，将+1，
     * 利用cas和自选锁的方式实现。
     * compareAndSet（当前值，期望值）：cas的一种api，
     *
     * 只有满足期望才会跳出while循环
     * */
    public final int incrementAndGetModulo() {
        int curent;
        int nextExpect;
        do {
             curent = currIndex.get();
             nextExpect = curent >= Integer.MAX_VALUE ? 0 : curent + 1;
             System.out.println("当前为第：" + curent + "访问" + "\t 期望值： " + nextExpect);
        }while (!currIndex.compareAndSet(curent,nextExpect));
        return currIndex.get();
    }

    /*
    * 利用访问次数对总的服务数进行求余，余数就为服务list的下标
    *
    * */
    @Override
    public ServiceInstance getApplyService(List<ServiceInstance> serviceInstanceList) {
        Integer integer = incrementAndGetModulo();
        if (serviceInstanceList == null || serviceInstanceList.size() <= 0) {
            return null;
        }else {
            int size = serviceInstanceList.size();
            int intenceIndex = integer % size ;
            ServiceInstance serviceInstance = serviceInstanceList.get(intenceIndex);
            return serviceInstance;
        }
    }
}
