package com.example.mybatisstudy.mapper;

import com.example.mybatisstudy.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


/**
 * @author mcw 2022/2/8 14:48
 */
@Mapper
public interface RoleMapper {
    /**
     * query information of one role
     * @param roleName name
     * @return Role
     */
    @Select("select seq_no,role_desc,role_id,role_name from sys_role where role_name = #{roleName}")
    Role queryRoleByName(@Param("roleName") String roleName);
}
