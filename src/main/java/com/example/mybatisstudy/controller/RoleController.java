package com.example.mybatisstudy.controller;

import com.example.mybatisstudy.entity.Role;
import com.example.mybatisstudy.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author mcw 2022/2/8 14:43
 */
@RestController
public class RoleController {

    private Logger logger = LoggerFactory.getLogger(RoleController.class);

    @Autowired
    RoleService roleService;

    @RequestMapping("/getRole/{roleName}")
    public String GetRole(@PathVariable String roleName){
        System.out.println("lalalala ~~~~~~~  "+roleName);
        Role role = roleService.query(roleName);
        logger.info(roleName+"------"+role);
        return role == null ? "查不到" : "找到了" + role.toString();
    }
}
