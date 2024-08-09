package com.example.localCache.support;

import java.util.Observable;
import java.util.Observer;

/**
 * @author MCW 2024/1/25
 */
public abstract class AbstractLazyCacheSupport extends Observable {

    /**
     * 缓存管理 观察者
     */
    protected Observer cacheManagerObserver;

    /**
     * 是否已经初始化
     *
     * @return 是否已经初始化
     */
    protected abstract boolean alreadyInitCache();


    protected void lazyInitIfNeed() {
        if (alreadyInitCache()) {
            return;
        }
        // 单点代码显示锁
        synchronized (AbstractLazyCacheSupport.class) {
            // 再次检测是否已经初始化
            if (alreadyInitCache()) {
                return;
            }
            this.addObserver(cacheManagerObserver);
            this.setChanged();
            this.notifyObservers();
        }
    }

    public void setCacheManagerObserver(Observer cacheManagerObserver) {
        this.cacheManagerObserver = cacheManagerObserver;
    }
}
