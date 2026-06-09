## 📝 详细迁移步骤（AI友好版）

### 阶段一：准备工作和依赖替换

#### 步骤 1.1：添加 Sa-Token 依赖

**文件**: `pom.xml` (根目录)

**操作**: 在 `<properties>` 中添加版本号：
```xml
<sa-token.version>1.39.0</sa-token.version>
```

**操作**: 在 `<dependencyManagement>` 中添加依赖：
```xml
<!-- Sa-Token 核心 -->
<dependency>
    <groupId>cn.dev33</groupId>
    <artifactId>sa-token-spring-boot3-starter</artifactId>
    <version>${sa-token.version}</version>
</dependency>

<!-- Sa-Token 整合 Redis （使用 jackson 序列化） -->
<dependency>
    <groupId>cn.dev33</groupId>
    <artifactId>sa-token-redis-jackson</artifactId>
    <version>${sa-token.version}</version>
</dependency>

<!-- Sa-Token 整合 Thymeleaf -->
<dependency>
    <groupId>cn.dev33</groupId>
    <artifactId>sa-token-dialect-thymeleaf</artifactId>
    <version>${sa-token.version}</version>
</dependency>
```

#### 步骤 1.2：添加 Redis 依赖（可选，推荐）

**文件**: `pom.xml` (根目录)

**操作**: 在 `<dependencyManagement>` 中添加：
```xml
<!-- Redis -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

#### 步骤 1.3：更新模块依赖

**文件**: `ruoyi-framework/pom.xml`

**操作**: 删除以下依赖：
```xml
<!-- 删除这些 -->
<dependency>
    <groupId>org.apache.shiro</groupId>
    <artifactId>shiro-spring</artifactId>
</dependency>
<dependency>
    <groupId>org.apache.shiro</groupId>
    <artifactId>shiro-core</artifactId>
</dependency>
<dependency>
    <groupId>org.apache.shiro</groupId>
    <artifactId>shiro-web</artifactId>
</dependency>
<dependency>
    <groupId>com.github.theborakompanioni</groupId>
    <artifactId>thymeleaf-extras-shiro</artifactId>
</dependency>
```

**操作**: 添加以下依赖：
```xml
<!-- 添加这些 -->
<dependency>
    <groupId>cn.dev33</groupId>
    <artifactId>sa-token-spring-boot3-starter</artifactId>
</dependency>
<dependency>
    <groupId>cn.dev33</groupId>
    <artifactId>sa-token-redis-jackson</artifactId>
</dependency>
<dependency>
    <groupId>cn.dev33</groupId>
    <artifactId>sa-token-dialect-thymeleaf</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

**文件**: `ruoyi-common/pom.xml`

**操作**: 删除Shiro依赖，保持EhCache（如果需要）或移除：
```xml
<!-- 删除 -->
<dependency>
    <groupId>org.apache.shiro</groupId>
    <artifactId>shiro-core</artifactId>
</dependency>
<dependency>
    <groupId>org.apache.shiro</groupId>
    <artifactId>shiro-ehcache</artifactId>
</dependency>
```

---

### 阶段二：配置类迁移

#### 步骤 2.1：创建 Sa-Token 配置类

**新建文件**: `ruoyi-framework/src/main/java/com/ruoyi/framework/config/SaTokenConfig.java`

```java
package com.ruoyi.framework.config;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.filter.SaServletFilter;
import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import com.ruoyi.framework.config.properties.PermitAllUrlProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SaTokenConfig implements WebMvcConfigurer {
    
    @Autowired
    private PermitAllUrlProperties permitAllUrl;
    
    // 注册 Sa-Token 拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SaInterceptor(handle -> {
            // 登录校验 -- 拦截所有路由
            SaRouter.match("/**")
                .notMatch("/login", "/register", "/captcha/**")
                .notMatch("/favicon.ico", "/ruoyi.png", "/html/**", "/css/**")
                .notMatch("/docs/**", "/fonts/**", "/img/**", "/ajax/**")
                .notMatch("/js/**", "/ruoyi/**")
                .notMatch(permitAllUrl.getUrls())
                .check(r -> StpUtil.checkLogin());
        })).addPathPatterns("/**");
    }
}
```

#### 步骤 2.2：创建配置文件

**新建文件**: `ruoyi-admin/src/main/resources/application-satoken.yml`

```yaml
############## Sa-Token 配置 ###############
sa-token:
  # token 名称（同时也是 cookie 名称）
  token-name: Authorization
  # token 有效期（单位：秒） 默认30天，-1 代表永久有效
  timeout: 1800
  # token 最低活跃频率（单位：秒），如果 token 超过此时间没有访问系统就会被冻结，默认-1 代表不限制，永不冻结
  active-timeout: -1
  # 是否允许同一账号多地同时登录 （为 true 时允许一起登录, 为 false 时新登录挤掉旧登录）
  is-concurrent: true
  # 在多人登录同一账号时，是否共用一个 token （为 true 时所有登录共用一个 token, 为 false 时每次登录新建一个 token）
  is-share: false
  # token 风格（默认可取值：uuid、simple-uuid、random-32、random-64、random-128、tik）
  token-style: uuid
  # 是否输出操作日志
  is-log: true
  # 是否从 cookie 中读取 token
  is-read-cookie: true
  # 是否从 head 中读取 token
  is-read-header: true
  # token前缀
  token-prefix: Bearer
  # 是否在初始化配置时打印版本字符画
  is-print: false
```

#### 步骤 2.3：更新主配置文件

**文件**: `ruoyi-admin/src/main/resources/application.yml`

**操作**: 删除 Shiro 配置块（86-123行），替换为：
```yaml
# 引入 Sa-Token 配置
spring:
  profiles:
    include: satoken
```

---

