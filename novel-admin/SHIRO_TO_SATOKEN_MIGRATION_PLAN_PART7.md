### 阶段十四：优化建议

#### 步骤 14.1：Redis 集成（推荐）

**添加Redis配置**: `application-redis.yml`

```yaml
spring:
  redis:
    host: localhost
    port: 6379
    password: 
    database: 0
    timeout: 10s
    lettuce:
      pool:
        min-idle: 0
        max-idle: 8
        max-active: 8
        max-wait: -1ms

# Sa-Token 使用 Redis 存储
sa-token:
  # 是否尝试从请求体里读取 token
  is-read-body: false
  # 是否尝试从 header 里读取 token
  is-read-header: true
  # token前缀
  token-prefix: "Bearer"
```

#### 步骤 14.2：性能优化配置

**优化Sa-Token配置**:
```yaml
sa-token:
  # JWT风格token（可选）
  token-style: simple-uuid
  
  # 关闭活跃检测（提升性能）
  active-timeout: -1
  
  # 启用二级缓存（需要配置）
  is-cache: true
  
  # 关闭不必要的日志
  is-log: false
  
  # 使用Redis持久化Session
  alone-redis:
    # 使用独立Redis连接池（高并发推荐）
    host: localhost
    port: 6379
    database: 1
```

#### 步骤 14.3：安全加固

**添加安全配置**:
```java
@Configuration
public class SaTokenSecurityConfig {
    
    /**
     * 注册 Sa-Token 全局过滤器
     */
    @Bean
    public SaServletFilter getSaServletFilter() {
        return new SaServletFilter()
            // 指定拦截路由
            .addInclude("/**")
            // 指定放行路由
            .addExclude("/favicon.ico", "/error")
            
            // 认证函数: 每次请求执行
            .setAuth(obj -> {
                // 强制登录校验
                SaRouter.match("/**")
                    .notMatch("/login", "/register", "/captcha/**")
                    .check(r -> StpUtil.checkLogin());
            })
            
            // 异常处理函数
            .setError(e -> {
                return AjaxResult.error(e.getMessage());
            })
            
            // 前置函数：在每次认证函数之前执行
            .setBeforeAuth(obj -> {
                // 设置跨域响应头
                SaHolder.getResponse()
                    .setHeader("Access-Control-Allow-Origin", "*")
                    .setHeader("Access-Control-Allow-Methods", "*")
                    .setHeader("Access-Control-Allow-Headers", "*")
                    .setHeader("Access-Control-Max-Age", "3600");
            });
    }
}
```

---

## 📊 工作量评估

### 时间预估（按阶段）

| 阶段 | 工作内容 | 预估时间 | 难度 |
|-----|---------|---------|------|
| 阶段一 | 依赖替换 | 0.5天 | ⭐ |
| 阶段二 | 配置类迁移 | 1天 | ⭐⭐⭐ |
| 阶段三 | 工具类迁移 | 0.5天 | ⭐⭐ |
| 阶段四 | 业务服务迁移 | 1天 | ⭐⭐⭐ |
| 阶段五 | 控制器层迁移 | 1.5天 | ⭐⭐ |
| 阶段六 | 权限服务迁移 | 1天 | ⭐⭐⭐⭐ |
| 阶段七 | 模板文件迁移 | 1天 | ⭐⭐ |
| 阶段八 | 在线用户管理 | 1天 | ⭐⭐⭐ |
| 阶段九 | 密码服务迁移 | 0.5天 | ⭐⭐ |
| 阶段十 | 异常处理迁移 | 0.5天 | ⭐⭐ |
| 阶段十一 | 删除旧代码 | 0.5天 | ⭐ |
| 阶段十二 | 测试验证 | 2天 | ⭐⭐⭐⭐ |
| 阶段十三 | 优化调整 | 1天 | ⭐⭐⭐ |
| **总计** | **全部工作** | **12天** | - |

### 人员配置建议

- **后端开发**: 2人
- **前端开发**: 1人  
- **测试人员**: 1人
- **技术负责人**: 1人

---

## ⚠️ 重要注意事项

### 1. 兼容性问题

**Session存储方式变更**：
- Shiro使用自己的Session管理
- Sa-Token使用SaSession
- 需要迁移现有在线用户数据

**权限缓存机制不同**：
- Shiro使用EhCache
- Sa-Token建议使用Redis
- 需要清理旧缓存数据

### 2. 配置项差异

**Shiro配置项** → **Sa-Token对应**：
```yaml
# Shiro
shiro.session.expireTime: 30  # 分钟

# Sa-Token
sa-token.timeout: 1800  # 秒（30*60）
```

