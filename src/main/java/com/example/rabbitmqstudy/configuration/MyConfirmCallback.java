package com.example.rabbitmqstudy.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author MCW 2023/10/7
 */
@Slf4j
@Component
public class MyConfirmCallback implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback {

    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * @PostConstruct 用于在依赖关系注入完成之后需要执行的方法上，以执行任何初始化
     */
    @PostConstruct
    public void init() {
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnsCallback(this);
    }

    /**
     * 确认消息是否成功到达交换机中，不管是否到达交换机，该回调都会执行
     * 生产者 ——> 交换机
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        log.info("发布消息成功的UUID为：{}", correlationData.getId());
        if (ack) {
            log.info("消息发布成功！");
        } else {
            log.info("消息发布失败！ 失败原因是：{}", cause);
        }
    }

    /**
     * 确认消息是否从交换机成功到达队列中，失败将会执行，成功则不执行
     * 交换机 ——> 队列
     */
    @Override
    public void returnedMessage(ReturnedMessage returned) {
        log.info("ReturnsCallback 回调内容：{}", returned);
    }
}
