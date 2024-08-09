package com.example.springbootstudy.RateLimiting;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 漏桶算法
 * <p>
 * 漏桶算法思路很简单，我们把水比作是请求，漏桶比作是系统处理能力极限，水先进入到漏桶里，
 * 漏桶里的水按一定速率流出，当流出的速率小于流入的速率时，由于漏桶容量有限，后续进入的水直接溢出（拒绝请求），以此实现限流。
 *
 * @author MCW 2023/8/18
 */
public class LeakBucket {
    /**
     * 水桶大小
     */
    private final int bucket;

    /**
     * QPS 水流出的速度
     */
    private int qps;

    /**
     * 当前水量
     */
    private long water;

    private long timeStamp = System.currentTimeMillis();

    public LeakBucket(int bucket, int qps) {
        this.bucket = bucket;
        this.qps = qps;
    }

    /**
     * 桶是否已经满了
     */
    public boolean tryAcquire() {
        // 1. 计算剩余水量
        long now = System.currentTimeMillis();
        long timeGap = (now - timeStamp) / 1000;
        water = Math.max(0, water - timeGap);
        timeGap = now;

        // 如果未满，放行
        if (water < bucket) {
            water += 1;
            return true;
        }
        return false;
    }


    public static void main(String[] args) throws InterruptedException {
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        ExecutorService singleThread = Executors.newSingleThreadExecutor();

        LeakBucket rateLimiter = new LeakBucket(20, 2);
        // 存储流量的队列
        Queue<Integer> queue = new LinkedList<>();
        // 模拟请求，不确定速率注水
        singleThread.execute(() -> {
            int count = 0;
            while (true) {
                count++;
                boolean flag = rateLimiter.tryAcquire();
                if (flag) {
                    queue.offer(count);
                    System.out.println(count + "-------------流量被放行----------");
                } else {
                    System.out.println(count + "流量被限制");
                }

                try {
                    Thread.sleep((long) (Math.random() * 1000));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // 模拟处理请求，固定速率漏水
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            if (!queue.isEmpty()) {
                System.out.println(queue.poll() + "");
            }
        }, 0, 100, TimeUnit.MILLISECONDS);

        // 保证主线程不会退出
        while (true) {
            Thread.sleep(10000);
        }
    }


    // 使用Google封装的令牌桶RateLimiter

    /**
     * 代码中限制 QPS 为 2，也就是每隔 500ms 生成一个令牌，但是程序每隔 250ms 获取一次令牌，所以两次获取中只有一次会成功。
     */
    // public static void main(String[] args) throws InterruptedException {
    //     RateLimiter rateLimiter = RateLimiter.create(2);
    //
    //     for (int i = 0; i < 10; i++) {
    //         String time = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME);
    //         System.out.println(time + ":" + rateLimiter.tryAcquire());
    //         Thread.sleep(250);
    //     }
    // }
}
