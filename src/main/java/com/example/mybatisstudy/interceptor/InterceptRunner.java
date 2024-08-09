package com.example.mybatisstudy.interceptor;

import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 这样写的目的确定拦截器的优先级，
 * <p>
 * 如果使用了其他拦截器，例如 PageHelper ，由于 PageHelper 的拦截器优先级更高，
 * 因此自定义的拦截器会后执行，有可能导致自定义拦截器的功能执行后达不到预期效果
 *
 * @author MCW 2023/8/3
 */
@Component
public class InterceptRunner implements ApplicationRunner {

    @Resource
    private List<SqlSessionFactory> sqlSessionFactoryList;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // MySqlInterceptor mySqlInterceptor = new MySqlInterceptor();
        // for (SqlSessionFactory factory : sqlSessionFactoryList) {
        //     Configuration configuration = factory.getConfiguration();
        //     configuration.addInterceptor(mySqlInterceptor);
        // }
    }
}
