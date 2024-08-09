package com.example.springbootstudy.threadlocal;

import com.baomidou.mybatisplus.extension.api.R;

import java.util.Map;

/**
 * @author MCW 2024/2/22
 */
public class DelegatingRunnable implements Runnable {

    private final Map<DelegatingThreadLocal<Object>, Object> holder;
    private final Runnable runnable;


    // 1. 主线程获取线程，并传递给子线程
    public DelegatingRunnable(Runnable runnable) {
        this.holder = DelegatingThreadLocal.copyForm();
        this.runnable = runnable;
    }


    @Override
    public void run() {
        // 2. 将主线程的共享变量复制到子线程
        DelegatingThreadLocal.copyTo(holder);
        // 3. 子线程执行业务逻辑
        runnable.run();
    }
}
