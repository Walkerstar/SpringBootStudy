package com.example.rabbitmqstudy.consumer;

import com.rabbitmq.client.*;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * RabbitListener 不仅可以加在类上，还可以加在方法体上。
 * 如果指定的队列不存在，那么启动时会报错。
 *
 * 所以在消费者端，健壮的写法就是也创建队列和交换机，如果队列和交换机存在，那么就拿来使用，不存在则创建，这样就不会报错。查看consumer2.java
 *
 * 当 @RabbitListener 加在类上时，则每个方法必须要加上 @RabbitHandler，该注解会根据消息的类型来判断 要调用哪个方法
 * @author MCW 2023/3/25
 */
@RabbitListener(queues = "TestDirectQueue")
@Component
public class DirectConsumer {

    @RabbitHandler
    public void process(Map map, Channel channel, Message message) throws IOException {
        System.out.println("消费者接受到的消息是："+map.toString());
        //由于配置设置了手动应答，所以这里要进行一个个手动应答。注意：如果设置了自动应答，这里又进行手动应答，会出现double ack,那么程序会报错
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
    }
}
