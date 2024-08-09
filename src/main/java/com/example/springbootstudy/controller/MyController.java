package com.example.springbootstudy.controller;

import com.example.springbootstudy.annotation.Prevent;
import com.example.springbootstudy.event.MyEventPublisher;
import com.example.springbootstudy.event.OneEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author MCW 2022/10/16
 */
@RestController
public class MyController {

    @Resource
    MyEventPublisher myEventPublisher;

    @RequestMapping("/testPrevent")
    // @Prevent(value = "10", message = "请不要多次调用")
    public String testPrevent(HttpServletRequest request) {

        OneEvent oneEvent = new OneEvent("");
        myEventPublisher.sendEvent(oneEvent);

        return "success";
    }
}
