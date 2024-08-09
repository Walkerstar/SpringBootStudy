package com.example.rabbitmqstudy.consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * @author MCW 2023/3/25
 */
@Slf4j
@Component
public class FanoutConsumer {

    @RabbitHandler
    @RabbitListener(bindings = {@QueueBinding(
            value = @Queue(value = "FanoutA", durable = "true"),
            exchange = @Exchange(value = "FanoutExchange", durable = "true", type = "fanout"),
            key = ""
    )})
    public void processA(Map map, Channel channel, Message message) throws IOException {
        log.info("收到的FanoutA队列的消息是：{}", map.toString());
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

    @RabbitHandler
    @RabbitListener(bindings = {@QueueBinding(
            value = @Queue(value = "FanoutB", durable = "true"),
            exchange = @Exchange(value = "FanoutExchange", durable = "true", type = "fanout"),
            key = ""
    )})
    public void processB(Map map, Channel channel, Message message) throws IOException {
        log.info("收到的FanoutB队列的消息是：{}", map.toString());
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

    @RabbitHandler
    @RabbitListener(bindings = {@QueueBinding(
            value = @Queue(value = "FanoutC", durable = "true"),
            exchange = @Exchange(value = "FanoutExchange", durable = "true", type = "fanout"),
            key = ""
    )})
    public void processC(Map map, Channel channel, Message message) throws IOException {
        log.info("收到的FanoutC队列的消息是：{}", map.toString());
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}
