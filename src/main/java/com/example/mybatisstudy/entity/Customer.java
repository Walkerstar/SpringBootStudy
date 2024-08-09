package com.example.mybatisstudy.entity;

import lombok.Data;
import lombok.ToString;

/**
 * @author mcw 2022/2/10 17:18
 */
@Data
@ToString
public class Customer {
    int id;
    Encrypt phone;
    String address;
}
