package com.ruoyi.common.utils;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.uuid.IdUtils;

/**
 * Sa-Token 工具类
 * 
 * @author ruoyi
 */
public class SaTokenUtils {
    
    /**
     * 获取当前登录用户ID
     */
    public static Long getUserId() {
        try {
            return Long.valueOf(String.valueOf(StpUtil.getLoginId()));
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * 获取当前登录用户
     */
    public static SysUser getSysUser() {
        try {
            return (SysUser) StpUtil.getSession().get("user");
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * 设置当前登录用户
     */
    public static void setSysUser(SysUser user) {
        StpUtil.getSession().set("user", user);
    }
    
    /**
     * 获取登录名
     */
    public static String getLoginName() {
        SysUser user = getSysUser();
        return user != null ? user.getLoginName() : null;
    }
    
    /**
     * 获取Session
     */
    public static SaSession getSession() {
        return StpUtil.getSession();
    }
    
    /**
     * 登出
     */
    public static void logout() {
        StpUtil.logout();
    }
    
    /**
     * 获取Token值
     */
    public static String getTokenValue() {
        return StpUtil.getTokenValue();
    }
    
    /**
     * 获取Session ID
     */
    public static String getSessionId() {
        return StpUtil.getTokenValue();
    }
    
    /**
     * 获取IP地址
     * 注意：需要在有HTTP请求上下文时调用
     */
    public static String getIp() {
        try {
            return StringUtils.substring(
                IpUtils.getIpAddr(ServletUtils.getRequest()), 0, 128);
        } catch (Exception e) {
            return "";
        }
    }
    
    /**
     * 是否为管理员
     */
    public static boolean isAdmin() {
        Long userId = getUserId();
        return isAdmin(userId);
    }
    
    /**
     * 是否为管理员
     * 
     * @param userId 用户ID
     * @return 结果
     */
    public static boolean isAdmin(Long userId) {
        return userId != null && 1L == userId;
    }
    
    /**
     * 生成随机盐
     */
    public static String randomSalt() {
        return IdUtils.fastSimpleUUID().substring(0, 6);
    }
}
