package com.example.rabbitmqstudy.configuration;

import com.rabbitmq.client.*;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author MCW 2023/3/25
 */
@Configuration
public class FanoutConfig {

    //创建Fanout 类型 的 Exchange
    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange("FanoutExchange", true, false);
    }

    //创建队列A
    @Bean
    public Queue fanoutQueueA() {
        return new Queue("FanoutA", true, false, false);
    }

    //创建队列B
    @Bean
    public Queue fanoutQueueB() {
        return new Queue("FanoutB", true, false, false);
    }

    //创建队列C
    @Bean
    public Queue fanoutQueueC() {
        return new Queue("FanoutC", true, false, false);
    }

    //绑定队列到交换机上
    @Bean
    public Binding bindingA() {
        return BindingBuilder.bind(fanoutQueueA()).to(fanoutExchange());
    }

    @Bean
    public Binding bindingB() {
        return BindingBuilder.bind(fanoutQueueB()).to(fanoutExchange());
    }

    @Bean
    public Binding bindingC() {
        return BindingBuilder.bind(fanoutQueueC()).to(fanoutExchange());
    }

}
