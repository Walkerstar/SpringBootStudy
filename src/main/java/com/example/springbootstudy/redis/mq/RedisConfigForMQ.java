package com.example.springbootstudy.redis.mq;

/**
 * @author MCW 2024/4/7
 */

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.Subscription;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;


import javax.annotation.Resource;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Configuration
public class RedisConfigForMQ {

    @Resource
    private RedisMqProperties redisMqProperties;

    @Resource
    private RedisStreamUtil redisStreamUtil;

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);

        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);

        // json 序列化配置
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        jackson2JsonRedisSerializer.setObjectMapper(om);

        // string 序列化配置
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

        // key采用String的序列化方式
        template.setKeySerializer(stringRedisSerializer);
        // value采用jackson的序列化方式
        template.setValueSerializer(jackson2JsonRedisSerializer);

        // hash的key也采用String的序列化方式
        template.setHashKeySerializer(stringRedisSerializer);
        // hash的value也采用 jackson 的序列化方式
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public RedisMessageListenerContainer container(RedisConnectionFactory redisConnectionFactory, RedisMessageListener listener, MessageListenerAdapter adapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        container.addMessageListener(listener, new PatternTopic("topic1"));
        container.addMessageListener(adapter, new PatternTopic("topic2"));

        Jackson2JsonRedisSerializer seria = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        seria.setObjectMapper(objectMapper);
        container.setTopicSerializer(seria);
        return container;
    }

    @Bean
    public MessageListenerAdapter listenerAdapter(PrintMessageReceiver printMessageReceiver) {
        MessageListenerAdapter receiveMessage = new MessageListenerAdapter(printMessageReceiver, "receiveMessage");
        Jackson2JsonRedisSerializer seria = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        seria.setObjectMapper(objectMapper);
        receiveMessage.setSerializer(seria);
        return receiveMessage;
    }


    @Bean
    public List<Subscription> subscriptions(RedisConnectionFactory factory) {
        List<Subscription> resultList = new ArrayList<>();
        AtomicInteger index = new AtomicInteger(1);
        int processors = Runtime.getRuntime().availableProcessors();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(processors, processors, 0, TimeUnit.SECONDS, new LinkedBlockingDeque<>(), r -> {
            Thread thread = new Thread();
            thread.setName("async-stream-consumer-" + index.getAndIncrement());
            thread.setDaemon(true);
            return thread;
        });
        StreamMessageListenerContainer
                .StreamMessageListenerContainerOptions<String,
                MapRecord<String, String, String>> options = StreamMessageListenerContainer
                .StreamMessageListenerContainerOptions
                .builder()
                // 一次做多获取5条
                .batchSize(5)
                .executor(executor)
                .pollTimeout(Duration.ofSeconds(1))
                .errorHandler(throwable -> log.error("[MQ handler exception]" + throwable.getMessage()))
                .build();

        for (RedisMqStream redisMqStream : redisMqProperties.getStreams()) {
            String streamName = redisMqStream.getName();
            RedisMqGroup redisMqGroup = redisMqStream.getGroups().get(0);
            initStream(streamName, redisMqGroup.getName());
            StreamMessgaeListenerContainer listenerContainer = StreamMessgaeLisListenrContainer.create(factory, options);
            // 手动 ack
            Subscription subscription = listenerContainer.receive(Consumer.form(redisMqGroup.getName(), redisMqGroup.getConsumers()[0]));
            // 自动 ack
            // Subscription subscription = listenerContainer.receiveAutoAck(Consumer.form(redisMqGroup.getName(), redisMqGroup.getConsumers()[0],
            //         StreamOffset.create(streamName, ReadOffset.lastConsumed()), new ReportReadMqListener());

            resultList.add(subscription);
            listenerContainer.start();
        }
        ReportReadMqListener.redisMqProperties = redisStreamUtil;
        return resultList;
    }

    private void initStream(String key, String group) {
        boolean hasKey = redisStreamUtil.hasKey(key);
        if (!hasKey) {
            Map<String, Object> map = new HashMap<>();
            map.put("field", "value");
            // 创建主题
            redisStreamUtil.createGroup(key, group);
            // 将初始化的值删掉
            redisStreamUtil.del(key, group);
            log.info("stream:{} - group:{} initialize success", key, group);
        }
    }

}
