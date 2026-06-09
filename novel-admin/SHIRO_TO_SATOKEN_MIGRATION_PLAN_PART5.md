### 阶段七：Thymeleaf 模板迁移

#### 步骤 7.1：配置 Sa-Token Thymeleaf 方言

**文件**: `ruoyi-framework/src/main/java/com/ruoyi/framework/config/SaTokenConfig.java`

**操作**: 添加方言Bean：
```java
import cn.dev33.satoken.thymeleaf.dialect.SaTokenDialect;
import org.springframework.context.annotation.Bean;

@Configuration
public class SaTokenConfig implements WebMvcConfigurer {
    
    // ... 其他配置 ...
    
    /**
     * Sa-Token 整合 Thymeleaf 方言
     */
    @Bean
    public SaTokenDialect getSaTokenDialect() {
        return new SaTokenDialect();
    }
}
```

#### 步骤 7.2：模板标签替换规则

**全局替换**（100+ 个HTML文件）：

**替换方式1：使用命名空间**
```html
<!-- 在模板头部添加 -->
<html xmlns:th="http://www.thymeleaf.org" 
      xmlns:sa="http://sa-token.dev33.cn">
```

**替换规则**：
```html
<!-- 查找 -->
shiro:hasPermission="system:user:add"

<!-- 替换为 -->
sa:permission="system:user:add"
```

```html
<!-- 查找 -->
shiro:hasRole="admin"

<!-- 替换为 -->
sa:role="admin"
```

**示例1**（用户管理页面）:
```html
<!-- 原代码 -->
<a class="btn btn-success" onclick="$.operate.addTab()" shiro:hasPermission="system:user:add">
    <i class="fa fa-plus"></i> 新增
</a>

<!-- 改造后 -->
<a class="btn btn-success" onclick="$.operate.addTab()" sa:permission="system:user:add">
    <i class="fa fa-plus"></i> 新增
</a>
```

**示例2**（角色管理页面）:
```html
<!-- 原代码 -->
<a class="btn btn-primary single disabled" onclick="$.operate.edit()" shiro:hasPermission="system:role:edit">
    <i class="fa fa-edit"></i> 修改
</a>

<!-- 改造后 -->
<a class="btn btn-primary single disabled" onclick="$.operate.edit()" sa:permission="system:role:edit">
    <i class="fa fa-edit"></i> 修改
</a>
```

#### 步骤 7.3：批量替换脚本（可选）

**创建批量替换脚本**: `replace_template_tags.py`

```python
import os
import re

def replace_shiro_tags(file_path):
    """替换单个文件中的Shiro标签"""
    with open(file_path, 'r', encoding='utf-8') as f:
        content = f.read()
    
    # 替换 shiro:hasPermission 为 sa:permission
    content = re.sub(r'shiro:hasPermission="([^"]+)"', r'sa:permission="\1"', content)
    
    # 替换 shiro:hasRole 为 sa:role
    content = re.sub(r'shiro:hasRole="([^"]+)"', r'sa:role="\1"', content)
    
    # 替换 shiro:lacksPermission 为 sa:notPermission
    content = re.sub(r'shiro:lacksPermission="([^"]+)"', r'sa:notPermission="\1"', content)
    
    # 替换 shiro:lacksRole 为 sa:notRole
    content = re.sub(r'shiro:lacksRole="([^"]+)"', r'sa:notRole="\1"', content)
    
    # 添加命名空间（如果不存在）
    if 'xmlns:sa=' not in content and 'sa:permission' in content or 'sa:role' in content:
        content = content.replace(
            '<html xmlns:th="http://www.thymeleaf.org"',
            '<html xmlns:th="http://www.thymeleaf.org" xmlns:sa="http://sa-token.dev33.cn"'
        )
    
    with open(file_path, 'w', encoding='utf-8') as f:
        f.write(content)
    
    print(f"已处理: {file_path}")

def process_directory(directory):
    """处理目录下所有HTML文件"""
    for root, dirs, files in os.walk(directory):
        for file in files:
            if file.endswith('.html'):
                file_path = os.path.join(root, file)
                replace_shiro_tags(file_path)

# 执行替换
templates_dir = 'ruoyi-admin/src/main/resources/templates'
process_directory(templates_dir)
print("所有模板文件处理完成！")
```

**使用方法**：
```bash
python replace_template_tags.py
```

---

### 阶段八：在线用户管理迁移

#### 步骤 8.1：创建在线用户服务

**新建文件**: `ruoyi-framework/src/main/java/com/ruoyi/framework/satoken/service/SaTokenOnlineService.java`

