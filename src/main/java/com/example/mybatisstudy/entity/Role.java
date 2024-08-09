package com.example.mybatisstudy.entity;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @author mcw 2022/2/8 14:44
 */
@Data
@ToString
public class Role {
    private String seqNo;
    private String roleDesc;
    private String roleId;
    private String roleName;

}
