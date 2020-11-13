package com.sleepy.zeo.springboot.database.mybatis.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * CREATE TABLE sys_permission (
 *   id int(11) NOT NULL AUTO_INCREMENT,
 *   url varchar(255) DEFAULT NULL,
 *   role_id int(11) DEFAULT NULL,
 *   permission varchar(255) DEFAULT NULL,
 *   PRIMARY KEY (id),
 *   KEY fk_roleId (role_id),
 *   CONSTRAINT fk_roleId FOREIGN KEY (role_id) REFERENCES sys_role (id) ON DELETE CASCADE ON UPDATE CASCADE
 * )；
 */
@Mapper
@Repository
public interface SysPermissionDao {

}
