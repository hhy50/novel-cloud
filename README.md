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
- **MySQL**: 关系型数据库
- **OpenFeign**: 服务间调用
- **Sa-Token**: 登录态管理与 JWT Token 签发

## 模块架构

```text
novel-cloud/
├── novel-common/
│   └── novel-common-core/
├── novel-user/
│   ├── novel-user-dto/
│   ├── novel-user-api/
│   └── novel-user-service/
├── novel-book/
│   ├── novel-book-dto/
│   ├── novel-book-api/
│   └── novel-book-service/
├── novel-subscribe/
│   ├── novel-subscribe-dto/
│   ├── novel-subscribe-api/
│   └── novel-subscribe-service/
├── novel-payment/
│   ├── novel-payment-dto/
│   ├── novel-payment-api/
│   └── novel-payment-service/
├── novel-gateway/
├── sql/
└── pom.xml
```

## 已生成模块说明

### 1. 父工程

- 父 POM: [`pom.xml`](novel-cloud/pom.xml)
- 统一管理 Java 21、Spring Boot、Spring Cloud、Spring Cloud Alibaba、MyBatis-Plus 等版本

### 2. 公共模块

#### [`novel-common-core`](novel-cloud/novel-common/novel-common-core/pom.xml)

包含：
- 通用返回包装 [`R<T>`](novel-cloud/novel-common/novel-common-core/src/main/java/com/novel/common/core/domain/R.java:13)
- 业务异常 [`BusinessException`](novel-cloud/novel-common/novel-common-core/src/main/java/com/novel/common/core/exception/BusinessException.java:3)
- WebFlux 基础配置 [`WebFluxConfig`](novel-cloud/novel-common/novel-common-core/src/main/java/com/novel/common/core/config/WebFluxConfig.java:7)

### 3. 用户服务

#### DTO 模块

- [`novel-user-dto`](novel-cloud/novel-user/novel-user-dto/pom.xml)
- 游客登录 DTO [`UserLoginDto`](novel-cloud/novel-user/novel-user-dto/src/main/java/com/novel/user/dto/UserLoginDto.java:10)
- 用户信息 VO [`UserInfoVo`](novel-cloud/novel-user/novel-user-dto/src/main/java/com/novel/user/dto/UserInfoVo.java:8)
- 登录返回 VO [`UserLoginVo`](novel-cloud/novel-user/novel-user-dto/src/main/java/com/novel/user/dto/UserLoginVo.java:8)

#### API 模块

- 当前保留 [`novel-user-api`](novel-cloud/novel-user/novel-user-api/pom.xml) 作为服务间调用预留模块
- 用户面向 App 的登录接口不生成 Feign 接口，直接由 [`UserController`](novel-cloud/novel-user/novel-user-service/src/main/java/com/novel/user/controller/UserController.java:16) 对外提供

#### Server 模块

- 启动类 [`NovelUserApplication`](novel-cloud/novel-user/novel-user-service/src/main/java/com/novel/user/NovelUserApplication.java:12)
- Controller [`UserController`](novel-cloud/novel-user/novel-user-service/src/main/java/com/novel/user/controller/UserController.java:16)
- Service 接口 [`UserService`](novel-cloud/novel-user/novel-user-service/src/main/java/com/novel/user/service/UserService.java:7)
- Service 实现 [`UserServiceImpl`](novel-cloud/novel-user/novel-user-service/src/main/java/com/novel/user/service/impl/UserServiceImpl.java:17)
- Mapper [`UserInfoMapper`](novel-cloud/novel-user/novel-user-service/src/main/java/com/novel/user/mapper/UserInfoMapper.java:6)
- Entity [`UserInfoEntity`](novel-cloud/novel-user/novel-user-service/src/main/java/com/novel/user/entity/UserInfoEntity.java:10)
- MyBatis-Plus 自动填充 [`MybatisPlusMetaObjectHandler`](novel-cloud/novel-user/novel-user-service/src/main/java/com/novel/user/config/MybatisPlusMetaObjectHandler.java:9)

#### 游客登录接口说明

