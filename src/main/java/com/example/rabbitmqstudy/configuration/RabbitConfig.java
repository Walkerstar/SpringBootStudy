package com.example.rabbitmqstudy.configuration;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import javax.annotation.Resource;

/**
 * 常用的三个设置如下：
 * 1. 设置手动应答 （ acknowledge-model: manual）
 * 2. 设置生产者消息发送的确认回调机制 ( # 这个配置是保证提供者确保消息推送到交换机中，不管成不成功，都会回调
 * publisher-confirm-type: correlated
 * #保证交换机能把消息推送到队列中
 * publisher-returns: true
 * template :
 * #以下是rabbitmqTemplate 配置
 * mandatory: true )
 * 3. 设置重拾
 *
 * @author MCW 2023/3/19
 */
@Configuration
public class RabbitConfig {

    @Resource
    private ConnectionFactory connectionFactory;

    @Resource
    private RabbitProperties properties;

    // 这里因为使用自动配置的 connectionFactory ,所以把自定义的 connectionFactory 注解掉
    // 存在此名字的 bean 自带的连接工厂会不加载（也就是说 yml 中的rabbitmq 下一级不生效），如果想自定义来区分开，需要更改 bean 名称
    // @Bean
    // public CachingConnectionFactory connectionFactory() throws Exception{
    //     // 创建工厂类
    //     CachingConnectionFactory connectionFactory=new CachingConnectionFactory();
    //     // 用户名，密码
    //     connectionFactory.setUsername("guest");
    //     connectionFactory.setPassword("guest");
    //     // rabbitmq 的地址
    //     connectionFactory.setHost("192.168.190.12");
    //     // rabbitmq端口
    //     connectionFactory.setPort(Integer.parseInt("5672"));
    //     // 设置消发布息后回调
    //     connectionFactory.setPublisherReturns(true);
    //     // 设置发布后确认类型，此处确认类型为交互
    //     connectionFactory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.CORRELATED);
    //     connectionFactory.setCacheMode(CachingConnectionFactory.CacheMode.CHANNEL);
    // 
    //     return connectionFactory;
    // }

    // 存在此名字的 bean 自带的容器工厂会不加载 (yml 中 rabbitmq 下的 listener 下的 simple 配置)，
    //  如果想自定义来区分开，需要改变bean的名字
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory() {
        SimpleRabbitListenerContainerFactory containerFactory = new SimpleRabbitListenerContainerFactory();
        containerFactory.setConnectionFactory(this.connectionFactory);

        // 并发消费者数量
        containerFactory.setConcurrentConsumers(1);
        containerFactory.setMaxConcurrentConsumers(20);

        // 与加载消息数量 QOS
        containerFactory.setPrefetchCount(1);
        // 应答模式（此处手动设置）
        containerFactory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        // 消息序列化方式
        containerFactory.setMessageConverter(new Jackson2JsonMessageConverter());
        // 设置通知调用链 （这里设置的是重试机制的调用链）
        containerFactory.setAdviceChain(RetryInterceptorBuilder
                .stateless()
                .recoverer(new RejectAndDontRequeueRecoverer())
                .build());
        return containerFactory;
    }


    // 存在此名字的 bean 自带的容器工厂会不加载 (yml 中 rabbitmq 下的 template 配置)，
    //  如果想自定义来区分开，需要改变bean的名字
    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        // 默认是jdk序列化
        // 数据转换为json存入消息队列，放边可视化界面查看消息
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        // 设置开启 mandatory ，才能触发回调函数，无论消息推送结果怎么样都将强制调用回调函数
        rabbitTemplate.setMandatory(true);
        // 此处设置重试 template 后，会在生产者发送消息的时候，调用该 template 中调用链
        rabbitTemplate.setRetryTemplate(rabbitRetryTemplate());

        // 以下回调可单独写一个类，参考 MyConfirmCallback
        // 自定义confirmCallback，消息由生产者到达交换机时回调，无论成功失败
        rabbitTemplate.setConfirmCallback(((correlationData, b, s) -> {
            System.out.println("ConfirmCallback   " + "相关数据: " + correlationData);
            System.out.println("ConfirmCallback   " + "确认情况: " + b);
            System.out.println("ConfirmCallback   " + "原因: " + s);
        }));
        // 自定义returnsCallback，消息由交换机到达队列 失败 时回调，成功不执行
        rabbitTemplate.setReturnsCallback(message -> {
            System.out.println("ReturnCallback   " + "消息： " + message.getMessage());
            System.out.println("ReturnCallback   " + "回应码： " + message.getReplyCode());
            System.out.println("ReturnCallback   " + "回应消息： " + message.getReplyText());
            System.out.println("ReturnCallback   " + "交换机： " + message.getExchange());
            System.out.println("ReturnCallback   " + "路由键： " + message.getRoutingKey());
        });

        return rabbitTemplate;
    }

    // 重试的template
    @Bean
    public RetryTemplate rabbitRetryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();
        // 设置监听，调用重试处理过程
        retryTemplate.registerListener(new RetryListener() {
            @Override
            public <T, E extends Throwable> boolean open(RetryContext retryContext, RetryCallback<T, E> retryCallback) {
                // 执行之前调用，（返回 false 时会终止执行）
                return true;
            }

            @Override
            public <T, E extends Throwable> void close(RetryContext retryContext, RetryCallback<T, E> retryCallback, Throwable throwable) {
                // 重试结束的时候调用 （最后一次调用）
                System.out.println("-------------------最后一次调用");
            }

            @Override
            public <T, E extends Throwable> void onError(RetryContext retryContext, RetryCallback<T, E> retryCallback, Throwable throwable) {
                // 异常都会调用
                System.out.println("-----------第 {} 次调用" + retryContext.getRetryCount());
            }
        });
        retryTemplate.setBackOffPolicy(backOffPolicyByProperties());
        retryTemplate.setRetryPolicy(retryPolicyByProperties());
        return retryTemplate;
    }

    @Bean
    public ExponentialBackOffPolicy backOffPolicyByProperties() {
        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        long maxInterval = properties.getListener().getSimple().getRetry().getMaxInterval().getSeconds();
        long initialInterval = properties.getListener().getSimple().getRetry().getInitialInterval().getSeconds();
        double multiplier = properties.getListener().getSimple().getRetry().getMultiplier();
        // 重试间隔
        backOffPolicy.setInitialInterval(initialInterval * 1000);
        // 重试最大问题
        backOffPolicy.setMaxInterval(maxInterval * 1000);
        // 重试间隔乘法策略
        backOffPolicy.setMultiplier(multiplier);
        return backOffPolicy;
    }

    @Bean
    public SimpleRetryPolicy retryPolicyByProperties() {
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        int maxAttempts = properties.getListener().getSimple().getRetry().getMaxAttempts();
        retryPolicy.setMaxAttempts(maxAttempts);
        return retryPolicy;
    }

}
