package com.sleepy.zeo.springboot.database.mybatis.dao;

import com.sleepy.zeo.springboot.database.mybatis.pojo.SysPermission;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * CREATE TABLE sys_permission (
 *   id int(11) NOT NULL AUTO_INCREMENT,
 *   url varchar(255) DEFAULT NULL,
 *   role_id int(11) DEFAULT NULL,
 *   permission varchar(255) DEFAULT NULL,
 *   PRIMARY KEY (id),
 *   KEY fk_roleId (role_id),
 *   CONSTRAINT fk_roleId FOREIGN KEY (role_id) REFERENCES sys_role (id) ON DELETE CASCADE ON UPDATE CASCADE
 * );
 */
@Mapper
@Repository
public interface SysPermissionDao {

    @Results(
            id = "permission",
            value = {
                    @Result(property = "id", column = "id", id = true),
                    @Result(property = "url", column = "url"),
                    @Result(property = "roleId", column = "role_id"),
                    @Result(property = "permission", column = "permission")
            }
    )
    @Select({"select * from sys_permission where role_id = #{roleId}"})
    List<SysPermission> fetchPermissions(@Param("roleId") int roleId);
}
