package com.example.springbootstudy.event;

import org.springframework.context.ApplicationEvent;

import java.time.Clock;

/**
 * 通过 继承 ApplicationEvent 抽象类 来自定义 事件
 *
 * @author MCW 2023/6/26
 */
public class OneEvent extends ApplicationEvent {

    public OneEvent(Object source) {
        super(source);
    }

    public OneEvent(Object source, Clock clock) {
        super(source, clock);
    }
}
