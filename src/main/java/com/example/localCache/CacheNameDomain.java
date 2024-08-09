package com.example.localCache;

/**
 * 每个处理器都有缓存名字，描述信息，缓存初始化顺序等信息，所以应该定义一个接口，名字为 CacheNameDomain
 *
 * @author MCW 2024/1/25
 */
public interface CacheNameDomain {


    /**
     * 缓存初始化顺序吗，级别越低，越早被初始化
     * <p>
     * 如果缓存的加载存在一定的依赖关系，通过缓存级别控制初始化或者刷新时缓存数据的加载顺序
     * 级别越低，越早被初始化
     * <p>
     * 如果缓存的加载没有依赖关系，可以使用默认顺序<code>Ordered.LOWEST_PRECEDENCE</code>
     *
     * @return 初始化顺序
     * @see org.springframework.core.Ordered
     */
    int getOrder();

    /**
     * 缓存名字，推荐使用英文大写字母表示
     *
     * @return 缓存名称
     */
    String getName();


    /**
     * 缓存描述信息，用于打印日志
     *
     * @return 缓存描述信息
     */
    String getDescription();


}
