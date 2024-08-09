package com.example.springbootstudy.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * @author MCW 2024/2/18
 */
@Aspect
@Component
public class ServiceAAop {

    @Before("execution(* com.example.springbootstudy.service.ServiceA.sayHello())")
    public void testAsync(){
        System.out.println("ServiceAAop");
    }
}
