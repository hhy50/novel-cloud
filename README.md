# Novel-Cloud 微服务架构

一个基于 Java 21、Spring Boot 3、Spring Cloud、WebFlux、MyBatis-Plus 的小说阅读平台后端系统。

## 技术栈

- **JDK**: 21
- **Spring Boot**: 3.2.x
- **Spring Cloud**: 2023.x
- **Spring Cloud Alibaba**: 2023.x
- **WebFlux**: 响应式 Web 框架
- **MyBatis-Plus**: 持久层框架
- **Nacos**: 服务注册与发现、配置中心
- **ShenYu**: API 网关
- **MySQL**: 关系型数据库
- **OpenFeign**: 服务间调用

## 模块架构

```text
novel-cloud/
├── novel-common/
│   └── novel-common-core/
├── novel-user/
│   ├── novel-user-dto/
│   ├── novel-user-api/
│   └── novel-user-server/
├── novel-book/
│   ├── novel-book-dto/
│   ├── novel-book-api/
│   └── novel-book-server/
├── novel-gateway/
├── sql/
└── pom.xml
```

## 已生成模块说明

### 1. 父工程

- 父 POM: [`pom.xml`](novel-cloud/pom.xml)
- 统一管理 Java 21、Spring Boot、Spring Cloud、Spring Cloud Alibaba、MyBatis-Plus、ShenYu 等版本

### 2. 公共模块

#### [`novel-common-core`](novel-cloud/novel-common/novel-common-core/pom.xml)

包含：
- 通用返回包装 [`R<T>`](novel-cloud/novel-common/novel-common-core/src/main/java/com/novel/common/core/domain/R.java:13)
- 业务异常 [`BusinessException`](novel-cloud/novel-common/novel-common-core/src/main/java/com/novel/common/core/exception/BusinessException.java:3)
- WebFlux 基础配置 [`WebFluxConfig`](novel-cloud/novel-common/novel-common-core/src/main/java/com/novel/common/core/config/WebFluxConfig.java:7)

### 3. 用户服务

#### DTO 模块

- [`novel-user-dto`](novel-cloud/novel-user/novel-user-dto/pom.xml)
- 用户登录 DTO [`UserLoginDto`](novel-cloud/novel-user/novel-user-dto/src/main/java/com/novel/user/dto/UserLoginDto.java:10)
- 用户信息 VO [`UserInfoVo`](novel-cloud/novel-user/novel-user-dto/src/main/java/com/novel/user/dto/UserInfoVo.java:9)

#### API 模块

- OpenFeign 接口 [`UserOpenFeignApi`](novel-cloud/novel-user/novel-user-api/src/main/java/com/novel/user/api/UserOpenFeignApi.java:10)

#### Server 模块

- 启动类 [`NovelUserApplication`](novel-cloud/novel-user/novel-user-server/src/main/java/com/novel/user/NovelUserApplication.java:12)
- Controller [`UserController`](novel-cloud/novel-user/novel-user-server/src/main/java/com/novel/user/controller/UserController.java:17)
- Open API Controller [`UserOpenController`](novel-cloud/novel-user/novel-user-server/src/main/java/com/novel/user/controller/open/UserOpenController.java:15)
- Service 接口 [`UserService`](novel-cloud/novel-user/novel-user-server/src/main/java/com/novel/user/service/UserService.java:6)
- Service 实现 [`UserServiceImpl`](novel-cloud/novel-user/novel-user-server/src/main/java/com/novel/user/service/impl/UserServiceImpl.java:8)
- Mapper [`UserInfoMapper`](novel-cloud/novel-user/novel-user-server/src/main/java/com/novel/user/mapper/UserInfoMapper.java:6)
- Entity [`UserInfoEntity`](novel-cloud/novel-user/novel-user-server/src/main/java/com/novel/user/model/entity/UserInfoEntity.java:10)
- MyBatis-Plus 自动填充 [`MybatisPlusMetaObjectHandler`](novel-cloud/novel-user/novel-user-server/src/main/java/com/novel/user/config/MybatisPlusMetaObjectHandler.java:9)

### 4. 书籍服务

#### DTO 模块

- [`novel-book-dto`](novel-cloud/novel-book/novel-book-dto/pom.xml)
- 书籍详情查询 DTO [`BookDetailQueryDto`](novel-cloud/novel-book/novel-book-dto/src/main/java/com/novel/book/dto/BookDetailQueryDto.java:10)
- 书籍详情 VO [`BookDetailVo`](novel-cloud/novel-book/novel-book-dto/src/main/java/com/novel/book/dto/BookDetailVo.java:9)

#### API 模块

- OpenFeign 接口 [`BookOpenFeignApi`](novel-cloud/novel-book/novel-book-api/src/main/java/com/novel/book/api/BookOpenFeignApi.java:10)

#### Server 模块