- App 登录接口：[`/api/user/login`](novel-cloud/novel-user/novel-user-service/src/main/java/com/novel/user/controller/UserController.java:22)
- 登录方式：游客登录，不再进行用户名密码匹配
- 登录参数：设备唯一标识、设备名称、系统类型、应用版本、区域、IP 等
- 登录逻辑：
  - 依据 [`deviceId`](novel-cloud/novel-user/novel-user-dto/src/main/java/com/novel/user/dto/UserLoginDto.java:15) 查询游客账号
  - 若不存在则自动创建新游客账号
  - 若存在则更新最近登录设备上下文
  - 调用 [`StpUtil.login()`](novel-cloud/novel-user/novel-user-service/src/main/java/com/novel/user/service/impl/UserServiceImpl.java:42) 签发 token
  - 返回 token 与用户基础信息

返回结构示例：

```json
{
  "code": 0,
  "message": "success",
  "data": {
    "token": "xxxxx",
    "tokenName": "Authorization",
    "expireSeconds": 2592000,
    "userInfo": {
      "userId": 1,
      "guestId": "guest_xxx",
      "nickname": "游客1717290000000",
      "avatar": "https://static.example.com/avatar/default.png",
      "vip": false,
      "deviceId": "android-device-001",
      "region": "CN",
      "ip": "127.0.0.1"
    }
  }
}
```

请求参数示例：

```json
{
  "deviceId": "android-device-001",
  "deviceName": "Xiaomi 14",
  "osType": "android",
  "appVersion": "1.0.0",
  "region": "CN",
  "ip": "127.0.0.1"
}
```

### 4. 书籍服务

#### DTO 模块

- [`novel-book-dto`](novel-cloud/novel-book/novel-book-dto/pom.xml)
- 书籍详情查询 DTO [`BookDetailQueryDto`](novel-cloud/novel-book/novel-book-dto/src/main/java/com/novel/book/dto/BookDetailQueryDto.java:10)
- 书籍详情 VO [`BookDetailVo`](novel-cloud/novel-book/novel-book-dto/src/main/java/com/novel/book/dto/BookDetailVo.java:9)

#### API 模块

- OpenFeign 接口 [`BookOpenFeignApi`](novel-cloud/novel-book/novel-book-api/src/main/java/com/novel/book/api/BookOpenFeignApi.java:10)

#### Server 模块

- 启动类 [`NovelBookApplication`](novel-cloud/novel-book/novel-book-service/src/main/java/com/novel/book/NovelBookApplication.java:12)
- Controller [`BookController`](novel-cloud/novel-book/novel-book-service/src/main/java/com/novel/book/controller/BookController.java:17)
- Open API Controller [`BookOpenController`](novel-cloud/novel-book/novel-book-service/src/main/java/com/novel/book/controller/open/BookOpenController.java:15)
- Service 接口 [`BookService`](novel-cloud/novel-book/novel-book-service/src/main/java/com/novel/book/service/BookService.java:6)
- Service 实现 [`BookServiceImpl`](novel-cloud/novel-book/novel-book-service/src/main/java/com/novel/book/service/impl/BookServiceImpl.java:8)
- Mapper [`BookInfoMapper`](novel-cloud/novel-book/novel-book-service/src/main/java/com/novel/book/mapper/BookInfoMapper.java:6)
- Entity [`BookInfoEntity`](novel-cloud/novel-book/novel-book-service/src/main/java/com/novel/book/entity/BookInfoEntity.java:10)
- MyBatis-Plus 自动填充 [`MybatisPlusMetaObjectHandler`](novel-cloud/novel-book/novel-book-service/src/main/java/com/novel/book/config/MybatisPlusMetaObjectHandler.java:9)

### 5. 订阅服务

#### DTO 模块

- [`novel-subscribe-dto`](novel-cloud/novel-subscribe/novel-subscribe-dto/pom.xml)
- 订阅计划 VO [`SubscribePlanVo`](novel-cloud/novel-subscribe/novel-subscribe-dto/src/main/java/com/novel/subscribe/dto/SubscribePlanVo.java)
- 创建订阅 DTO [`SubscribeCreateDto`](novel-cloud/novel-subscribe/novel-subscribe-dto/src/main/java/com/novel/subscribe/dto/SubscribeCreateDto.java)
- 用户订阅记录 VO [`UserSubscribeVo`](novel-cloud/novel-subscribe/novel-subscribe-dto/src/main/java/com/novel/subscribe/dto/UserSubscribeVo.java)
- 订阅激活 DTO [`SubscribeActivateDto`](novel-cloud/novel-subscribe/novel-subscribe-dto/src/main/java/com/novel/subscribe/dto/SubscribeActivateDto.java)

