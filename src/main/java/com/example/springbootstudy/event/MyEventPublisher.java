package com.example.springbootstudy.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

/**
 * 通过 实现 ApplicationEventPublisherAware 接口 来自定义 事件发布
 *
 * @author MCW 2023/6/26
 */
@Component
public class MyEventPublisher implements ApplicationEventPublisherAware {
    ApplicationEventPublisher applicationEventPublisher;

    /**
     * 被自动调用，把真正发事件的底层组件注入进来
     * org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext
     *
     * @param applicationEventPublisher
     */
    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void sendEvent(ApplicationEvent event) {
        applicationEventPublisher.publishEvent(event);
    }

}