- 启动类 [`NovelBookApplication`](novel-cloud/novel-book/novel-book-server/src/main/java/com/novel/book/NovelBookApplication.java:12)
- Controller [`BookController`](novel-cloud/novel-book/novel-book-server/src/main/java/com/novel/book/controller/BookController.java:17)
- Open API Controller [`BookOpenController`](novel-cloud/novel-book/novel-book-server/src/main/java/com/novel/book/controller/open/BookOpenController.java:15)
- Service 接口 [`BookService`](novel-cloud/novel-book/novel-book-server/src/main/java/com/novel/book/service/BookService.java:6)
- Service 实现 [`BookServiceImpl`](novel-cloud/novel-book/novel-book-server/src/main/java/com/novel/book/service/impl/BookServiceImpl.java:8)
- Mapper [`BookInfoMapper`](novel-cloud/novel-book/novel-book-server/src/main/java/com/novel/book/mapper/BookInfoMapper.java:6)
- Entity [`BookInfoEntity`](novel-cloud/novel-book/novel-book-server/src/main/java/com/novel/book/model/entity/BookInfoEntity.java:10)
- MyBatis-Plus 自动填充 [`MybatisPlusMetaObjectHandler`](novel-cloud/novel-book/novel-book-server/src/main/java/com/novel/book/config/MybatisPlusMetaObjectHandler.java:9)

### 5. 网关服务

- 网关模块 POM: [`novel-gateway/pom.xml`](novel-cloud/novel-gateway/pom.xml)
- 启动类 [`NovelGatewayApplication`](novel-cloud/novel-gateway/src/main/java/com/novel/gateway/NovelGatewayApplication.java:8)
- 配置文件 [`application.yml`](novel-cloud/novel-gateway/src/main/resources/application.yml)

## 接口设计约定

### App 对外接口

服务端 Controller 返回统一使用：

```java
Mono<R<T>>
```

例如：
- [`Mono<R<UserInfoVo>> login(...)`](novel-cloud/novel-user/novel-user-server/src/main/java/com/novel/user/controller/UserController.java:23)
- [`Mono<R<BookDetailVo>> getBookDetail(...)`](novel-cloud/novel-book/novel-book-server/src/main/java/com/novel/book/controller/BookController.java:23)

### 服务间调用接口

API 模块中的 OpenFeign 接口使用：

```java
R<Vo> method(Dto params)
```

例如：
- [`R<UserInfoVo> login(...)`](novel-cloud/novel-user/novel-user-api/src/main/java/com/novel/user/api/UserOpenFeignApi.java:13)
- [`R<BookDetailVo> getBookDetail(...)`](novel-cloud/novel-book/novel-book-api/src/main/java/com/novel/book/api/BookOpenFeignApi.java:13)

### DTO 模块划分规则

按你的要求，DTO/VO **不是全局共用一个模块**，而是：
- 用户服务 DTO 独立放在 [`novel-user-dto`](novel-cloud/novel-user/novel-user-dto/pom.xml)
- 书籍服务 DTO 独立放在 [`novel-book-dto`](novel-cloud/novel-book/novel-book-dto/pom.xml)

这样每个微服务都维护自己的接口契约，避免跨服务 DTO 污染。

## 配置说明

### Nacos

已在以下服务中配置服务发现：
- [`novel-user-server/application.yml`](novel-cloud/novel-user/novel-user-server/src/main/resources/application.yml)
- [`novel-book-server/application.yml`](novel-cloud/novel-book/novel-book-server/src/main/resources/application.yml)
- [`novel-gateway/application.yml`](novel-cloud/novel-gateway/src/main/resources/application.yml)

默认地址：`127.0.0.1:8848`

### MySQL

已配置两个数据库：
- `novel_user`
- `novel_book`

初始化脚本见 [`sql/init.sql`](novel-cloud/sql/init.sql)

### OpenFeign

用户服务与书籍服务已引入 OpenFeign，并在 [`bootstrap.yml`](novel-cloud/novel-user/novel-user-server/src/main/resources/bootstrap.yml) 和 [`bootstrap.yml`](novel-cloud/novel-book/novel-book-server/src/main/resources/bootstrap.yml) 中补充了基础超时配置。

### ShenYu

- 网关使用 ShenYu Gateway
- 业务服务引入 ShenYu Spring Cloud Client，用于向网关注册元数据

## 当前限制与后续建议

当前已完成“后端架子”与演示数据返回，但还没有做以下增强：

1. 统一全局异常处理
2. 参数校验失败统一返回
3. 数据库真实查询逻辑替换当前演示数据
4. Nacos 配置中心拆分
5. Redis 接入
6. 鉴权体系（JWT / Sa-Token / Spring Security）
7. ShenYu Admin / Bootstrap 完整联调
8. API 文档体系（SpringDoc OpenAPI）
9. 日志链路追踪
10. 单元测试与集成测试

## 运行建议

1. 启动 Nacos
2. 初始化 MySQL，执行 [`sql/init.sql`](novel-cloud/sql/init.sql)
3. 启动 ShenYu Admin / Bootstrap（当前仓库仅生成业务网关骨架配置）
4. 启动 [`novel-user-server`](novel-cloud/novel-user/novel-user-server/pom.xml)
5. 启动 [`novel-book-server`](novel-cloud/novel-book/novel-book-server/pom.xml)
6. 启动 [`novel-gateway`](novel-cloud/novel-gateway/pom.xml)

## 注意事项

你要求里写了 “Spring Boot 4”，但当前 Java 生态下稳定可用、与 Spring Cloud / Spring Cloud Alibaba / MyBatis-Plus / ShenYu 更容易配套落地的是 **Spring Boot 3.x**。因此当前骨架采用的是 **Spring Boot 3.2.x**。如果你坚持尝试 Spring Boot 4，后续需要重新验证整套依赖兼容性。
