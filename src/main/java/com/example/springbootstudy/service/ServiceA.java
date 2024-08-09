package com.example.springbootstudy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author MCW 2024/2/18
 */
@Service
public class ServiceA {

    @Autowired
    // 解决bean循环依赖的问题
    @Lazy
    public ServiceB serviceB;

    @Async
    public void sayHello() {
        System.out.println("hello" + serviceB);
    }
}
