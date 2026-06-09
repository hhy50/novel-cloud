### 阶段十：异常处理迁移

#### 步骤 10.1：更新全局异常处理器

**文件**: `ruoyi-framework/src/main/java/com/ruoyi/framework/web/exception/GlobalExceptionHandler.java`

**操作**: 修改Shiro异常处理为Sa-Token异常处理：
```java
package com.ruoyi.framework.web.exception;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import com.ruoyi.common.core.domain.AjaxResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    /**
     * 未登录异常
     */
    @ExceptionHandler(NotLoginException.class)
    public AjaxResult handleNotLoginException(NotLoginException e) {
        log.error("未登录异常：{}", e.getMessage());
        
        String message = "未登录";
        if (e.getType().equals(NotLoginException.NOT_TOKEN)) {
            message = "未提供token";
        } else if (e.getType().equals(NotLoginException.INVALID_TOKEN)) {
            message = "token无效";
        } else if (e.getType().equals(NotLoginException.TOKEN_TIMEOUT)) {
            message = "token已过期";
        } else if (e.getType().equals(NotLoginException.BE_REPLACED)) {
            message = "token已被顶下线";
        } else if (e.getType().equals(NotLoginException.KICK_OUT)) {
            message = "token已被踢下线";
        }
        
        return AjaxResult.error(401, message);
    }
    
    /**
     * 权限不足异常
     */
    @ExceptionHandler(NotPermissionException.class)
    public AjaxResult handleNotPermissionException(NotPermissionException e) {
        log.error("权限不足异常：{}", e.getMessage());
        return AjaxResult.error(403, "没有访问权限，请联系管理员授权");
    }
    
    /**
     * 角色不足异常
     */
    @ExceptionHandler(NotRoleException.class)
    public AjaxResult handleNotRoleException(NotRoleException e) {
        log.error("角色不足异常：{}", e.getMessage());
        return AjaxResult.error(403, "没有访问权限，需要角色：" + e.getRole());
    }
    
    // ... 保留其他异常处理 ...
}
```

---

### 阶段十一：删除旧代码

#### 步骤 11.1：删除Shiro相关文件

**删除以下目录和文件**：
```
ruoyi-framework/src/main/java/com/ruoyi/framework/shiro/
├── realm/
│   └── UserRealm.java
├── rememberMe/
│   └── CustomCookieRememberMeManager.java
├── service/
│   ├── SysLoginService.java (已被SaTokenLoginService替代)
│   ├── SysPasswordService.java (已被SaTokenPasswordService替代)
│   ├── SysRegisterService.java (迁移到satoken包)
│   └── SysShiroService.java
├── session/
│   ├── OnlineSessionDAO.java
│   └── OnlineSessionFactory.java
├── util/
│   └── AuthorizationUtils.java
└── web/
    ├── CustomShiroFilterFactoryBean.java
    ├── filter/
    │   ├── LogoutFilter.java
    │   ├── captcha/CaptchaValidateFilter.java
    │   ├── csrf/CsrfValidateFilter.java
    │   ├── kickout/KickoutSessionFilter.java
    │   ├── online/OnlineSessionFilter.java
    │   └── sync/SyncOnlineSessionFilter.java
    └── session/
        ├── OnlineWebSessionManager.java
        └── SpringSessionValidationScheduler.java

ruoyi-framework/src/main/java/com/ruoyi/framework/config/ShiroConfig.java

ruoyi-framework/src/main/java/com/ruoyi/framework/manager/ShutdownManager.java (修改移除Shiro相关)

ruoyi-admin/src/main/resources/ehcache/ehcache-shiro.xml
```

#### 步骤 11.2：可选保留（兼容过渡）

**保留但标记为@Deprecated**：
- `ShiroUtils.java` - 内部调用SaTokenUtils
- `ShiroConstants.java` - 重命名为SaTokenConstants或保留

---

### 阶段十二：测试验证

#### 步骤 12.1：单元测试清单

**创建测试类**: `SaTokenMigrationTest.java`

