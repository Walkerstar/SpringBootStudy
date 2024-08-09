package com.example.mybatisstudy.service;

import com.example.mybatisstudy.entity.Role;
import com.example.mybatisstudy.mapper.RoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author mcw 2022/2/8 15:17
 */
@Service
public class RoleService {

    @Autowired
    RoleMapper roleMapper;

    public Role query(String roleName){
        return roleMapper.queryRoleByName(roleName);
    }
}
