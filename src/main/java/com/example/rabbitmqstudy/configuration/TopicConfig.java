package com.example.rabbitmqstudy.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author MCW 2023/3/25
 */
@Configuration
public class TopicConfig {

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange("TopicExchange", true, false);
    }

    @Bean
    public Queue topQueueA() {
        return new Queue("TopicA", true, false, false);
    }

    @Bean
    public Queue topQueueB() {
        return new Queue("TopicB", true, false, false);
    }

    @Bean
    public Queue topQueueC() {
        return new Queue("TopicC", true, false, false);
    }

    @Bean
    public Binding TopicBindA() {
        return BindingBuilder.bind(topQueueA()).to(topicExchange()).with("test.#");
    }

    @Bean
    public Binding TopicBindB() {
        return BindingBuilder.bind(topQueueB()).to(topicExchange()).with("test.*");
    }

    @Bean
    public Binding TopicBindC() {
        return BindingBuilder.bind(topQueueC()).to(topicExchange()).with("test.topic");
    }
}
