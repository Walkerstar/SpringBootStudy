package com.example.mybatisstudy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author mcw 2022/6/29 16:46
 */
@Controller
public class mybatiesTestController {

    @RequestMapping(value = "/leaf")
    public String useThyemLeaf(){

        System.out.println("sddddddddddddd");
        return "index";
    }
}
