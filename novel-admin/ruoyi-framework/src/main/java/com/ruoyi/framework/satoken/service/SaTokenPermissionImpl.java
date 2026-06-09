package com.ruoyi.framework.satoken.service;

import cn.dev33.satoken.stp.StpInterface;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.SaTokenUtils;
import com.ruoyi.system.service.ISysMenuService;
import com.ruoyi.system.service.ISysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Sa-Token 权限认证接口实现
 * 
 * @author ruoyi
 */
@Component
public class SaTokenPermissionImpl implements StpInterface {
    
    @Autowired
    private ISysMenuService menuService;
    
    @Autowired
    private ISysRoleService roleService;
    
    /**
     * 返回一个账号所拥有的权限码集合 
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        SysUser user = SaTokenUtils.getSysUser();
        Set<String> perms = new HashSet<>();
        
        // 管理员拥有所有权限
        if (user != null && user.isAdmin()) {
            perms.add("*:*:*");
        } else if (user != null) {
            perms = menuService.selectPermsByUserId(user.getUserId());
        }
        
        return new ArrayList<>(perms);
    }

    /**
     * 返回一个账号所拥有的角色标识集合 
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        SysUser user = SaTokenUtils.getSysUser();
        Set<String> roles = new HashSet<>();
        
        // 管理员拥有所有角色
        if (user != null && user.isAdmin()) {
            roles.add("admin");
        } else if (user != null) {
            roles = roleService.selectRoleKeys(user.getUserId());
        }
        
        return new ArrayList<>(roles);
    }
}
