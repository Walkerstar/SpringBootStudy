package com.example.springbootstudy;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * 训练 junit5 的使用
 *
 * @author MCW 2022/7/23
 */
//@SpringBootTest
public class Junit5Test {

    //######################### 测试 junit5 的常用注解 ##############################
    @Autowired
    private RedisTemplate redisTemplate;

    @Disabled
    @Test
    public void redisUse() {
        String o = (String) redisTemplate.opsForValue().get("s1");
        System.out.println(o);
    }


    /**
     * DisplayName 为测试类或者测试方法设置展示名称
     */
    @DisplayName("测试displayName注解")
    @Test
    public void testDisplayName() {
        System.out.println("测试displayName注解");
    }

    @DisplayName("测试2")
    @Test
    public void test2() {
        System.out.println("测试2");
    }

    /**
     * BeforeEach 表示在每个单元测试之前执行
     */
    @BeforeEach
    public void testBeforeEach() {
        System.out.println("测试开始！！！！！！！！！");
    }

    /**
     * AfterEach 表示在每个单元测试之后执行
     */
    @AfterEach
    public void testAfterEach() {
        System.out.println("测试结束！！！！！！！！！");
    }

    /**
     * BeforeAll 表示在所有单元测试之前执行
     */
    @BeforeAll
    public static void testBeforeAll() {
        System.out.println("所有测试开始！！！！！！！！！");
    }

    /**
     * AfterAll 表示在所有单元测试之后执行
     */
    @AfterAll
    public static void testAfterAll() {
        System.out.println("所有测试结束！！！！！！！！！");
    }

    /**
     * Timeout 超出时间，出现异常
     */
    @Disabled
    @Timeout(value = 1, unit = TimeUnit.SECONDS)
    @Test
    public void testTimeout() throws InterruptedException {
        Thread.sleep(1500);
    }

    @RepeatedTest(5)
    public void test3() {
        System.out.println(5);
    }

    //####################### 测试断言 #################################

    /**
     * 模拟业务逻辑
     */
    public int cal(int i, int j) {
        return i + j;
    }

    @DisplayName("测试简单断言")
    @Test
    public void testSimpleAssertions() {
        int v=cal(2,4);
        Assertions.assertEquals(6,v,"值不对，请重试！");

        Object o1=new Object();
        Object o2=new Object();
        Assertions.assertEquals(o1,o2);
    }

    @Test
    @DisplayName("array Assertions")
    public void testArrayAssertions(){
        Assertions.assertArrayEquals(new int[]{1,2},new int[]{1,4});
    }

    @Test
    @DisplayName("组合 Assertions")
    public void testAllAssertions() {
        Assertions.assertAll("1",
                () -> Assertions.assertTrue(true),
                () -> Assertions.assertNull(null));
    }

    @Test
    @DisplayName("exception Assertions")
    public void testExceptionAssertions() {
       Assertions.assertThrows(Exception.class,()-> {
           int i = 1 / 2;
       },"此处不应该失败");
    }

    @Test
    @DisplayName("Timeout Assertions")
    public void testTimeoutAssertions() {
        //如果测试方法时间超过 1s 将会异常
        Assertions.assertTimeout(Duration.ofMillis(1000),()->Thread.sleep(1000));
    }

    @Test
    @DisplayName("Fail Assertions")
    public void testFailAssertions() {
        //快速失败
        Assertions.fail();
    }

    //###################### 假设 ######################
    @DisplayName("测试前置条件")
    @Test
    public void testAssumptions(){
        Assertions.assertTrue(true);
        System.out.println("111111111111111111");
    }
}
