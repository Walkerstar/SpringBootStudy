package com.example.mybatisstudy.mapper;

import com.example.mybatisstudy.entity.Customer;
import com.example.mybatisstudy.entity.Encrypt;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author mcw 2022/2/10 16:59
 */
@Mapper
public interface CustomerMapper {
    int addCustomer(@Param("phone") Encrypt phone,@Param("address") String address);

    Customer findCustomer(@Param("phone") Encrypt phone);
}
