package com.example.springbootstudy.RateLimiting;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 计数器限流(固定窗口)
 * 「原理：」
 * <p>
 * 时间线划分为多个独立且固定大小窗口；
 * 落在每一个时间窗口内的请求就将计数器加1；
 * 如果计数器超过了限流阈值，则后续落在该窗口的请求都会被拒绝。但时间达到下一个时间窗口时，计数器会被重置为0。
 *
 * @author MCW 2023/8/18
 */
public class FixedWindow {

    /**
     * 阈值
     */
    private static Integer QPS = 2;

    /**
     * 时间窗口（毫秒）
     */
    private static long TIME_WINDOWS = 1000;

    /**
     * 计数器
     */
    private static AtomicInteger REQ_COUNT = new AtomicInteger();

    /**
     * 窗口开始时间
     */
    private static long START_TIME = System.currentTimeMillis();

    public synchronized static boolean tryAcquire() {
        // 超时窗口
        if ((System.currentTimeMillis() - START_TIME) > TIME_WINDOWS) {
            REQ_COUNT.set(0);
            START_TIME = System.currentTimeMillis();
        }
        return REQ_COUNT.incrementAndGet() <= QPS;
    }

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            Thread.sleep(250);
            LocalTime now = LocalTime.now();
            if (!tryAcquire()) {
                System.out.println(now + "被限流");
            } else {
                System.out.println(now + "做点什么");
            }
        }
    }
}
