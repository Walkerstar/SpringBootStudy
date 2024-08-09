package com.example.localCache;

import com.example.localCache.manager.CacheManagerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * 定义一个缓存处理器生命周期的处理器，会声明 CacheManager，做第一次的处理，也是所有处理器的父类，
 * 所以这应该是一个抽象类，名字为 AbstractCacheManager
 *
 * @author MCW 2024/1/25
 */
public abstract class AbstractCacheManager implements CacheManager, InitializingBean {

    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractCacheManager.class);

    /**
     * 获取可读性好的缓存信息，用于日志打印操作
     *
     * @return 缓存信息
     */
    protected abstract String getCacheInfo();

    /**
     * 查询数据仓库，并加载到缓存数据
     */
    protected abstract void loadingCache();

    /**
     * 查询缓存大小
     *
     * @return 缓存大小
     */
    protected abstract long getSize();

    /**
     * @see InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        CacheManagerRegistry.register(this);
    }

    @Override
    public void initCache() {
        String description = getCacheName().getDescription();
        LOGGER.info("start init{}", description);

        loadingCache();

        afterRefreshCache();

        LOGGER.info("{} end refresh", description);
    }

    @Override
    public void refreshCache() {

        String description = getCacheName().getDescription();

        LOGGER.info("start refresh {}", description);

        loadingCache();

        afterRefreshCache();

        LOGGER.info("{} end refresh", description);
    }


    @Override
    public int getOrder() {
        return getCacheName().getOrder();
    }

    @Override
    public void dumpCache() {
        String description = getCacheName().getDescription();
        LOGGER.info("start print {} {}{}", description, "\n", getCacheInfo());
        LOGGER.info("{} end print", description);
    }

    @Override
    public long getCacheSize() {
        LOGGER.info("Cache Size Count: {} ", getSize());
        return getSize();
    }


    /**
     * 刷新之后，其他业务处理，比如监听器的注册
     */
    protected void afterInitCache() {
        // 有需要后续动作的缓存实现
    }


    /**
     * 刷新之后，其他业务处理，比如缓存变通通知
     */
    protected void afterRefreshCache() {
        // 有需要后续动作的缓存实现
    }
}
