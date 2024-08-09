package com.example.springbootstudy.redis.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.connection.stream.StreamInfo;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author MCW 2024/4/7
 */
@Slf4j
@Component
public class ReportReadMqListener implements StreamListener<String, MapRecord<String, String, String>> {

    public static RedisStreamUtil redisStreamUtil;


    @Override
    public void onMessage(MapRecord<String, String, String> message) {
        String stramKey = message.getStream();
        RecordId recordId = message.getId();
        Map<String, String> msg = message.getValue();

        // 处理数据

        log.info("[streamKey] = " + stramKey + ", [recordId]= " + recordId + ", [msg] = " + msg);

        // 逻辑处理完成后，ack消息，删除消息，group 为消费组名称
        StreamInfo.XInfoGroups xInfoGroups = redisStreamUtil.queryGroup(stramKey);
        xInfoGroups.forEach(xInfoGroup -> redisStreamUtil.ack(stramKey, xInfoGroup.groupName(), recordId.getValue()));
        redisStreamUtil.del(stramKey, recordId.getValue());
    }
}
