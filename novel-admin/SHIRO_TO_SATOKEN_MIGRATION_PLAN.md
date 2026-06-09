# Shiro 迁移到 Sa-Token 方案

## 📋 项目概述

**项目名称**: RuoYi 管理系统  
**当前版本**: 4.8.3  
**当前认证框架**: Apache Shiro 2.1.0  
**目标认证框架**: Sa-Token  
**Spring Boot版本**: 4.0.3  
**Java版本**: 17

---

## 🎯 迁移目标

将若依管理系统从 Apache Shiro 完全迁移到 Sa-Token，保持所有现有功能不变，提升性能和可维护性。

---

## 📊 当前 Shiro 使用情况分析

### 1. Maven 依赖（4个）
```xml
<!-- 父 pom.xml -->
- org.apache.shiro:shiro-core:2.1.0 (jakarta)
- org.apache.shiro:shiro-web:2.1.0 (jakarta)
- org.apache.shiro:shiro-spring:2.1.0 (jakarta)
- org.apache.shiro:shiro-ehcache:2.1.0
- com.github.theborakompanioni:thymeleaf-extras-shiro:2.1.0
```

### 2. 核心配置类（1个）
- `ShiroConfig.java` - 600+ 行的核心配置

### 3. 核心功能模块（19个Java文件）

#### Realm（认证授权）
- `UserRealm.java` - 自定义Realm，处理登录认证和权限授权

#### Session管理（4个）
- `OnlineSessionDAO.java` - 在线会话DAO
- `OnlineSessionFactory.java` - 会话工厂
- `OnlineWebSessionManager.java` - Web会话管理器
- `SpringSessionValidationScheduler.java` - 会话验证调度器

#### 业务服务（4个）
- `SysLoginService.java` - 登录服务
- `SysPasswordService.java` - 密码服务
- `SysRegisterService.java` - 注册服务
- `SysShiroService.java` - Shiro服务

#### 过滤器（6个）
- `LogoutFilter.java` - 登出过滤器
- `CaptchaValidateFilter.java` - 验证码过滤器
- `CsrfValidateFilter.java` - CSRF防护过滤器
- `KickoutSessionFilter.java` - 踢出会话过滤器
- `OnlineSessionFilter.java` - 在线会话过滤器
- `SyncOnlineSessionFilter.java` - 同步在线会话过滤器

#### 其他组件（4个）
- `CustomCookieRememberMeManager.java` - 记住我管理器
- `CustomShiroFilterFactoryBean.java` - 自定义过滤器工厂
- `AuthorizationUtils.java` - 权限工具类
- `ShiroUtils.java` - Shiro工具类

### 4. 核心常量类（1个）
- `ShiroConstants.java` - 84行常量定义

### 5. 配置文件
- `application.yml` - Shiro相关配置（20+配置项）
- `ehcache-shiro.xml` - EhCache缓存配置

### 6. 控制器使用（30+个Controller）
- 使用 `@RequiresPermissions` 注解（权限控制）
- 使用 `@RequiresRoles` 注解（角色控制）
- 使用 `ShiroUtils` 获取当前用户信息

### 7. 模板文件（100+处）
- 使用 `shiro:hasPermission` 标签（按钮级权限控制）
- 前端页面大量使用Shiro标签进行权限控制

---

## 🔄 Sa-Token 对应功能映射

| Shiro 功能 | Sa-Token 对应方案 |
|-----------|-----------------|
| `@RequiresPermissions` | `@SaCheckPermission` |
| `@RequiresRoles` | `@SaCheckRole` |
| `Subject.login()` | `StpUtil.login()` |
| `Subject.logout()` | `StpUtil.logout()` |
| `Subject.getPrincipal()` | `StpUtil.getLoginId()` + Session存储 |
| `SecurityUtils.getSubject()` | `StpUtil` 静态方法 |
| `Session` 管理 | `SaSession` |
| Remember Me | Sa-Token内置支持 |
| 权限缓存 | Sa-Token内置Redis/内存缓存 |
| 在线用户管理 | `SaSession` 查询 |
| 踢人下线 | `StpUtil.kickout()` |
| Thymeleaf标签 | `sa-token-dialect-thymeleaf` |

---

