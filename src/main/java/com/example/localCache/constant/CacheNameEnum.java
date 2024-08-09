package com.example.localCache.constant;

import com.example.localCache.CacheNameDomain;
import org.springframework.core.Ordered;

/**
 * 缓存枚举
 *
 * @author MCW 2024/1/25
 */
public enum CacheNameEnum implements CacheNameDomain {

    /**
     * 系统配置缓存
     */
    SYS_CONFIG("SYS_CONFIG", "系统配置缓存", Ordered.LOWEST_PRECEDENCE),
    ;

    private String name;
    private String description;
    private int order;

    CacheNameEnum(String name, String description, int order) {
        this.name = name;
        this.description = description;
        this.order = order;
    }


    @Override
    public int getOrder() {
        return order;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
