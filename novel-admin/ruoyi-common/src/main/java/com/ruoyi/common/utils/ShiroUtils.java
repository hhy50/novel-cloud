package com.ruoyi.common.utils;

import com.ruoyi.common.core.domain.entity.SysUser;

/**
 * Shiro工具类（兼容层，内部调用SaToken）
 *
 * @author ruoyi
 * @deprecated 请使用 SaTokenUtils
 */
@Deprecated
public class ShiroUtils {
    
    public static SysUser getSysUser() {
        return SaTokenUtils.getSysUser();
    }
    
    public static void setSysUser(SysUser user) {
        SaTokenUtils.setSysUser(user);
    }
    
    public static Long getUserId() {
        return SaTokenUtils.getUserId();
    }
    
    public static String getLoginName() {
        return SaTokenUtils.getLoginName();
    }
    
    public static String getIp() {
        return SaTokenUtils.getIp();
    }
    
    public static String getSessionId() {
        return SaTokenUtils.getSessionId();
    }
    
    public static void logout() {
        SaTokenUtils.logout();
    }
    
    public static boolean isAdmin() {
        return SaTokenUtils.isAdmin();
    }
    
    public static boolean isAdmin(Long userId) {
        return SaTokenUtils.isAdmin(userId);
    }
    
    public static String randomSalt() {
        return SaTokenUtils.randomSalt();
    }
}
