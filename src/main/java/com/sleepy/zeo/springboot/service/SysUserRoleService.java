package com.sleepy.zeo.springboot.service;

import com.sleepy.zeo.springboot.database.mybatis.pojo.SysUserRole;

import java.util.List;

public interface SysUserRoleService {

    List<SysUserRole> fetchUserRole(int userId);
}
