package com.example.springbootstudy.event;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 监听事件的方法之一：
 * 实现 ApplicationListener 接口，通过 重写 onApplicationEvent 方法，来监听事件，并自定义操作
 *
 * @author MCW 2023/6/26
 */
@Service
public class OneListener implements ApplicationListener<OneEvent> {

    static Map<String, Integer> count = new HashMap<>();

    @Override
    public void onApplicationEvent(OneEvent event) {
        System.out.println("============ 实现 ApplicationListener 接口感知到指定事件，" + event.getSource());
        System.out.println("============ 开始调用相关自定义方法===============");
        add(event);
    }

    public void add(OneEvent event) {
        Object source = event.getSource();
        count.putIfAbsent((String) source, count.getOrDefault(source, 0) + 1);
        System.out.println("=========== 事件出现次数：" + count.get(source));
    }
}
