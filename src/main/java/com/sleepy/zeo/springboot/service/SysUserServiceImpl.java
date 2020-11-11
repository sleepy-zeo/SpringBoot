package com.sleepy.zeo.springboot.service;

import com.sleepy.zeo.springboot.database.mybatis.dao.SysUserDao;
import com.sleepy.zeo.springboot.database.mybatis.pojo.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl implements SysUserService {
    private SysUserDao sysUserDao;

    @Autowired
    public void setSysUserDao(SysUserDao sysUserDao) {
        this.sysUserDao = sysUserDao;
    }

    @Override
    public SysUser fetchUser(int id) {
        return sysUserDao.fetchUserById(id);
    }

    @Override
    public SysUser fetchUser(String name) {
        return sysUserDao.fetchUserByName(name);
    }
}
