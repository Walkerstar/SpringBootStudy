package com.example.localCache;

import org.springframework.core.Ordered;

/**
 * 每个处理器都有生命周期，如初始化、刷新、获取处理器信息等操作，这应该也是一个接口，处理器都应该声明这个接口，名字为CacheManager
 *
 * @author MCW 2024/1/25
 */
public interface CacheManager extends Ordered {

    /**
     * 初始化缓存
     */
    public void initCache();


    /**
     * 刷新缓存
     */
    public void refreshCache();

    /**
     * 获取缓存名称
     *
     * @return 缓存名称
     */
    public CacheNameDomain getCacheName();

    /**
     * 打印缓存信息
     */
    public void dumpCache();

    /**
     * 获取缓存条数
     *
     * @return 缓存条数
     */
    public long getCacheSize();

}
