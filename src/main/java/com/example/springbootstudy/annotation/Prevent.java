package com.example.springbootstudy.annotation;

import java.lang.annotation.*;

/**
 * 接口防刷注解
 *
 * 使用:
 * 在相应需要防刷的方法上加上该注解，即可
 * @author MCW 2022/10/16
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Prevent {

    /**
     * 限制的时间值（秒）
     */
    String value() default "60";

    /**
     * 提示
     */
    String message() default "";

    /**
     * 策略
     */
    PreventStrategy strategy() default PreventStrategy.DEFAULT;
}
