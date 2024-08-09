package com.example.springbootstudy;

import com.example.springbootstudy.beanLifecycle.BeanOfLifeCycle;
import com.example.springbootstudy.service.ServiceA;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.TypeExcludeFilter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;

@SpringBootApplication
//@ComponentScan(excludeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION,classes = Component.class)})
//@ComponentScan 一旦使用，则只会加载 它指定的包，启动类的默认加载机制不会执行
@ComponentScan(basePackages = {"com.example.*"},
        excludeFilters = {@ComponentScan.Filter(type = FilterType.CUSTOM, classes = CustomerExcludeFilter.class)})
@MapperScan("com.example.*")
// localCache 使用
@EnableScheduling
@EnableAsync
public class SpringBootStudyApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SpringBootStudyApplication.class, args);
        ServiceA serviceA = context.getBean(ServiceA.class);
        serviceA.sayHello();
    }

    @Bean(initMethod = "myInit", destroyMethod = "myDestroy")
    public BeanOfLifeCycle bean() {
        BeanOfLifeCycle bean = new BeanOfLifeCycle();
        return bean;
    }

}

class CustomerExcludeFilter extends TypeExcludeFilter {
    @Override
    public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
        String className = metadataReader.getClassMetadata().getClassName();
        return className.startsWith("com.example.springbootstudy.redis") ||
                className.startsWith("com.example.rabbitmqstudy") ;
        // return !className.startsWith("com.example.springbootstudy");
    }
}

