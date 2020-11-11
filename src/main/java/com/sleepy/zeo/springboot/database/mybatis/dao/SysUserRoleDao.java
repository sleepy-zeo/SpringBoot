package com.sleepy.zeo.springboot.database.mybatis.dao;

import com.sleepy.zeo.springboot.database.mybatis.pojo.SysUserRole;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SysUserRoleDao {

    @Results({
            @Result(property = "userId", column = "user_id"),
            @Result(property = "roleId", column = "role_id")
    })
    @Select({"select * from sys_user_role where user_id = #{userId}"})
    List<SysUserRole> fetchUserRole(@Param("userId") int userId);
}
