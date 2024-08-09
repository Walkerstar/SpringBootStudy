package com.example.springbootstudy.redis.mq;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;


/**
 * @author MCW 2024/4/7
 */
@Data
@Configuration
// @EnableConfigurationProperties
@ConfigurationProperties(prefix = "redis.mq")
public class RedisMqProperties {

    public List<RedisMqStream> streams;

}
