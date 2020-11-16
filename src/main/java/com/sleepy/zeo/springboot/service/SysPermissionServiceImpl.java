package com.sleepy.zeo.springboot.service;

import com.sleepy.zeo.springboot.database.mybatis.dao.SysPermissionDao;
import com.sleepy.zeo.springboot.database.mybatis.pojo.SysPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysPermissionServiceImpl implements SysPermissionService {

    private SysPermissionDao permissionDao;

    @Autowired
    public void setPermissionDao(SysPermissionDao permissionDao) {
        this.permissionDao = permissionDao;
    }

    @Override
    public List<SysPermission> fetchPermissionsByRoleId(Integer roleId) {
        return permissionDao.fetchPermissions(roleId);
    }
}
