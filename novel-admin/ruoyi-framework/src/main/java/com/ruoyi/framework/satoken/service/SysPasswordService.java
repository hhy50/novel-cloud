package com.ruoyi.framework.satoken.service;

import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.exception.user.UserPasswordNotMatchException;
import com.ruoyi.common.exception.user.UserPasswordRetryLimitExceedException;
import com.ruoyi.common.utils.CacheUtils;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.security.Md5Utils;
import com.ruoyi.framework.manager.AsyncManager;
import com.ruoyi.framework.manager.factory.AsyncFactory;

/**
 * 登录密码方法（Sa-Token版本）
 * 
 * @author ruoyi
 */
@Component
public class SysPasswordService
{
    private static final String LOGIN_RECORD_CACHE = "login-record";

    @Value(value = "${user.password.maxRetryCount}")
    private String maxRetryCount;

    public void validate(SysUser user, String password)
    {
        String loginName = user.getLoginName();

        AtomicInteger retryCount = (AtomicInteger) CacheUtils.get(LOGIN_RECORD_CACHE, loginName);

        if (retryCount == null)
        {
            retryCount = new AtomicInteger(0);
            CacheUtils.put(LOGIN_RECORD_CACHE, loginName, retryCount);
        }
        if (retryCount.incrementAndGet() > Integer.valueOf(maxRetryCount).intValue())
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(loginName, Constants.LOGIN_FAIL, MessageUtils.message("user.password.retry.limit.exceed", maxRetryCount)));
            throw new UserPasswordRetryLimitExceedException(Integer.valueOf(maxRetryCount).intValue());
        }

        if (!matches(user, password))
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(loginName, Constants.LOGIN_FAIL, MessageUtils.message("user.password.retry.limit.count", retryCount)));
            CacheUtils.put(LOGIN_RECORD_CACHE, loginName, retryCount);
            throw new UserPasswordNotMatchException();
        }
        else
        {
            clearLoginRecordCache(loginName);
        }
    }

    public boolean matches(SysUser user, String newPassword)
    {
        return user.getPassword().equals(encryptPassword(user.getLoginName(), newPassword, user.getSalt()));
    }

    public void clearLoginRecordCache(String loginName)
    {
        CacheUtils.remove(LOGIN_RECORD_CACHE, loginName);
    }

    public String encryptPassword(String loginName, String password, String salt)
    {
        return Md5Utils.hash(loginName + password + salt);
    }
}
