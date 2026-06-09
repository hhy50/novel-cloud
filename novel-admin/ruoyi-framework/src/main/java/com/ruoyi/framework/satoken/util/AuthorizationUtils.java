package com.ruoyi.framework.satoken.util;

import cn.dev33.satoken.stp.StpUtil;

/**
 * 用户授权信息工具类（Sa-Token版本）
 * 
 * @author ruoyi
 */
public class AuthorizationUtils
{
    /**
     * 清理所有用户授权信息缓存
     * 在Sa-Token中，通过踢人下线并重新登录来刷新权限
     */
    public static void clearAllCachedAuthorizationInfo()
    {
        // Sa-Token的权限缓存通过注解自动管理
        // 如果需要强制刷新所有在线用户的权限，可以：
        // 1. 清空所有用户的Token（会导致所有用户需要重新登录）
        // 2. 或者让用户下次请求时自动刷新权限（推荐）
        
        // 这里不做任何操作，因为Sa-Token会在下次权限检查时自动从数据库重新加载
        // 如果确实需要立即生效，可以考虑清除特定用户的session
        
        // 注意：如果使用了Sa-Token的权限缓存，可以在这里清除
        // 但默认情况下Sa-Token每次都会调用StpInterface获取最新权限
    }
    
    /**
     * 清理指定用户的授权信息缓存
     *
     * @param userId 用户ID
     */
    public static void clearCachedAuthorizationInfo(Object userId)
    {
        try
        {
            // 删除指定用户的session，强制下次请求重新加载权限
            StpUtil.logout(userId);
        }
        catch (Exception e)
        {
            // 忽略异常，用户可能未登录
        }
    }
}