```java
package com.ruoyi.test;

import cn.dev33.satoken.stp.StpUtil;
import com.ruoyi.common.utils.SaTokenUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Sa-Token 迁移测试
 */
@SpringBootTest
public class SaTokenMigrationTest {
    
    @Test
    public void testLogin() {
        // 测试登录
        StpUtil.login(1L);
        assertTrue(StpUtil.isLogin());
        assertEquals(1L, SaTokenUtils.getUserId());
    }
    
    @Test
    public void testPermission() {
        // 测试权限检查
        StpUtil.login(1L);
        // 假设用户1是管理员
        assertTrue(StpUtil.hasPermission("*:*:*"));
    }
    
    @Test
    public void testRole() {
        // 测试角色检查
        StpUtil.login(1L);
        assertTrue(StpUtil.hasRole("admin"));
    }
    
    @Test
    public void testSession() {
        // 测试Session
        StpUtil.login(1L);
        StpUtil.getSession().set("testKey", "testValue");
        assertEquals("testValue", StpUtil.getSession().get("testKey"));
    }
    
    @Test
    public void testLogout() {
        // 测试登出
        StpUtil.login(1L);
        StpUtil.logout();
        assertFalse(StpUtil.isLogin());
    }
}
```

#### 步骤 12.2：功能测试清单

**必测功能**：

✅ **1. 用户登录**
- [ ] 正常登录
- [ ] 错误密码登录
- [ ] 验证码校验
- [ ] 密码重试限制
- [ ] 记住我功能

✅ **2. 权限控制**
- [ ] 菜单权限显示
- [ ] 按钮权限控制
- [ ] 接口权限拦截
- [ ] 角色权限验证

✅ **3. 会话管理**
- [ ] 在线用户列表
- [ ] 强制下线
- [ ] 批量踢出
- [ ] 会话超时
- [ ] 同一用户多端登录控制

✅ **4. 注销登出**
- [ ] 正常登出
- [ ] 清除Session
- [ ] 跳转登录页

✅ **5. 异常处理**
- [ ] 未登录跳转
- [ ] 权限不足提示
- [ ] Token过期处理

✅ **6. 前端集成**
- [ ] Thymeleaf权限标签
- [ ] Token传递
- [ ] 自动刷新Token

#### 步骤 12.3：性能对比测试

**测试脚本**: `performance_test.jmx` (JMeter)

```xml
<?xml version="1.0" encoding="UTF-8"?>
<jmeterTestPlan version="1.2">
  <hashTree>
    <TestPlan>
      <stringProp name="TestPlan.name">Sa-Token vs Shiro 性能对比</stringProp>
    </TestPlan>
    <hashTree>
      <ThreadGroup>
        <stringProp name="ThreadGroup.num_threads">100</stringProp>
        <stringProp name="ThreadGroup.ramp_time">10</stringProp>
        <stringProp name="ThreadGroup.duration">60</stringProp>
      </ThreadGroup>
      <hashTree>
        <!-- 登录请求 -->
        <HTTPSamplerProxy>
          <stringProp name="HTTPSampler.path">/login</stringProp>
          <stringProp name="HTTPSampler.method">POST</stringProp>
        </HTTPSamplerProxy>
        <!-- 权限检查请求 -->
        <HTTPSamplerProxy>
          <stringProp name="HTTPSampler.path">/system/user/list</stringProp>
          <stringProp name="HTTPSampler.method">GET</stringProp>
        </HTTPSamplerProxy>
      </hashTree>
    </hashTree>
  </hashTree>
</jmeterTestPlan>
```

---

### 阶段十三：回滚方案

#### 步骤 13.1：版本控制

**Git分支策略**：
```bash
# 创建迁移分支
git checkout -b feature/migrate-to-satoken

# 提交每个阶段的更改
git add .
git commit -m "阶段一：依赖替换"
git commit -m "阶段二：配置类迁移"
# ... 等等

# 如果需要回滚
git checkout main
git branch -D feature/migrate-to-satoken
```

#### 步骤 13.2：数据库回滚

**备份策略**：
```sql
-- 备份在线用户表（如果有修改）
CREATE TABLE sys_user_online_backup AS SELECT * FROM sys_user_online;

-- 回滚
DROP TABLE sys_user_online;
ALTER TABLE sys_user_online_backup RENAME TO sys_user_online;
```

#### 步骤 13.3：快速回滚检查清单

**回滚前检查**：
- [ ] 备份当前代码分支
- [ ] 备份数据库
- [ ] 记录当前依赖版本
- [ ] 保存配置文件

**回滚步骤**：
1. 切换到备份分支
2. 恢复pom.xml依赖
3. 恢复application.yml配置
4. 恢复数据库（如有必要）
5. 重启应用

---

