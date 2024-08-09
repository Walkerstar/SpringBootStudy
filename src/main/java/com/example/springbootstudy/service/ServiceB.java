package com.example.springbootstudy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author MCW 2024/2/18
 */
@Service
public class ServiceB {

    @Autowired
    private ServiceA serviceA;

}
