package com.example.springbootstudy;

import com.atguigu.hello.service.HelloService;
import com.example.springbootstudy.controller.MyController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

@SpringBootTest
class SpringBootStudyApplicationTests {

    @Autowired
    HelloService helloService;

    @Resource
    MyController myController;

    @Test
    void contextLoads() {

    }

    @DisplayName("测试")
    @Test
    public void test2() {
        System.out.println("测试2");
        System.out.println(helloService.sayHello("Jack"));
    }

    @Test
    public void testGetBean(){
        //MockServletContext sc=new MockServletContext("");
        //sc.addInitParameter(ContextLoader.CONFIG_LOCATION_PARAM,"");
        //ServletContextListener listener=new ContextLoaderListener();
        //ServletContextEvent event=new ServletContextEvent(sc);
        //listener.contextInitialized(event);
        //
        //WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
        //Object xString = context.getBean(MyController.class);
        System.out.println(myController);
    }


    @Test
    public void testRabbitmqSendMessage(){
        RestTemplate testRestTemplate=new RestTemplate();
        String object = testRestTemplate.getForObject("http://localhost:8080/sendMessage", String.class);
        System.out.println(object);
    }
}
