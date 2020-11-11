package com.sleepy.zeo.springboot.service;

import com.sleepy.zeo.springboot.database.mybatis.dao.SysUserRoleDao;
import com.sleepy.zeo.springboot.database.mybatis.pojo.SysUserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysUserRoleServiceImpl implements SysUserRoleService {
    private SysUserRoleDao sysUserRoleDao;

    @Autowired
    public void setSysUserRoleDao(SysUserRoleDao sysUserRoleDao) {
        this.sysUserRoleDao = sysUserRoleDao;
    }

    @Override
    public List<SysUserRole> fetchUserRole(int userId) {
        return sysUserRoleDao.fetchUserRole(userId);
    }
}
