### 阶段五：控制器层迁移

#### 步骤 5.1：注解替换规则

**全局替换操作**（使用IDE的全局替换功能）：

1. **导入语句替换**：
```java
// 查找
import org.apache.shiro.authz.annotation.RequiresPermissions;

// 替换为
import cn.dev33.satoken.annotation.SaCheckPermission;
```

```java
// 查找
import org.apache.shiro.authz.annotation.RequiresRoles;

// 替换为
import cn.dev33.satoken.annotation.SaCheckRole;
```

```java
// 查找
import org.apache.shiro.authz.annotation.Logical;

// 替换为
import cn.dev33.satoken.annotation.SaMode;
```

2. **注解替换**：
```java
// 查找
@RequiresPermissions

// 替换为
@SaCheckPermission
```

```java
// 查找
@RequiresRoles

// 替换为
@SaCheckRole
```

```java
// 查找
Logical.AND

// 替换为
SaMode.AND
```

```java
// 查找
Logical.OR

// 替换为
SaMode.OR
```

#### 步骤 5.2：登录控制器改造

**文件**: `ruoyi-admin/src/main/java/com/ruoyi/web/controller/system/SysLoginController.java`

**操作**: 修改登录逻辑：
```java
package com.ruoyi.web.controller.system;

import cn.dev33.satoken.stp.StpUtil;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.SaTokenUtils;
import com.ruoyi.framework.satoken.service.SaTokenLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 登录控制器
 */
@RestController
public class SysLoginController extends BaseController {
    
    @Autowired
    private SaTokenLoginService loginService;
    
    /**
     * 登录方法
     */
    @PostMapping("/login")
    public AjaxResult login(@RequestParam String username, 
                           @RequestParam String password,
                           @RequestParam(required = false) Boolean rememberMe) {
        // 执行登录
        SysUser user = loginService.login(username, password);
        
        // 获取token
        String token = StpUtil.getTokenValue();
        
        return AjaxResult.success()
            .put("token", token)
            .put("user", user);
    }
    
    /**
     * 登出方法
     */
    @PostMapping("/logout")
    public AjaxResult logout() {
        StpUtil.logout();
        return AjaxResult.success("退出成功");
    }
    
    /**
     * 获取用户信息
     */
    @GetMapping("/getInfo")
    public AjaxResult getInfo() {
        SysUser user = SaTokenUtils.getSysUser();
        // 角色权限信息
        Set<String> roles = new HashSet<>();
        Set<String> permissions = new HashSet<>();
        
        if (user.isAdmin()) {
            roles.add("admin");
            permissions.add("*:*:*");
        } else {
            roles = roleService.selectRoleKeys(user.getUserId());
            permissions = menuService.selectPermsByUserId(user.getUserId());
        }
        
        return AjaxResult.success()
            .put("user", user)
            .put("roles", roles)
            .put("permissions", permissions);
    }
}
```

#### 步骤 5.3：批量更新所有Controller

**影响的Controller文件**（30+个）：
- `SysUserController.java`
- `SysRoleController.java`
- `SysMenuController.java`
- `SysDeptController.java`
- `SysPostController.java`
- `SysConfigController.java`
- `SysDictDataController.java`
- `SysDictTypeController.java`
- `SysNoticeController.java`
- `SysOperlogController.java`
- `SysLogininforController.java`
- `SysUserOnlineController.java`
- `SysJobController.java`
- `SysJobLogController.java`
- 等等...

**示例改造**（以SysUserController为例）：

**原代码**:
```java
import org.apache.shiro.authz.annotation.RequiresPermissions;

@RequiresPermissions("system:user:view")
public String user() {
    return prefix + "/user";
}

@RequiresPermissions("system:user:list")
@PostMapping("/list")
public TableDataInfo list(SysUser user) {
    // ...
}
```

**改造后**:
```java
import cn.dev33.satoken.annotation.SaCheckPermission;

@SaCheckPermission("system:user:view")
public String user() {
    return prefix + "/user";
}

@SaCheckPermission("system:user:list")
@PostMapping("/list")
public TableDataInfo list(SysUser user) {
    // ...
}
```

---

### 阶段六：权限服务迁移

#### 步骤 6.1：创建 Sa-Token 权限服务

**新建文件**: `ruoyi-framework/src/main/java/com/ruoyi/framework/satoken/service/SaTokenPermissionService.java`

```java
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
 */
@Component
public class SaTokenPermissionService implements StpInterface {
    
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
```

#### 步骤 6.2：更新 PermissionService

**文件**: `ruoyi-framework/src/main/java/com/ruoyi/framework/web/service/PermissionService.java`

**操作**: 修改为使用 Sa-Token API：
```java
package com.ruoyi.framework.web.service;

import cn.dev33.satoken.stp.StpUtil;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.SaTokenUtils;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Set;

/**
 * 自定义权限验证服务
 */
@Service("ss")
public class PermissionService {
    
    /** 所有权限标识 */
    private static final String ALL_PERMISSION = "*:*:*";
    
    /** 管理员角色权限标识 */
    private static final String SUPER_ADMIN = "admin";
    
    /**
     * 验证用户是否具备某权限
     */
    public boolean hasPermi(String permission) {
        if (StringUtils.isEmpty(permission)) {
            return false;
        }
        
        SysUser user = SaTokenUtils.getSysUser();
        if (user == null || CollectionUtils.isEmpty(user.getRoles())) {
            return false;
        }
        
        return hasPermissions(user.getRoles(), permission);
    }
    
    /**
     * 验证用户是否具有某个角色
     */
    public boolean hasRole(String role) {
        if (StringUtils.isEmpty(role)) {
            return false;
        }
        
        SysUser user = SaTokenUtils.getSysUser();
        if (user == null || CollectionUtils.isEmpty(user.getRoles())) {
            return false;
        }
        
        for (SysRole sysRole : user.getRoles()) {
            String roleKey = sysRole.getRoleKey();
            if (SUPER_ADMIN.equals(roleKey) || roleKey.equals(role)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * 判断用户是否拥有某个权限
     */
    private boolean hasPermissions(Set<SysRole> roles, String permission) {
        // 管理员拥有所有权限
        if (roles.stream().anyMatch(r -> SUPER_ADMIN.equals(r.getRoleKey()))) {
            return true;
        }
        
        // 检查具体权限
        for (SysRole role : roles) {
            Set<String> permissions = role.getPermissions();
            if (permissions != null && 
                (permissions.contains(ALL_PERMISSION) || permissions.contains(permission))) {
                return true;
            }
        }
        
        return false;
    }
}
```

---

