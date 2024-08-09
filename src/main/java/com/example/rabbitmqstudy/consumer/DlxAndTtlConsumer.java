package com.example.rabbitmqstudy.consumer;

import com.example.rabbitmqstudy.configuration.DlxAndTtlDelayConfig;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * @author MCW 2023/4/5
 */
@Slf4j
@Component
public class DlxAndTtlConsumer {

    @RabbitListener(queues = DlxAndTtlDelayConfig.DLX_QUEUE)
    public void receive(Channel channel, Message message) throws IOException {

        String s = new String(message.getBody(), StandardCharsets.UTF_8);
        log.info("使用死信队列接收到的消息是:{}", s);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

}
