package com.sleepy.zeo.springboot.database.mybatis.dao;

import com.sleepy.zeo.springboot.database.mybatis.pojo.SysRole;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

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
}