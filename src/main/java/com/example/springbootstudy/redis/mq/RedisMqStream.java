package com.example.springbootstudy.redis.mq;

import lombok.Data;

import java.util.List;

/**
 * @author MCW 2024/4/7
 */
@Data
public class RedisMqStream {
    // 队列
    public String name;
    // 消费者组
    public List<RedisMqGroup> groups;
}
