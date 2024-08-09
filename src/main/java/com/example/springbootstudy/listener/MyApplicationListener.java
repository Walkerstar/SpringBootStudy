package com.example.springbootstudy.listener;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

/**
 *  ApplicationListener 感知全阶段 ： 基于事件机制，感知事，一旦到达了哪个阶段，就可以做些别的事
 * @author MCW 2022/8/25
 */
public class MyApplicationListener implements ApplicationListener<ApplicationEvent> {
    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        System.out.println("MyApplicationListener..................onApplicationEvent……………… 事件到达" + event);
    }
}
