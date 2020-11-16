package com.sleepy.zeo.springboot.service;

import com.sleepy.zeo.springboot.database.mybatis.dao.SysRoleDao;
import com.sleepy.zeo.springboot.database.mybatis.pojo.SysRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysRoleServiceImpl implements SysRoleService {
    private SysRoleDao sysRoleDao;

    @Autowired
    public void setSysRoleDao(SysRoleDao sysRoleDao) {
        this.sysRoleDao = sysRoleDao;
    }

    @Override
    public SysRole fetchRole(int id) {
        return sysRoleDao.fetchRole(id);
    }

    @Override
    public SysRole fetchRole2(String roleName) {
        return sysRoleDao.fetchRole2(roleName);
    }
}
