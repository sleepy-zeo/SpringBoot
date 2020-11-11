package com.sleepy.zeo.springboot.service;

import com.sleepy.zeo.springboot.database.mybatis.pojo.SysRole;
import com.sleepy.zeo.springboot.database.mybatis.pojo.SysUser;
import com.sleepy.zeo.springboot.database.mybatis.pojo.SysUserRole;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * CREATE TABLE sys_user (
 *   id int(11) NOT NULL AUTO_INCREMENT,
 *   name varchar(255) NOT NULL,
 *   password varchar(255) NOT NULL,
 *   PRIMARY KEY (id)
 * );
 *
 * CREATE TABLE sys_role (
 *   id int(11) NOT NULL,
 *   name varchar(255) NOT NULL,
 *   PRIMARY KEY (id)
 * );
 *
 * CREATE TABLE sys_user_role (
 *   user_id int(11) NOT NULL,
 *   role_id int(11) NOT NULL,
 *   PRIMARY KEY (user_id,role_id),
 *   CONSTRAINT fk_role_id FOREIGN KEY (role_id) REFERENCES sys_role (id) ON DELETE CASCADE ON UPDATE CASCADE,
 *   CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES sys_user (id) ON DELETE CASCADE ON UPDATE CASCADE
 * );
 *
 *
 * INSERT INTO sys_user VALUES ('9001', 'admin', '10001000');
 * INSERT INTO sys_user VALUES ('9002', 'steven', '00001111');
 *
 * // Spring Security规定权限格式必须为ROLE_XXX
 * INSERT INTO sys_role VALUES ('1001', 'ROLE_ADMIN');
 * INSERT INTO sys_role VALUES ('1002', 'ROLE_USER');
 *
 *
 * INSERT INTO sys_user_role VALUES ('9001', '1001');
 * INSERT INTO sys_user_role VALUES ('9002', '1002');
 */
@Service(value = "userDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {

    private Log logger = LogFactory.getLog(UserDetailsServiceImpl.class);

    private SysUserService userService;
    private SysRoleService roleService;
    private SysUserRoleService userRoleService;

    @Autowired
    public void setUserService(SysUserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setRoleService(SysRoleService roleService) {
        this.roleService = roleService;
    }

    @Autowired
    public void setUserRoleService(SysUserRoleService userRoleService) {
        this.userRoleService = userRoleService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Collection<GrantedAuthority> authorities = new ArrayList<>();

        SysUser sysUser = userService.fetchUser(username);
        if (sysUser == null) {
            throw new UsernameNotFoundException("username not exists");
        }
        logger.info("Username: " + username);

        List<SysUserRole> userRoles = userRoleService.fetchUserRole(sysUser.userId);
        for (SysUserRole sysUserRole : userRoles) {
            SysRole sysRole = roleService.fetchRole(sysUserRole.roleId);
            logger.info("Rolename: " + sysRole.rolename);
            authorities.add(new SimpleGrantedAuthority(sysRole.rolename));
        }

        return new User(sysUser.username, sysUser.password, authorities);
    }
}
