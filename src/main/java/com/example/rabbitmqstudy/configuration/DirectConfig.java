package com.example.rabbitmqstudy.configuration;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @author MCW 2023/3/19
 */
@Configuration
public class DirectConfig {

    //创建一个名为directQueue 的队列
    @Bean
    public Queue directQueue() {
        // durable : 是否持久化，默认是false, 持久化队列: 会被存储在磁盘上，当消息代理重启仍然存在；暂存队列：当前有效连接
        // exclusive : 默认也是 false,只能被当前创建的连接使用，而且当连接关闭后队列即被删除。此参考优先级高于 durable
        // autoDelete : 是否自动删除，有消息订阅本队列，然后说有消费者都解除订阅此队列，会自动删除
        // arguments : 队列携带的参数，比如设置队列的死信队列，消息的过期时间等等
        return new Queue("TestDirectQueue", true);
    }

    //创建一个名为 directExchange 的 Direct 类型的交换机
    @Bean
    public DirectExchange directExchange() {
        // durable : 是否持久化，默认是 false ，持久化交换机
        // autoDelete : 是否自动删除，交换机现有队列或者其他交换机绑定的时候，然后当该交换机没有队列或其他交换机绑定的时候，会自动删除
        // arguments : 交换机设置的参数，比如设置交换机的备用交换机 ( Alternate  Exchange)，当然消息不能被路由到该交换机绑定的队列时，会自动路由到备用交换机
        DirectExchange directExchange = new DirectExchange("TestDirectExchange", true, false);
        //设置备份交换机
        directExchange.addArgument("alternate-exchange","backExchange");
        return directExchange;
    }

    //创建备份交换机
    @Bean
    public FanoutExchange backExchange() {
        return ExchangeBuilder.fanoutExchange("backExchange").durable(true).build();
    }

    //备份交换机需要绑定的队列
    @Bean
    public Queue warningQueue() {
        return QueueBuilder.durable().build();
    }

    //绑定备份交换机和备份队列
    @Bean
    public Binding warningQueueToBackExchange() {
        return BindingBuilder.bind(warningQueue()).to(backExchange());
    }
}
