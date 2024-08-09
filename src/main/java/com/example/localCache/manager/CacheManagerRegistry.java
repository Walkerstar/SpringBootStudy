package com.example.localCache.manager;

import com.example.localCache.CacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.OrderComparator;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 当有很多缓存处理器的时候，那么需要一个统一注册、统一管理的的地方，可以实现对分散在各处的缓存管理器统一维护，
 * 名字为CacheManagerRegistry
 * <p>
 * 缓存管理器集中注册接口，可以实现对分散在各处的缓存管理器统一维护
 *
 * @author MCW 2024/1/25
 */
@Component
public class CacheManagerRegistry implements InitializingBean {


    private static final Logger logger = LoggerFactory.getLogger(CacheManagerRegistry.class);


    /**
     * 缓存管理器
     */
    private static Map<String, CacheManager> managerMap = new ConcurrentHashMap<>();

    public static void register(CacheManager cacheManager) {
        String cacheName = resolveCacheName(cacheManager.getCacheName().getName());
        managerMap.put(cacheName, cacheManager);
    }


    /**
     * 刷新特定的缓存
     *
     * @param cacheName 名称
     */
    public static void refreshCache(String cacheName) {
        CacheManager cacheManager = managerMap.get(resolveCacheName(cacheName));
        if (cacheManager == null) {
            logger.warn("cache manager is not exist,cacheName={}", cacheName);
            return;
        }

        cacheManager.refreshCache();
        cacheManager.dumpCache();
    }

    /**
     * 获取缓存总条数
     */
    public static long getCacheSize(String cacheName) {
        CacheManager cacheManager = managerMap.get(resolveCacheName(cacheName));
        if (cacheManager == null) {
            logger.warn("cache manager is not exist,cacheName={}", cacheName);
            return 0;
        }
        return cacheManager.getCacheSize();
    }

    /**
     * 获取缓存列表
     */
    public static List<String> getCacheNameList() {
        List<String> cacheNameList = new ArrayList<>();
        managerMap.forEach((k, v) -> {
            cacheNameList.add(k);
        });
        return cacheNameList;
    }


    public void startup() {
        try {
            deployCompletion();
        } catch (Exception e) {
            logger.error("Cache Component Init Fail:", e);
            // 系统启动时出现异常，不希望启动应用
            throw new RuntimeException("启动加载失败", e);
        }
    }


    /**
     * 部署完成，执行缓存初始化
     */
    private void deployCompletion() {
        List<CacheManager> managers = new ArrayList<>(managerMap.values());

        // 根据缓存级别进行排序，以此顺序进行缓存的初始化
        Collections.sort(managers, new OrderComparator());

        // 打印系统启动日志
        logger.info("cache manager component extensions:");
        for (CacheManager cacheManager : managers) {
            String beanName = cacheManager.getClass().getSimpleName();
            logger.info(cacheManager.getCacheName().getName(), "==>", beanName);
        }

        // 初始化缓存
        for (CacheManager cacheManager : managers) {
            cacheManager.initCache();
            cacheManager.dumpCache();
        }
    }

    /**
     * 解析缓存名称，大小写不敏感，增强刷新的容错能力
     *
     * @param cacheName 缓存名称
     * @return 转换大小写的缓存名称
     */
    private static String resolveCacheName(String cacheName) {
        return cacheName.toUpperCase();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        startup();
    }
}
