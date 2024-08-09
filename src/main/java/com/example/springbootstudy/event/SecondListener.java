package com.example.springbootstudy.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * 事件监听方法之二：
 * 在自定义方法上 使用 @EventListener 注解，方法参数 写入 要监听的事件，便可监听该事件
 *
 * @author MCW 2023/6/26
 */
@Service
public class SecondListener {

    @EventListener
    public void watchEvent(OneEvent event) {
        System.out.println("使用注解感知事件并处理自定义事件，" + event.getSource());
    }
}
