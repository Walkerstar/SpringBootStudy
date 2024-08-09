package com.example.rabbitmqstudy.consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * @author MCW 2023/4/5
 */
@Component
public class TopicConsumer {
    public static final Logger logger = LoggerFactory.getLogger(TopicConsumer.class);

    @RabbitListener(queues = "TopicA")
    public void receiveA(Map map, Channel channel, Message message) throws IOException {
        logger.info("TopicA 接收到的消息是：{}", map.toString());
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

    @RabbitListener(queues = "TopicB")
    public void receiveB(Map map, Channel channel, Message message) throws IOException {
        logger.info("TopicB 接收到的消息是：{}", map.toString());
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

    @RabbitListener(queues = "TopicC")
    public void receiveC(Map map, Channel channel, Message message) throws IOException {
        logger.info("TopicC 接收到的消息是：{}", map.toString());
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}
