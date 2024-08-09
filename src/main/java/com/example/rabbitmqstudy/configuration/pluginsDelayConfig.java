package com.example.rabbitmqstudy.configuration;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 测试延迟队列
 * 使用 rabbitmq_delayed_message_exchange 插件来实现
 * <p>
 * 插件使用步骤：
 * 1. 官网下载该插件—— .ez文件
 * 2. 将该插件移动到 rabbitmq 的 plugins目录下, 如使用docker，则命令如下：docker cp 插件文件地址 容器名:/plugins
 * 3. 在容器内启动插件，命令： rabbitmq-plugins enable rabbitmq_delayed_message_exchange；然后可通过 rabbitmq-plugins list 查看插件是否运行
 *
 * @author MCW 2023/4/3
 */
@Configuration
public class pluginsDelayConfig {

    /**
     * 声明延迟类型的交换机
     */
    @Bean
    public CustomExchange pluginsDelayExchange() {
        Map<String, Object> map = new HashMap<>();
        map.put("x-delayed-type", "direct");
        return new CustomExchange("delayExchange", "x-delayed-message", true, false, map);
    }

    /**
     * 声明一个队列
     */
    @Bean
    public Queue pluginsDelayQueue() {
        return new Queue("delayQueue", true);
    }

    /**
     * 绑定 延迟交换机和队列
     */
    @Bean
    public Binding pluginsBinding() {
        return BindingBuilder.bind(pluginsDelayQueue()).to(pluginsDelayExchange()).with("test_delay").noargs();
    }
}
