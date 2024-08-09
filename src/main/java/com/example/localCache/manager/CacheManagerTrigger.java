package com.example.localCache.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;

/**
 * 定时、并按Order顺序刷新缓存
 *
 * @author MCW 2024/1/25
 */
@Component
public class CacheManagerTrigger {

    private static final Logger logger = LoggerFactory.getLogger(CacheManagerTrigger.class);

    /**
     * 触发刷新缓存
     */
    @Scheduled(fixedDelay = 1000 * 60, initialDelay = 1000 * 60)
    private static void refreshCache() {
        List<String> cacheNameList = getCacheList();
        logger.info("start refresh instruction,cacheNameList={}", cacheNameList);
        if (CollectionUtils.isEmpty(cacheNameList)) {
            logger.warn("cache name list are empty");
            return;
        }

        long totalCacheSize = 0;
        for (String cacheName : cacheNameList) {
            CacheManagerRegistry.refreshCache(cacheName);
            totalCacheSize += CacheManagerRegistry.getCacheSize(cacheName);

            logger.info(MessageFormat.format("刷新缓存成功，缓存管理器：{0}，总缓存条目数量：{1}条", cacheNameList.size(), totalCacheSize));
        }
    }

    public static List<String> getCacheList() {
        return CacheManagerRegistry.getCacheNameList();
    }

}