#### API 模块

- OpenFeign 接口 [`SubscribeOpenFeignApi`](novel-cloud/novel-subscribe/novel-subscribe-api/src/main/java/com/novel/subscribe/api/SubscribeOpenFeignApi.java)
- 供支付模块回调激活订阅

#### Server 模块

- 启动类 [`NovelSubscribeApplication`](novel-cloud/novel-subscribe/novel-subscribe-service/src/main/java/com/novel/subscribe/NovelSubscribeApplication.java)
- Controller [`SubscribeController`](novel-cloud/novel-subscribe/novel-subscribe-service/src/main/java/com/novel/subscribe/controller/SubscribeController.java)
- Open API Controller [`SubscribeOpenController`](novel-cloud/novel-subscribe/novel-subscribe-service/src/main/java/com/novel/subscribe/controller/open/SubscribeOpenController.java)
- Service 接口 [`SubscribeService`](novel-cloud/novel-subscribe/novel-subscribe-service/src/main/java/com/novel/subscribe/service/SubscribeService.java)
- Service 实现 [`SubscribeServiceImpl`](novel-cloud/novel-subscribe/novel-subscribe-service/src/main/java/com/novel/subscribe/service/impl/SubscribeServiceImpl.java)
- Mapper [`SubscribePlanMapper`](novel-cloud/novel-subscribe/novel-subscribe-service/src/main/java/com/novel/subscribe/mapper/SubscribePlanMapper.java) / [`UserSubscribeMapper`](novel-cloud/novel-subscribe/novel-subscribe-service/src/main/java/com/novel/subscribe/mapper/UserSubscribeMapper.java)
- Entity [`SubscribePlanEntity`](novel-cloud/novel-subscribe/novel-subscribe-service/src/main/java/com/novel/subscribe/entity/SubscribePlanEntity.java) / [`UserSubscribeEntity`](novel-cloud/novel-subscribe/novel-subscribe-service/src/main/java/com/novel/subscribe/entity/UserSubscribeEntity.java)

#### 订阅业务逻辑说明

订阅模块**只负责业务层面**：
- 管理订阅计划（VIP套餐的名称、时长、价格）
- 管理用户订阅记录（生效时间、到期时间、自动续订）
- **续订周期计算**：用户已有未过期订阅时，新订阅从到期时间续订，而非当前时间
- **VIP到账时间**：由 `activateSubscribe` 方法根据计划时长与当前订阅状态计算
- 创建订阅时调用支付模块 [`PaymentOpenFeignApi.createPayment()`](novel-cloud/novel-payment/novel-payment-api/src/main/java/com/novel/payment/api/PaymentOpenFeignApi.java) 创建支付订单

#### 接口列表

| 接口 | 方法 | 说明 |
|------|------|------|
| `/api/subscribe/plans` | GET | 查询上架的订阅计划列表 |
| `/api/subscribe/create` | POST | 创建订阅（发起订阅并创建支付订单） |
| `/api/subscribe/active` | GET | 查询用户当前订阅状态 |
| `/api/subscribe/history` | GET | 查询用户订阅历史 |
| `/api/open/subscribe/activate` | POST | 支付成功后激活订阅（服务间调用） |

### 6. 支付服务

#### DTO 模块

- [`novel-payment-dto`](novel-cloud/novel-payment/novel-payment-dto/pom.xml)
- 创建支付订单 DTO [`PaymentCreateDto`](novel-cloud/novel-payment/novel-payment-dto/src/main/java/com/novel/payment/dto/PaymentCreateDto.java)
- 支付订单结果 VO [`PaymentResultVo`](novel-cloud/novel-payment/novel-payment-dto/src/main/java/com/novel/payment/dto/PaymentResultVo.java)
- 支付回调 DTO [`PaymentNotifyDto`](novel-cloud/novel-payment/novel-payment-dto/src/main/java/com/novel/payment/dto/PaymentNotifyDto.java)

