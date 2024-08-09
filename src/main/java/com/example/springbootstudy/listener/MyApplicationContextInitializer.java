package com.example.springbootstudy.listener;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

/** ApplicationContextInitializer 感知 IOC 容器初始化
 *  bootstrapRegistryInitializers 感知 引导 初始化
 *
 * @author MCW 2022/8/25
 */
public class MyApplicationContextInitializer implements ApplicationContextInitializer {
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        System.out.println("MyApplicationContextInitializer..............initialize");
    }
}
