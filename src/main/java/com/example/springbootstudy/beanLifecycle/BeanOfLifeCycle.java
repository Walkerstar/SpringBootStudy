package com.example.springbootstudy.beanLifecycle;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 验证 bean 的生命周期
 *
 * @author MCW 2023/10/24
 */
@Component
@Slf4j
public class BeanOfLifeCycle implements InitializingBean, BeanNameAware, DisposableBean, ApplicationContextAware {

    private int id;
    private String name;

    public BeanOfLifeCycle() {
        id = 1;
        name = "lifeCycle";
        log.info("2. 调用构造函数");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        log.info("属性注入 id");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        log.info("属性注入 name");
    }

    @Override
    public void setBeanName(String s) {
        log.info("调用 BeanNameAware.setBeanName() 方法");
    }

    @Override
    public void destroy() throws Exception {
        log.info("调用 DisposableBean.destroy() 方法");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("调用 InitializingBean.afterPropertiesSet() 方法");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        log.info("调用 ApplicationContextAware.setApplicationContext() 方法");
    }

    public void myDestroy() {
        log.info("调用 destroy-method 方法");
    }

    public void myInit() {
        log.info("调用 init-method 方法");
    }

    @Override
    public String toString() {
        return "BeanOfLifeCycle{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