#### API 模块

- OpenFeign 接口 [`PaymentOpenFeignApi`](novel-cloud/novel-payment/novel-payment-api/src/main/java/com/novel/payment/api/PaymentOpenFeignApi.java)
- 供订阅模块调用创建支付订单

#### Server 模块

- 启动类 [`NovelPaymentApplication`](novel-cloud/novel-payment/novel-payment-service/src/main/java/com/novel/payment/NovelPaymentApplication.java)
- Controller [`PaymentController`](novel-cloud/novel-payment/novel-payment-service/src/main/java/com/novel/payment/controller/PaymentController.java)
- Open API Controller [`PaymentOpenController`](novel-cloud/novel-payment/novel-payment-service/src/main/java/com/novel/payment/controller/open/PaymentOpenController.java)
- Service 接口 [`PaymentService`](novel-cloud/novel-payment/novel-payment-service/src/main/java/com/novel/payment/service/PaymentService.java)
- Service 实现 [`PaymentServiceImpl`](novel-cloud/novel-payment/novel-payment-service/src/main/java/com/novel/payment/service/impl/PaymentServiceImpl.java)
- Mapper [`PaymentOrderMapper`](novel-cloud/novel-payment/novel-payment-service/src/main/java/com/novel/payment/mapper/PaymentOrderMapper.java)
- Entity [`PaymentOrderEntity`](novel-cloud/novel-payment/novel-payment-service/src/main/java/com/novel/payment/entity/PaymentOrderEntity.java)

#### 支付模块职责说明

支付模块**只负责支付订单层面**：
- 创建支付订单、生成订单号
- 处理第三方支付平台回调、更新支付状态
- 记录交易流水号与回调原始数据
- 支付成功后回调订阅模块 [`SubscribeOpenFeignApi.activateSubscribe()`](novel-cloud/novel-subscribe/novel-subscribe-api/src/main/java/com/novel/subscribe/api/SubscribeOpenFeignApi.java) 激活订阅
- **不涉及任何订阅业务逻辑**（不知道VIP时长、续订规则等）

#### 接口列表

| 接口 | 方法 | 说明 |
|------|------|------|
| `/api/payment/query` | GET | 查询支付订单 |
| `/api/payment/list` | GET | 查询用户支付订单列表 |
| `/api/open/payment/create` | POST | 创建支付订单（服务间调用） |
| `/api/open/payment/notify` | POST | 支付回调（第三方平台回调） |

### 7. 网关服务

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
- [`Mono<R<UserLoginVo>> login(...)`](novel-cloud/novel-user/novel-user-service/src/main/java/com/novel/user/controller/UserController.java:22)
- [`Mono<R<BookDetailVo>> getBookDetail(...)`](novel-cloud/novel-book/novel-book-service/src/main/java/com/novel/book/controller/BookController.java:23)

### 服务间调用接口

仅真正需要给其他微服务调用的能力，才在 `api` 模块中定义 OpenFeign 接口。

例如：
- [`R<BookDetailVo> getBookDetail(...)`](novel-cloud/novel-book/novel-book-api/src/main/java/com/novel/book/api/BookOpenFeignApi.java:13)

### DTO 模块划分规则

DTO/VO 不放公共模块，而是：
- 用户服务 DTO 独立放在 [`novel-user-dto`](novel-cloud/novel-user/novel-user-dto/pom.xml)
- 书籍服务 DTO 独立放在 [`novel-book-dto`](novel-cloud/novel-book/novel-book-dto/pom.xml)

## 配置说明

### Nacos

已在以下服务中配置服务发现：
- [`novel-user-service/application.yml`](novel-cloud/novel-user/novel-user-service/src/main/resources/application.yml)
- [`novel-book-service/application.yml`](novel-cloud/novel-book/novel-book-service/src/main/resources/application.yml)
- [`novel-subscribe-service/application.yml`](novel-cloud/novel-subscribe/novel-subscribe-service/src/main/resources/application.yml)
- [`novel-payment-service/application.yml`](novel-cloud/novel-payment/novel-payment-service/src/main/resources/application.yml)
- [`novel-gateway/application.yml`](novel-cloud/novel-gateway/src/main/resources/application.yml)

