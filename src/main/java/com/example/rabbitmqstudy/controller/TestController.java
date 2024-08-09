package com.example.rabbitmqstudy.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author MCW 2023/3/19
 */
@Slf4j
@RestController
public class TestController {

    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送消息到 direct 类型的交换机
     */
    @GetMapping("/sendMessage")
    public String sendDirectMessage() throws Exception {
        String messageId = UUID.randomUUID().toString();
        String messageData = "test message for direct, hello!";
        String current = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        Map<String, Object> map = new HashMap<>();
        map.put("messageId", messageId);
        map.put("data", messageData);
        map.put("current", current);
        rabbitTemplate.convertAndSend("TestDirectExchange", "123", map, new CorrelationData(UUID.randomUUID().toString()));
        return "ok";
    }

    /**
     * 发送消息到 fanout 类型的交换机
     */
    @GetMapping("/sendMessage2")
    public String sendFanoutMessage() {
        String messageId = UUID.randomUUID().toString();
        String messageData = "test message for fanout, hello!";
        String current = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        Map<String, Object> map = new HashMap<>();
        map.put("messageId", messageId);
        map.put("data", messageData);
        map.put("current", current);
        rabbitTemplate.convertAndSend("FanoutExchange", "", map, new CorrelationData(UUID.randomUUID().toString()));
        return "ok";
    }

    /**
     * 发送消息到 topic 类型的交换机
     */
    @GetMapping("/sendMessage3")
    public String sendTopicMessage() {
        String messageId = UUID.randomUUID().toString();
        String messageData = "test message for topic, hello!";
        String current = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        Map<String, Object> map = new HashMap<>();
        map.put("messageId", messageId);
        map.put("data", messageData);
        map.put("current", current);
        rabbitTemplate.convertAndSend("TopicExchange", "test.topic.a", map, new CorrelationData(UUID.randomUUID().toString()));
        return "ok";
    }

    /**
     * 发送消息到 delay 类型的交换机,使用插件
     */
    @GetMapping("/sendMessage4")
    public String sendDelayMessage() {
        String messageId = UUID.randomUUID().toString();
        String messageData = "test message for delay, hello!";
        String current = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        Map<String, Object> map = new HashMap<>();
        map.put("messageId", messageId);
        map.put("data", messageData);
        map.put("current", current);
        rabbitTemplate.convertAndSend("delayExchange", "test_delay", map, message -> {
            message.getMessageProperties().setHeader("x-delay", 1000 * 60);
            return message;
        }, new CorrelationData(UUID.randomUUID().toString()));
        return "ok";
    }

    /**
     * 发送消息到 delay 类型的交换机,使用过期时间和死信队列
     */
    @GetMapping("/sendMessage5")
    public String sendDelayMessageWithDLX() {
        log.info("使用过期时间和死信队列 开始发送 延迟 消息");
        String messageId = UUID.randomUUID().toString();
        String messageData = "test message for delay with DLX and TTL, hello!";
        rabbitTemplate.convertAndSend("mediocreExchange", "delayed", messageData,new CorrelationData(UUID.randomUUID().toString()));
        return "ok";
    }
}
