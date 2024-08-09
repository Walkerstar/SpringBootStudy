package com.example.springbootstudy.beanLifecycle;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * @author MCW 2023/10/24
 */
@Component
@Slf4j
public class MyInstantiationAwareBeanPostProcessor implements InstantiationAwareBeanPostProcessor {

    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        if ("beanOfLifeCycle".equals(beanName)) {
            log.info("1 - 调用 InstantiationAwareBeanPostProcessor.postProcessBeforeInstantiation 方法");
        }
        return null;
    }

    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        if ("beanOfLifeCycle".equals(beanName)) {
            log.info("3 - 调用 InstantiationAwareBeanPostProcessor.postProcessAfterInstantiation 方法");
        }
        return true;
    }

    @Override
    public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        if ("beanOfLifeCycle".equals(beanName)) {
            log.info("4 - 调用 InstantiationAwareBeanPostProcessor.postProcessProperties 方法");
        }
        return null;
    }
}