默认地址：`127.0.0.1:8848`

### MySQL

已配置四个数据库：
- `novel_user`
- `novel_book`
- `novel_subscribe`
- `novel_payment`

初始化脚本见 [`sql/init.sql`](novel-cloud/sql/init.sql)

游客演示数据：
- `deviceId`: `demo-device-id`
- `region`: `CN`
- `ip`: `127.0.0.1`

### Sa-Token

用户服务已引入：
- [`sa-token-spring-boot3-starter`](novel-cloud/novel-user/novel-user-service/pom.xml)
- [`sa-token-jwt`](novel-cloud/novel-user/novel-user-service/pom.xml)

基础配置位于 [`novel-user-service/application.yml`](novel-cloud/novel-user/novel-user-service/src/main/resources/application.yml)，包括：
- `token-name`
- `timeout`
- `jwt-secret-key`

### OpenFeign

用户服务与书籍服务已引入 OpenFeign，并在 [`bootstrap.yml`](novel-cloud/novel-user/novel-user-service/src/main/resources/bootstrap.yml) 和 [`bootstrap.yml`](novel-cloud/novel-book/novel-book-service/src/main/resources/bootstrap.yml) 中补充了基础超时配置。

## 当前限制与后续建议

当前已完成“后端架子”与游客登录接口接入 Sa-Token，但还没有做以下增强：

1. 设备信息签名校验与防刷策略
2. 统一全局异常处理
3. 参数校验失败统一返回
4. Nacos 配置中心拆分
5. Redis 接入，并将登录态持久化到 Redis
6. 游客转正式账号、绑定手机号或第三方登录
7. API 文档体系（SpringDoc OpenAPI）
8. 日志链路追踪 9单元测试与集成测试

## 运行建议

1. 启动 Nacos
2. 初始化 MySQL，执行 [`sql/init.sql`](novel-cloud/sql/init.sql)
3. 启动 [`novel-user-service`](novel-cloud/novel-user/novel-user-service/pom.xml)
4. 启动 [`novel-book-service`](novel-cloud/novel-book/novel-book-service/pom.xml)
5. 启动 [`novel-subscribe-service`](novel-cloud/novel-subscribe/novel-subscribe-service/pom.xml)
6. 启动 [`novel-payment-service`](novel-cloud/novel-payment/novel-payment-service/pom.xml)
7. 启动 [`novel-gateway`](novel-cloud/novel-gateway/pom.xml)

## 注意事项

你要求里写了 “Spring Boot 4”，但当前 Java 生态下稳定可用、与 Spring Cloud / Spring Cloud Alibaba / MyBatis-Plus 更容易配套落地的是 **Spring Boot 3.x**。因此当前骨架采用的是 **Spring Boot 3.2.x**。如果你坚持尝试 Spring Boot 4，后续需要重新验证整套依赖兼容性。

## 订阅与支付模块调用关系

```text
┌──────────────┐     创建订阅 + 创建支付订单     ┌──────────────┐
│              │ ──────────────────────────────► │              │
│   订阅模块    │                                 │   支付模块    │
│  (subscribe) │ ◄────────────────────────────── │  (payment)   │
│              │     支付成功回调激活订阅           │              │
└──────────────┘                                 └──────────────┘
       │                                                │
       │ 管理业务逻辑                                     │ 只管订单层面
       │ · 续订周期计算                                    │ · 创建订单
       │ · VIP到账时间                                    │ · 支付回调
       │ · 订阅计划                                       │ · 订单查询
       │ · 订阅记录                                       │ · 交易流水
```

**调用流程**：
1. App 调用 `POST /api/subscribe/create` 发起订阅
2. 订阅模块创建待支付订阅记录，通过 Feign 调用 `POST /api/open/payment/create` 创建支付订单
3. App 完成支付后，第三方支付平台回调 `POST /api/open/payment/notify`
4. 支付模块更新订单状态，支付成功时通过 Feign 调用 `POST /api/open/subscribe/activate` 激活订阅
5. 订阅模块计算 VIP 生效时间和到期时间
