package com.example.localCache.manager;

import com.example.localCache.AbstractCacheManager;
import com.example.localCache.CacheNameDomain;
import com.example.localCache.constant.CacheNameEnum;
import com.example.localCache.util.CacheMessageUtil;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author MCW 2024/1/25
 */
@Component
public class SysConfigCacheManager extends AbstractCacheManager {

    private static final Lock LOCK = new ReentrantLock();

    /**
     * KEY : 自定义
     */
    private static ConcurrentMap<String, Object> CACHE;

    @Override
    protected String getCacheInfo() {
        return CacheMessageUtil.toString(CACHE);
    }

    @Override
    protected void loadingCache() {
        LOCK.lock();
        try {
            CACHE = new ConcurrentHashMap<>();
            CACHE.put("key1", "value1");
            CACHE.put("key2", "value2");
            CACHE.put("key3", "value3");
        } finally {
            LOCK.unlock();
        }
    }

    @Override
    protected long getSize() {
        return null == CACHE ? 0 : CACHE.size();
    }


    @Override
    public CacheNameDomain getCacheName() {
        return CacheNameEnum.SYS_CONFIG;
    }
}
