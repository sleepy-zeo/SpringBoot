package com.sleepy.zeo.springboot.service;

import com.sleepy.zeo.springboot.database.mybatis.pojo.SysRole;

public interface SysRoleService {

    SysRole fetchRole(int id);
}
