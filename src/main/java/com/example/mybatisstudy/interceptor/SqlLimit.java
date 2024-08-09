package com.example.mybatisstudy.interceptor;

import java.lang.annotation.*;

/**
 * 加上 此 注解后， 可进行权限过滤
 *
 * @author MCW 2023/8/3
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface SqlLimit {

    /**
     * sql表别名
     * <p>
     * alis 用于标注 sql 表别名，
     * 如 针对 sql select * from tablea as a left join tableb as b on a.id = b.id 进行改写，如果不知道表别名，会直接在后面拼接 where dpt_id = #{dptId},
     * 那此 SQL 就会错误的，通过别名 @SqlLimit(alis = "a") 就可以知道需要拼接的是 where a.dpt_id = #{dptId}
     */
    String alias() default "";


    /**
     * 通过此列名进行限制
     */
    String column() default "";
}
