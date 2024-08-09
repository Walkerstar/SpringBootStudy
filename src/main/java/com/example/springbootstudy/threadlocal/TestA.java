package com.example.springbootstudy.threadlocal;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author MCW 2024/2/22
 */
public class TestA {

    // private static ThreadLocal<String> localStr = new ThreadLocal<>();
    private static ThreadLocal<String> localStr = new DelegatingThreadLocal<>();

    // 线程池初始化 （初始化的执行线程不能是线程A）
    // ExecutorService executorService = Executors.newFixedThreadPool(2);
    ExecutorService executorService = new DelegatingExecutorService(Executors.newFixedThreadPool(2));

    // 线程A 调用 test() 方法
    public void test() {

        // 线程A 往 “公共内存” 中写入 “新年快了”
        // 共享内存 ： ThreadLocal<Map<DelegatingThreadLocal<Object>, Object>> holder
        localStr.set("新年快乐");

        // 2. 线程A创建出DelegatingRunnable 对象
        // 将自己 ThreadLocal 中的值 copy 给待创建的 DelegatingRunnable 对象中的成员变量
        // 3. 线程A 将 DelegatingRunnable 对象提交给线程池
        executorService.submit(() -> {
            // 4. 线程1在执行 run 方法之前，将 DelegatingRunnable 对象中的成员变量 copy 回自己的 ThreadLocal 中
            // 5. 线程1执行 run 方法，从自己的 ThreadLocal 中取出 “新年快乐”
            // 线程1执行
            System.out.println("我是线程1：" + localStr.get());
        });

        executorService.submit(() -> {
            // 4.  线程2在执行 run 方法之前，将 DelegatingRunnable 对象中的成员变量 copy 回自己的 ThreadLocal 中
            // 5. 线程2执行 run 方法，从自己的 ThreadLocal 中取出 “新年快乐”
            // 线程2执行
            System.out.println("我是线程2：" + localStr.get());
        });

    }

    public static void main(String[] args) {
        new TestA().test();
    }
}
