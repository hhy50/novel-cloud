package com.ruoyi.framework.aspectj;

import cn.dev33.satoken.annotation.SaCheckPermission;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import com.ruoyi.common.core.context.PermissionContextHolder;
import com.ruoyi.common.utils.StringUtils;

/**
 * 自定义权限拦截器，将权限字符串放到当前请求中以便用于多个角色匹配符合要求的权限
 * （Sa-Token版本）
 * 
 * @author ruoyi
 */
@Aspect
@Component
public class PermissionsAspect
{
    @Before("@annotation(controllerSaCheckPermission)")
    public void doBefore(JoinPoint point, SaCheckPermission controllerSaCheckPermission) throws Throwable
    {
        handleSaCheckPermission(point, controllerSaCheckPermission);
    }

    protected void handleSaCheckPermission(final JoinPoint joinPoint, SaCheckPermission saCheckPermission)
    {
        PermissionContextHolder.setContext(StringUtils.join(saCheckPermission.value(), ","));
    }
}
