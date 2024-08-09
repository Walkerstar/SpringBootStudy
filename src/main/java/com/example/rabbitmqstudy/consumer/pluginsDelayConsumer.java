package com.example.rabbitmqstudy.consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;


/**
 * @author MCW 2023/4/3
 */
@Slf4j
@Component
public class pluginsDelayConsumer {


    @RabbitHandler
    @RabbitListener(queues ="delayQueue")
    public void receive(Map map, Channel channel, Message message) throws IOException {
        log.info("delayQueue 接收到的消息是：{}", map.toString());
        //设置了手动应答
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}
