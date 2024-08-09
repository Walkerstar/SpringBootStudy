package com.example.springbootstudy.beanLifecycle;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * @author MCW 2023/10/24
 */
@Component
@Slf4j
public class MyBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if ("beanOfLifeCycle".equals(beanName)) {
            log.info("调用 BeanPostProcessor.postProcessBeforeInitialization 方法");
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if ("beanOfLifeCycle".equals(beanName)) {
            log.info("调用 BeanPostProcessor.postProcessAfterInitialization 方法");
        }
        return bean;
    }
}
