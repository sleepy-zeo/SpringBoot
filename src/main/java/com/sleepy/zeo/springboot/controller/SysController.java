package com.sleepy.zeo.springboot.controller;

import com.sleepy.zeo.springboot.database.mybatis.pojo.SysRole;
import com.sleepy.zeo.springboot.database.mybatis.pojo.SysUser;
import com.sleepy.zeo.springboot.database.mybatis.pojo.SysUserRole;
import com.sleepy.zeo.springboot.service.SysRoleService;
import com.sleepy.zeo.springboot.service.SysUserRoleService;
import com.sleepy.zeo.springboot.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/sys")
public class SysController {

    private SysUserService sysUserService;
    private SysRoleService sysRoleService;
    private SysUserRoleService sysUserRoleService;

    @Autowired
    public void setSysUserService(SysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    @Autowired
    public void setSysRoleService(SysRoleService sysRoleService) {
        this.sysRoleService = sysRoleService;
    }

    @Autowired
    public void setSysUserRoleService(SysUserRoleService sysUserRoleService) {
        this.sysUserRoleService = sysUserRoleService;
    }

    @RequestMapping("/user/{id}")
    @ResponseBody
    public SysUser fetchUser(@PathVariable("id") int id) {
        return sysUserService.fetchUser(id);
    }

    @RequestMapping("/role/{id}")
    @ResponseBody
    public SysRole fetchRole(@PathVariable("id") int id) {
        return sysRoleService.fetchRole(id);
    }

    @RequestMapping("/ur/{id}")
    @ResponseBody
    public List<SysUserRole> fetchUserRole(@PathVariable("id") int userId) {
        return sysUserRoleService.fetchUserRole(userId);
    }

}
