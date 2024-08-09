package com.example.rabbitmqstudy.configuration;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 思路：
 * <p>
 * 假如一条消息需要延迟 30 分钟执行，我们就设置这条消息的有效期为 30 分钟，同时为这条消息配置死信交换机和死信 routing_key，
 * 并且不为这个消息队列设置消费者，那么 30 分钟后，这条消息由于没有被消费者消费而进入死信队列，
 * 此时我们有一个消费者就在“蹲点”这个死信队列，消息一进入死信队列，就立马被消费了。
 *
 * @author MCW 2023/4/5
 */
@Configuration
public class DlxAndTtlDelayConfig {

    public static final String DLX_EXCHANGE = "dlx_exchange";
    public static final String DLX_TTL_KEY = "dlx_ttl_key";
    public static final String DLX_QUEUE = "dlx_queue";

    /**
     * 声明死信交换机
     */
    @Bean
    public DirectExchange dlxExchange() {
        return ExchangeBuilder.directExchange(DLX_EXCHANGE).durable(true).build();
    }

    /**
     * 声明死信队列
     */
    @Bean
    public Queue dlxQueue() {
        return QueueBuilder.durable(DLX_QUEUE).build();
    }

    /**
     * 绑定死信队列和死信交换机
     */
    @Bean
    public Binding dlxBinding() {
        return BindingBuilder.bind(dlxQueue()).to(dlxExchange()).with(DLX_TTL_KEY);
    }

    /**
     * 声明普通交换机
     */
    @Bean
    public DirectExchange mediocreExchange() {
        return ExchangeBuilder.directExchange("mediocreExchange").durable(true).build();
    }

    /**
     * 普通消息队列
     */
    @Bean
    public Queue mediocreQueue() {
        //声明过期时间为10秒 ，消息过期后，需要路由到的 死信交换机为 DLX_EXCHANGE，路由的key是DLX_TTL_KEY
        return QueueBuilder.durable("mediocreQueue")
                .ttl(1000 * 10)
                .deadLetterExchange(DLX_EXCHANGE)
                .deadLetterRoutingKey(DLX_TTL_KEY)
                .build();
    }

    /**
     * 绑定普通交换机和队列
     */
    @Bean
    public Binding mediocreBinding() {
        return BindingBuilder.bind(mediocreQueue()).to(mediocreExchange()).with("delayed");
    }

}
