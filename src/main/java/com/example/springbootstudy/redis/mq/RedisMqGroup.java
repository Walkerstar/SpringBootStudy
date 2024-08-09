package com.example.springbootstudy.redis.mq;

import lombok.Data;

/**
 * @author MCW 2024/4/7
 */
@Data
public class RedisMqGroup {
    // 消费者组名
    private String name;
    // 消费者
    private String[] consumers;
}
