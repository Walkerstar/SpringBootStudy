package com.example.springbootstudy.RateLimiting;

import lombok.Data;

import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 滑动窗口
 * 「原理：」
 * <p>
 * 将单位时间划分为多个区间，一般都是均分为多个小的时间段；
 * <p>
 * 每一个区间内都有一个计数器，有一个请求落在该区间内，则该区间内的计数器就会加一；
 * <p>
 * 每过一个时间段，时间窗口就会往右滑动一格，抛弃最老的一个区间，并纳入新的一个区间；
 * <p>
 * 计算整个时间窗口内的请求总数时会累加所有的时间片段内的计数器，计数总和超过了限制数量，则本窗口内所有的请求都被丢弃。
 *
 * @author MCW 2023/8/18
 */
public class SlidingWindow {

    /**
     * 阈值
     */
    private int qps = 2;

    /**
     * 时间窗口总大小（毫秒）
     */
    private long windowSize = 1000;

    /**
     * 多少个子窗口
     */
    private Integer windowCount = 10;

    /**
     * 窗口显示列表
     */
    private WindowInfo[] windowArray = new WindowInfo[windowCount];

    public SlidingWindow(int qps) {
        this.qps = qps;
        long currentTimeMillis = System.currentTimeMillis();
        for (int i = 0; i < windowArray.length; i++) {
            windowArray[i] = new WindowInfo(currentTimeMillis, new AtomicInteger(0));
        }
    }

    /**
     * 1. 计算当前时间窗口
     * 2. 更新当前窗口计数 & 重置过期窗口计数
     * 3. 当前QPS是否超过限制
     */
    public synchronized boolean tryAcquire() {
        long currentTimeMillis = System.currentTimeMillis();
        // 1. 计算当前时间窗口
        int currentIndex = (int) (currentTimeMillis % windowSize / (windowSize / windowCount));
        // 2. 更新当前窗口计数 & 重置过期窗口计数
        int sum = 0;
        for (int i = 0; i < windowArray.length; i++) {
            WindowInfo windowInfo = windowArray[i];
            if ((currentTimeMillis - windowInfo.getTime()) > windowSize) {
                windowInfo.getNumber().set(0);
                windowInfo.setTime(currentTimeMillis);
            }
            if (currentIndex == i && windowInfo.getNumber().get() < qps) {
                windowInfo.getNumber().incrementAndGet();
            }
            sum = sum + windowInfo.getNumber().get();
        }
        // 3.当前QPS是否超过限制
        return sum <= qps;
    }


    @Data
    private class WindowInfo {
        /**
         * 窗口开始时间
         */
        private Long time;

        /**
         * 计数器
         */
        private AtomicInteger number;

        public WindowInfo(long time, AtomicInteger number) {
            this.time = time;
            this.number = number;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int qps = 2, count = 20, sleep = 300, success = count * sleep / 1000 * qps;
        System.out.println(String.format(" 当前QPS限制为： %d，当前测试次数： %d,间隔：%dms,预计成功次数:%d", qps, count, sleep, success));
        success = 0;
        SlidingWindow myRateLimiter = new SlidingWindow(qps);
        for (int i = 0; i < count; i++) {
            Thread.sleep(sleep);
            if (myRateLimiter.tryAcquire()) {
                success++;
                if (success % qps == 0) {
                    System.out.println(LocalTime.now() + ": success");
                } else {
                    System.out.println(LocalTime.now() + ": success");
                }
            } else {
                System.out.println(LocalTime.now() + ": fail");
            }
        }
        System.out.println();
        System.out.println("实际测试成功次数：" + success);
    }
}