```java
package com.ruoyi.framework.satoken.service;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.domain.SysUserOnline;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Sa-Token 在线用户服务
 */
@Service
public class SaTokenOnlineService {
    
    /**
     * 获取所有在线用户
     */
    public List<SysUserOnline> selectOnlineUserList() {
        List<SysUserOnline> userOnlineList = new ArrayList<>();
        
        // 获取所有在线token
        List<String> tokenList = StpUtil.searchTokenValue("", 0, -1, false);
        
        for (String token : tokenList) {
            try {
                // 根据token获取登录ID
                Object loginId = StpUtil.getLoginIdByToken(token);
                
                // 获取Session
                SaSession session = StpUtil.getSessionByLoginId(loginId);
                
                // 构造在线用户对象
                SysUserOnline online = new SysUserOnline();
                
                SysUser user = (SysUser) session.get("user");
                if (user != null) {
                    online.setSessionId(token);
                    online.setLoginName(user.getLoginName());
                    online.setDeptName(user.getDept() != null ? user.getDept().getDeptName() : "");
                    online.setIpaddr((String) session.get("loginIp"));
                    online.setLoginLocation((String) session.get("loginLocation"));
                    online.setBrowser((String) session.get("browser"));
                    online.setOs((String) session.get("os"));
                    online.setStartTimestamp(new Date(session.getCreateTime()));
                    online.setLastAccessTime(new Date(session.getLastActivityTime()));
                    online.setExpireTime(StpUtil.getTokenTimeout(token));
                    
                    userOnlineList.add(online);
                }
            } catch (Exception e) {
                // 忽略异常token
                continue;
            }
        }
        
        return userOnlineList;
    }
    
    /**
     * 踢出用户
     */
    public void forceLogout(String sessionId) {
        StpUtil.logoutByTokenValue(sessionId);
    }
    
    /**
     * 批量踢出用户
     */
    public void batchForceLogout(List<String> sessionIds) {
        for (String sessionId : sessionIds) {
            forceLogout(sessionId);
        }
    }
    
    /**
     * 根据用户ID踢出用户
     */
    public void forceLogoutByUserId(Long userId) {
        StpUtil.kickout(userId);
    }
}
```

#### 步骤 8.2：更新在线用户控制器

**文件**: `ruoyi-admin/src/main/java/com/ruoyi/web/controller/monitor/SysUserOnlineController.java`

**操作**: 修改为使用Sa-Token服务：
```java
package com.ruoyi.web.controller.monitor;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.framework.satoken.service.SaTokenOnlineService;
import com.ruoyi.system.domain.SysUserOnline;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 在线用户监控
 */
@RestController
@RequestMapping("/monitor/online")
public class SysUserOnlineController extends BaseController {
    
    @Autowired
    private SaTokenOnlineService onlineService;
    
    @SaCheckPermission("monitor:online:list")
    @GetMapping("/list")
    public TableDataInfo list(SysUserOnline userOnline) {
        List<SysUserOnline> list = onlineService.selectOnlineUserList();
        return getDataTable(list);
    }
    
    @SaCheckPermission("monitor:online:forceLogout")
    @PostMapping("/forceLogout")
    public AjaxResult forceLogout(@RequestBody String sessionId) {
        onlineService.forceLogout(sessionId);
        return AjaxResult.success();
    }
    
    @SaCheckPermission("monitor:online:batchForceLogout")
    @PostMapping("/batchForceLogout")
    public AjaxResult batchForceLogout(@RequestBody List<String> sessionIds) {
        onlineService.batchForceLogout(sessionIds);
        return AjaxResult.success();
    }
}
```

---

### 阶段九：密码服务迁移

#### 步骤 9.1：创建 Sa-Token 密码服务

**新建文件**: `ruoyi-framework/src/main/java/com/ruoyi/framework/satoken/service/SaTokenPasswordService.java`

```java
package com.ruoyi.framework.satoken.service;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.exception.user.UserPasswordNotMatchException;
import com.ruoyi.common.exception.user.UserPasswordRetryLimitExceedException;
import com.ruoyi.common.utils.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Sa-Token 密码验证服务
 */
@Component
public class SaTokenPasswordService {
    
    @Value("${user.password.maxRetryCount}")
    private int maxRetryCount;
    
    private static final String LOGIN_RECORD_CACHE = "loginRecordCache";
    
    /**
     * 验证密码
     */
    public void validate(SysUser user, String password) {
        String loginName = user.getLoginName();
        
        // 获取缓存中的登录记录
        SaSession session = StpUtil.getSessionByLoginId(loginName, false);
        Integer retryCount = 0;
        
        if (session != null) {
            retryCount = (Integer) session.get(LOGIN_RECORD_CACHE);
            if (retryCount == null) {
                retryCount = 0;
            }
        }
        
        if (retryCount >= maxRetryCount) {
            throw new UserPasswordRetryLimitExceedException(maxRetryCount);
        }
        
        // 验证密码
        if (!matches(user, password)) {
            retryCount++;
            
            // 更新重试次数
            if (session == null) {
                session = StpUtil.getSessionByLoginId(loginName, true);
            }
            session.set(LOGIN_RECORD_CACHE, retryCount);
            session.setTimeout(600); // 10分钟过期
            
            throw new UserPasswordNotMatchException();
        } else {
            // 密码正确，清除重试记录
            if (session != null) {
                session.delete(LOGIN_RECORD_CACHE);
            }
        }
    }
    
    /**
     * 密码匹配
     */
    public boolean matches(SysUser user, String rawPassword) {
        String encryptPassword = encryptPassword(user.getLoginName(), rawPassword, user.getSalt());
        return user.getPassword().equals(encryptPassword);
    }
    
    /**
     * 加密密码
     */
    public String encryptPassword(String username, String password, String salt) {
        return SaSecureUtil.md5(username + password + salt);
    }
}
```

---

