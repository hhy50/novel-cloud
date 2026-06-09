### 阶段三：核心工具类迁移

#### 步骤 3.1：创建 Sa-Token 工具类

**新建文件**: `ruoyi-common/src/main/java/com/ruoyi/common/utils/SaTokenUtils.java`

```java
package com.ruoyi.common.utils;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import com.ruoyi.common.core.domain.entity.SysUser;

/**
 * Sa-Token 工具类
 */
public class SaTokenUtils {
    
    /**
     * 获取当前登录用户ID
     */
    public static Long getUserId() {
        return Long.valueOf(String.valueOf(StpUtil.getLoginId()));
    }
    
    /**
     * 获取当前登录用户
     */
    public static SysUser getSysUser() {
        return (SysUser) StpUtil.getSession().get("user");
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
     * 获取Session ID
     */
    public static String getSessionId() {
        return StpUtil.getTokenValue();
    }
    
    /**
     * 获取IP地址
     */
    public static String getIp() {
        return ServletUtils.getClientIP();
    }
    
    /**
     * 是否为管理员
     */
    public static boolean isAdmin() {
        return isAdmin(getUserId());
    }
    
    /**
     * 是否为管理员
     */
    public static boolean isAdmin(Long userId) {
        return userId != null && 1L == userId;
    }
    
    /**
     * 生成随机盐（如果还需要的话）
     */
    public static String randomSalt() {
        return IdUtils.fastSimpleUUID().substring(0, 6);
    }
}
```

#### 步骤 3.2：更新 ShiroUtils（兼容过渡）

**文件**: `ruoyi-common/src/main/java/com/ruoyi/common/utils/ShiroUtils.java`

**操作**: 保留文件，但方法内部调用 SaTokenUtils（向后兼容）：
```java
package com.ruoyi.common.utils;

import com.ruoyi.common.core.domain.entity.SysUser;

/**
 * Shiro工具类（兼容层，内部调用SaToken）
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
```

#### 步骤 3.3：更新常量类

**文件**: `ruoyi-common/src/main/java/com/ruoyi/common/constant/ShiroConstants.java`

**操作**: 重命名为 `SaTokenConstants.java` 或保留并标记为 @Deprecated：
```java
package com.ruoyi.common.constant;

/**
 * 认证框架通用常量
 */
public class SaTokenConstants {
    
    /** 当前登录的用户 */
    public static final String CURRENT_USER = "currentUser";
    
    /** 用户名字段 */
    public static final String CURRENT_USERNAME = "username";
    
    /** 锁定屏幕字段 */
    public static final String LOCK_SCREEN = "lockscreen";
    
    /** 消息key */
    public static final String MESSAGE = "message";
    
    /** 错误key */
    public static final String ERROR = "errorMsg";
    
    /** csrf token */
    public static final String CSRF_TOKEN = "csrf_token";
    
    /** csrf request header */
    public static final String X_CSRF_TOKEN = "X-CSRF-Token";
    
    /** 当前在线会话 */
    public static final String ONLINE_SESSION = "online_session";
    
    /** 验证码key */
    public static final String CURRENT_CAPTCHA = "captcha";
    
    /** 验证码开关 */
    public static final String CURRENT_ENABLED = "captchaEnabled";
    
    /** 验证码类型 */
    public static final String CURRENT_TYPE = "captchaType";
    
    /** 验证码 */
    public static final String CURRENT_VALIDATECODE = "validateCode";
    
    /** 验证码错误 */
    public static final String CAPTCHA_ERROR = "captchaError";
    
    /** 登录记录缓存 */
    public static final String LOGIN_RECORD_CACHE = "loginRecordCache";
    
    /** 系统活跃用户缓存 */
    public static final String SYS_USERCACHE = "sys-userCache";
}
```

---

### 阶段四：业务服务层迁移

#### 步骤 4.1：重构登录服务

**文件**: `ruoyi-framework/src/main/java/com/ruoyi/framework/satoken/service/SaTokenLoginService.java`

**操作**: 创建新的登录服务（基于原 SysLoginService.java）：
```java
package com.ruoyi.framework.satoken.service;

import cn.dev33.satoken.stp.StpUtil;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.SaTokenConstants;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SaTokenUtils;
import com.ruoyi.common.utils.ServletUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Sa-Token 登录服务
 */
@Service
public class SaTokenLoginService {
    
    @Autowired
    private SaTokenPasswordService passwordService;
    
    @Autowired
    private ISysUserService userService;
    
    @Autowired
    private ISysMenuService menuService;
    
    @Autowired
    private ISysConfigService configService;
    
    /**
     * 登录
     */
    public SysUser login(String username, String password) {
        // 验证码校验
        if (SaTokenConstants.CAPTCHA_ERROR.equals(
            ServletUtils.getRequest().getAttribute(SaTokenConstants.CURRENT_CAPTCHA))) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(
                username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.error")));
            throw new CaptchaException();
        }
        
        // 用户名或密码验证
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(
                username, Constants.LOGIN_FAIL, MessageUtils.message("not.null")));
            throw new UserNotExistsException();
        }
        
        // 密码长度验证
        if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(
                username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
            throw new UserPasswordNotMatchException();
        }
        
        // 用户名长度验证
        if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(
                username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
            throw new UserPasswordNotMatchException();
        }
        
        // IP黑名单校验
        String blackStr = configService.selectConfigByKey("sys.login.blackIPList");
        if (IpUtils.isMatchedIp(blackStr, SaTokenUtils.getIp())) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(
                username, Constants.LOGIN_FAIL, MessageUtils.message("login.blocked")));
            throw new BlackListException();
        }
        
        // 查询用户信息
        SysUser user = userService.selectUserByLoginName(username);
        
        if (user == null) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(
                username, Constants.LOGIN_FAIL, MessageUtils.message("user.not.exists")));
            throw new UserNotExistsException();
        }
        
        if (UserStatus.DELETED.getCode().equals(user.getDelFlag())) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(
                username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.delete")));
            throw new UserDeleteException();
        }
        
        if (UserStatus.DISABLE.getCode().equals(user.getStatus())) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(
                username, Constants.LOGIN_FAIL, MessageUtils.message("user.blocked")));
            throw new UserBlockedException();
        }
        
        // 密码验证
        passwordService.validate(user, password);
        
        // Sa-Token 登录
        StpUtil.login(user.getUserId());
        
        // 存储用户信息到Session
        SaTokenUtils.setSysUser(user);
        
        // 记录登录日志
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(
            username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
        
        setRolePermission(user);
        recordLoginInfo(user.getUserId());
        
        return user;
    }
    
    /**
     * 设置角色权限
     */
    public void setRolePermission(SysUser user) {
        // 保持原有逻辑
        // ...
    }
    
    /**
     * 记录登录信息
     */
    public void recordLoginInfo(Long userId) {
        userService.updateLoginInfo(userId, SaTokenUtils.getIp(), DateUtils.getNowDate());
    }
}
```

