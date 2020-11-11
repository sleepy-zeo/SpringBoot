package com.sleepy.zeo.springboot.database.mybatis.dao;

import com.sleepy.zeo.springboot.database.mybatis.pojo.SysUser;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface SysUserDao {

    @Results(
            id = "user",
            value = {
                    @Result(property = "userId", column = "id", id = true),
                    @Result(property = "username", column = "name"),
                    @Result(property = "password", column = "password")
            }
    )
    @Select({"select * from sys_user where id = #{id}"})
    SysUser fetchUserById(@Param("id") int id);

    @ResultMap(value = "user")
    @Select({"select * from sys_user where name = #{name}"})
    SysUser fetchUserByName(@Param("name") String name);
}
