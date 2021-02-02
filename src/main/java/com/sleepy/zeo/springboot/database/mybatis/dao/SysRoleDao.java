package com.sleepy.zeo.springboot.database.mybatis.dao;

import com.sleepy.zeo.springboot.database.mybatis.pojo.SysRole;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

// 对于xml生成jdbc接口的类不需要@Mapper注解
// 对于接口生成jdbc接口的类，需要添加@Mapper注解
@Mapper
@Repository
public interface SysRoleDao {

    @Results(
            id = "role",
            value = {
                    @Result(property = "roleId", column = "id", id = true),
                    @Result(property = "rolename", column = "name")
            }
    )
    @Select({"select * from sys_role where id = #{id}"})
    SysRole fetchRole(@Param("id") int id);

    @ResultMap(value = "role")
    @Select({"select * from sys_role where name = #{roleName}"})
    SysRole fetchRole2(@Param("roleName") String roleName);
}
