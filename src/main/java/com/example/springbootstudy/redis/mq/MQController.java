package com.example.springbootstudy.redis.mq;

/**
 * @author MCW 2024/4/7
 */

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import java.util.HashMap;

@Controller
public class MQController {

    @Resource
    private RedisStreamUtil redisStreamUtil;


    @GetMapping("/testSttream")
    public String testStream() {
        HashMap<String, Object> message = new HashMap<>(2);
        message.put("body", "消息主题");
        message.put("sendTime", "消息发送时间");
        String streamKey = "redis:mq:streams:key2";
        redisStreamUtil.addMap(streamKey, message);
        return "testStream";

    }


}
