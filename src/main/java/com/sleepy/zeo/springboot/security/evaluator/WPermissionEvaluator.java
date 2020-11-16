package com.sleepy.zeo.springboot.security.evaluator;

import com.sleepy.zeo.springboot.database.mybatis.pojo.SysPermission;
import com.sleepy.zeo.springboot.service.SysPermissionService;
import com.sleepy.zeo.springboot.service.SysRoleService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Component
public class WPermissionEvaluator implements PermissionEvaluator {

    private static final Log logger = LogFactory.getLog(WPermissionEvaluator.class);

    private SysRoleService roleService;
    private SysPermissionService permissionService;

    @Autowired
    public void setRoleService(SysRoleService roleService) {
        this.roleService = roleService;
    }

    @Autowired
    public void setPermissionService(SysPermissionService permissionService) {
        this.permissionService = permissionService;
    }

    /*
     * targetUrl对应@PreAuthorize("hasPermission('/admin','r')")的第一个参数
     * targetPermission对应@PreAuthorize("hasPermission('/admin','r')")的第二个参数
     */
    @Override
    public boolean hasPermission(Authentication authentication, Object targetUrl, Object targetPermission) {
        User user = (User) authentication.getPrincipal();
        Collection<GrantedAuthority> authorities = user.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            String roleName = authority.getAuthority();

            int roleId = roleService.fetchRole2(roleName).roleId;
            logger.info("roleName: " + roleName + ", roleId: " + roleId);
            List<SysPermission> permissionsList = permissionService.fetchPermissionsByRoleId(roleId);
            for (SysPermission p : permissionsList) {
                List<Object> permissions = p.getPermissions();
                logger.info("per: " + permissions);
                if (targetUrl.equals(p.url) && permissions.contains(targetPermission)) {
                    return true;
                }
            }

        }
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable serializable, String s, Object o) {
        return false;
    }
}
