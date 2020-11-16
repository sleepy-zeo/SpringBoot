package com.sleepy.zeo.springboot.service;

import com.sleepy.zeo.springboot.database.mybatis.pojo.SysPermission;

import java.util.List;

public interface SysPermissionService {

    List<SysPermission> fetchPermissionsByRoleId(Integer roleId);
}
