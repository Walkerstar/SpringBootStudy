package com.example.springbootstudy.redis.mq;

import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.StreamInfo;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author MCW 2024/4/7
 */
@Component
public class RedisStreamUtil {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    public String createGroup(String key, String group) {
        return redisTemplate.opsForStream().createGroup(key, group);
    }

    public StreamInfo.XInfoConsumers queryConsumers(String key, String group) {
        return redisTemplate.opsForStream().consumers(key, group);
    }

    public StreamInfo.XInfoGroups queryGroup(String key) {
        return redisTemplate.opsForStream().groups(key);
    }

    public String addMap(String key, Map<String, Object> value) {
        return redisTemplate.opsForStream().add(key, value).getValue();
    }

    public List<MapRecord<String, Object, Object>> read(String key) {
        return redisTemplate.opsForStream().read(StreamOffset.fromStart(key));
    }

    public Long ack(String key, String group,String... recordIds) {
        return redisTemplate.opsForStream().acknowledge(key, group, recordIds);
    }
    public Long del(String key,String... recordIds){
        return redisTemplate.opsForStream().delete(key,recordIds);
    }

    public boolean hasKey(String key){
        Boolean aBoolean=redisTemplate.hasKey(key);
        return aBoolean != null && aBoolean;
    }

}
