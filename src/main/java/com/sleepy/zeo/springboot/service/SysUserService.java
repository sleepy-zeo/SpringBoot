package com.sleepy.zeo.springboot.service;

import com.sleepy.zeo.springboot.database.mybatis.pojo.SysUser;

public interface SysUserService {

    SysUser fetchUser(int id);

    SysUser fetchUser(String name);
}