### 3. API差异对照

| Shiro | Sa-Token | 说明 |
|-------|----------|------|
| `Subject.login(token)` | `StpUtil.login(id)` | 登录方式不同 |
| `Subject.logout()` | `StpUtil.logout()` | 登出 |
| `Subject.getPrincipal()` | `StpUtil.getLoginId()` | 获取用户标识 |
| `Subject.getSession()` | `StpUtil.getSession()` | 获取Session |
| `Subject.hasRole()` | `StpUtil.hasRole()` | 角色判断 |
| `SecurityUtils.getSubject()` | `StpUtil` 直接调用 | 无需获取Subject |

### 4. 迁移风险

**高风险项**：
- ⚠️ 在线用户会全部掉线（需要重新登录）
- ⚠️ 记住我Cookie会失效
- ⚠️ 原有Session数据会丢失

**缓解措施**：
- 在低峰期进行迁移
- 提前通知用户
- 准备快速回滚方案

---

## 🎯 快速参考手册

### 常用API对照表

```java
// ==================== 登录相关 ====================
// Shiro
Subject subject = SecurityUtils.getSubject();
UsernamePasswordToken token = new UsernamePasswordToken(username, password);
subject.login(token);

// Sa-Token
StpUtil.login(userId);


// ==================== 获取当前用户 ====================
// Shiro
SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
Long userId = ShiroUtils.getUserId();

// Sa-Token
Long userId = SaTokenUtils.getUserId();
SysUser user = SaTokenUtils.getSysUser();


// ==================== 权限判断 ====================
// Shiro
SecurityUtils.getSubject().isPermitted("system:user:list");
SecurityUtils.getSubject().checkPermission("system:user:add");

// Sa-Token
StpUtil.hasPermission("system:user:list");
StpUtil.checkPermission("system:user:add");


// ==================== 角色判断 ====================
// Shiro
SecurityUtils.getSubject().hasRole("admin");
SecurityUtils.getSubject().checkRole("admin");

// Sa-Token
StpUtil.hasRole("admin");
StpUtil.checkRole("admin");


// ==================== Session操作 ====================
// Shiro
Session session = SecurityUtils.getSubject().getSession();
session.setAttribute("key", value);
Object value = session.getAttribute("key");

// Sa-Token
SaSession session = StpUtil.getSession();
session.set("key", value);
Object value = session.get("key");


// ==================== 登出 ====================
// Shiro
SecurityUtils.getSubject().logout();

// Sa-Token
StpUtil.logout();


// ==================== 踢人下线 ====================
// Shiro (需要自己实现)
sessionManager.getActiveSessions().remove(sessionId);

// Sa-Token
StpUtil.kickout(userId);           // 踢出指定用户
StpUtil.kickoutByTokenValue(token); // 踢出指定token
```

### 注解对照表

```java
// ==================== Controller注解 ====================
// Shiro
@RequiresPermissions("system:user:list")
@RequiresRoles("admin")
@RequiresPermissions(value = {"system:user:add", "system:user:edit"}, logical = Logical.OR)

// Sa-Token
@SaCheckPermission("system:user:list")
@SaCheckRole("admin")
@SaCheckPermission(value = {"system:user:add", "system:user:edit"}, mode = SaMode.OR)
```

### Thymeleaf标签对照表

```html
<!-- Shiro -->
<div shiro:hasPermission="system:user:add">有权限才显示</div>
<div shiro:lacksPermission="system:user:add">无权限才显示</div>
<div shiro:hasRole="admin">有角色才显示</div>
<div shiro:lacksRole="admin">无角色才显示</div>

<!-- Sa-Token -->
<div sa:permission="system:user:add">有权限才显示</div>
<div sa:notPermission="system:user:add">无权限才显示</div>
<div sa:role="admin">有角色才显示</div>
<div sa:notRole="admin">无角色才显示</div>
```

---

## 📚 参考资源

### 官方文档
- **Sa-Token官网**: https://sa-token.cc/
- **Sa-Token文档**: https://sa-token.cc/doc.html
- **Sa-Token GitHub**: https://github.com/dromara/sa-token
- **Spring Boot整合**: https://sa-token.cc/doc.html#/start/example

### 示例代码
- **官方示例**: https://github.com/dromara/sa-token/tree/master/sa-token-demo
- **RuoYi-Vue-Plus**: https://gitee.com/dromara/RuoYi-Vue-Plus (已集成Sa-Token)

### 社区支持
- **Gitee Issues**: https://gitee.com/dromara/sa-token/issues
- **开发者社群**: Sa-Token官网获取

---

