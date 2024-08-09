package com.example.rabbitmqstudy.consumer;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * @author MCW 2023/3/25
 */
@Component
public class DirectConsumer2 {

    /**
     * 本例在@RabbitmqListener中声明了队列和交换机，并且指定了routing Key ，当这一对关系存在时，那么会直接使用，不存在就会创建。
     */
    @RabbitHandler
    @RabbitListener(bindings = {
            @QueueBinding(value = @Queue(value = "q5", durable = "true"), // 如果括号中不指定队列的名称，那么这时候创建的就是临时队列，当消费者连接断开的时候，该队列就会消失
                    exchange = @Exchange(value = "myExchange", durable = "true", type = "direct"),
                    key = "123")
    })
    public void process(Map map, Channel channel, Message message) throws IOException {
        System.out.println("消费者接收到的消息是：" + map.toString());
        //由于配置设置了手动应答，所以这里要进行一个个手动应答。注意：如果设置了自动应答，这里又进行手动应答，会出现double ack,那么程序会报错
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}
