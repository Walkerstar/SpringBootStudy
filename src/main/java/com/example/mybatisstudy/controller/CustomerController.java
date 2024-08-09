package com.example.mybatisstudy.controller;


import com.example.mybatisstudy.entity.Customer;
import com.example.mybatisstudy.entity.Encrypt;
import com.example.mybatisstudy.mapper.CustomerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mcw 2022/2/10 17:01
 */
@RestController
public class CustomerController {

    Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @Resource
    CustomerMapper customerMapper;

    @RequestMapping("/add/{phone}/{address}")
    public String addCustomer(@PathVariable("phone") String phone,
                              @PathVariable("address") String address) {
        logger.info("receive phone and address is :" + phone + "  " + address);

        int i = customerMapper.addCustomer(new Encrypt(phone), address);

        return "success add : " + i;
    }


    @RequestMapping("/find/{phone}")
    public Map findCustomer(@PathVariable("phone") String phone) {
        logger.info("receive phone and address is :" + phone);

        Customer customer = customerMapper.findCustomer(new Encrypt(phone));
        HashMap hashMap = new HashMap();
        hashMap.put("----", customer);
        return hashMap;
    }
}
