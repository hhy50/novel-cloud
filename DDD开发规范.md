# Novel-Cloud DDD 开发规范

## 1. 项目概述

本项目采用 **领域驱动设计（DDD）** 架构，基于 Spring Boot 3 + Spring Cloud + WebFlux 构建微服务系统。

### 1.1 技术栈
- **JDK**: 21
- **Spring Boot**: 3.2.x
- **Spring Cloud**: 2023.x
- **Spring Cloud Alibaba**: 2023.x
- **WebFlux**: 响应式编程
- **MyBatis-Plus**: 3.5.6
- **Sa-Token**: 认证授权
- **Nacos**: 服务注册与配置中心
- **MySQL**: 8.3.0

### 1.2 DDD 分层架构

```
novel-xxx/                           # 业务模块
├── novel-xxx-adapter/               # 适配层（接口适配）
│   └── adapter/
│       └── web/                     # Web 控制器
├── novel-xxx-app/                   # 应用服务层（用例编排）
├── novel-xxx-domain/                # 领域层（核心业务逻辑）
│   ├── entity/                      # 领域实体
│   ├── repository/                  # 仓储接口
│   └── service/                     # 领域服务
├── novel-xxx-infrastructure/        # 基础设施层
│   ├── dataobject/                  # 数据库对象（DO）
│   ├── mapper/                      # MyBatis Mapper
│   └── repository/                  # 仓储实现
├── novel-xxx-dto/                   # 数据传输对象
└── novel-xxx-api/                   # 服务间调用接口（OpenFeign）
```

---

## 2. 分层职责与规范

### 2.1 Adapter 层（适配层）

**职责**：
- 接收外部请求（HTTP、RPC、MQ等）
- 参数校验与格式转换
- 调用应用服务层
- 返回统一格式响应

**规范**：

#### 命名规范
- Controller 类命名：`XxxController`
- 包路径：`com.novel.{module}.adapter.web`
- 方法返回类型：`Mono<R<T>>` （WebFlux 响应式）

#### 代码示例
```java
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserAppService userAppService;

    @PostMapping("/login")
    public Mono<R<UserLoginVo>> login(@Valid @RequestBody UserLoginDto params) {
        return userAppService.login(params).map(R::ok);
    }
}
```

#### 注意事项
- ✅ 仅做参数校验、格式转换，不包含业务逻辑
- ✅ 使用 `@Valid` 进行参数校验
- ✅ 返回统一包装 `R<T>`
- ❌ 不直接调用 Repository
- ❌ 不包含复杂业务判断

---

### 2.2 App 层（应用服务层）

**职责**：
- 用例编排，组合多个领域服务完成业务流程
- 事务管理
- 调用外部服务（OpenFeign）
- DTO 与领域对象转换

**规范**：

#### 命名规范
- 服务类命名：`XxxAppService`
- 包路径：`com.novel.{module}.app`
- 方法返回类型：`Mono<T>`

#### 代码示例
```java
@Service
@RequiredArgsConstructor
public class UserAppService {

    private final UserInfoRepository userInfoRepository;
    private final UserDomainService userDomainService;

    public Mono<UserLoginVo> login(UserLoginDto params) {
        return Mono.fromCallable(() -> doLogin(params));
    }

    private UserLoginVo doLogin(UserLoginDto params) {
        UserInfo userInfo = userInfoRepository.findByDeviceId(params.getDeviceId());
        if (userInfo == null) {
            // 创建新用户
            Long userId = IdUtil.getSnowflakeNextId();
            userInfo = userDomainService.createVisitorUser(userId, params.getDeviceId(),
                    params.getIp(), params.getAppVersion(), params.getCountry(), params.getLanguage());
            userInfoRepository.save(userInfo);
        } else {
            // 更新登录信息
            userDomainService.updateLoginInfo(userInfo, params.getIp(), params.getAppVersion());
            userInfoRepository.updateById(userInfo);
        }
        return convertToVo(userInfo);
    }
}
```

#### 注意事项
- ✅ 负责流程编排，调用领域服务
- ✅ 在此层进行事务控制（`@Transactional`）
- ✅ 调用外部服务（Feign）
- ❌ 不包含核心业务规则（应在 Domain 层）
- ❌ 不直接操作数据库（通过 Repository）

---

### 2.3 Domain 层（领域层）

**职责**：
- 定义领域实体和核心业务规则
- 领域服务封装复杂业务逻辑
- 定义仓储接口（不实现）

**规范**：

#### 命名规范
- 实体类命名：`XxxEntity` 或直接使用业务名称（如 `UserInfo`）
- 领域服务命名：`XxxDomainService`
- 仓储接口命名：`XxxRepository`
- 包路径：
  - `com.novel.{module}.domain.entity`
  - `com.novel.{module}.domain.service`
  - `com.novel.{module}.domain.repository`

#### 领域实体示例
```java
/**
 * 用户信息领域实体
 * - 纯 POJO，不包含持久化注解
 * - 可包含业务方法
 */
@Data
public class UserInfo {
    private Long id;
    private String nickname;
    private String avatar;
    private Integer vipStatus;
    private String deviceId;
    private String ip;
    private LocalDateTime lastLoginTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    // 业务方法示例
    public boolean isVip() {
        return vipStatus != null && vipStatus == 1;
    }
}
```

#### 领域服务示例
```java
@Service
@RequiredArgsConstructor
public class UserDomainService {

    private static final String DEFAULT_AVATAR = "https://static.example.com/avatar/default.png";

    /**
     * 创建访客用户 - 封装创建规则
     */
    public UserInfo createVisitorUser(Long id, String deviceId, String ip, 
                                      String appVersion, String country, String language) {
        UserInfo user = new UserInfo();
        user.setId(id);
        user.setNickname("Visitor" + id);  // 命名规则
        user.setAvatar(DEFAULT_AVATAR);
        user.setVipStatus(0);
        user.setDeviceId(deviceId);
        user.setIp(ip);
        user.setAppVersion(appVersion);
        user.setCountry(country);
        user.setLanguage(language);
        user.setLastLoginTime(LocalDateTime.now());
        return user;
    }
}
```

#### 仓储接口示例
```java
/**
 * 用户信息仓储接口
 * - 只定义接口，不实现
 * - 使用领域对象（UserInfo），不使用 DO
 */
public interface UserInfoRepository {
    UserInfo findByDeviceId(String deviceId);
    void save(UserInfo userInfo);
    void updateById(UserInfo userInfo);
}
```

#### 注意事项
- ✅ 领域实体是纯 POJO，不依赖框架注解（MyBatis-Plus、JPA等）
- ✅ 核心业务规则放在领域服务或实体方法中
- ✅ Repository 接口定义在 Domain 层，实现在 Infrastructure 层
- ✅ 领域对象应具有自解释性，方法命名清晰表达业务含义
- ❌ 不直接依赖外部框架（Spring 的 `@Service` 除外）
- ❌ 不包含持久化细节（SQL、缓存等）

---

### 2.4 Infrastructure 层（基础设施层）

**职责**：
- 实现 Repository 接口
- 数据持久化（MyBatis-Plus Mapper）
- DO（Data Object）与领域对象转换
- 集成第三方服务

**规范**：

#### 命名规范
- 数据对象命名：`XxxDO`
- 仓储实现命名：`XxxRepositoryImpl`
- Mapper 命名：`XxxMapper`
- 包路径：
  - `com.novel.{module}.infrastructure.dataobject`
  - `com.novel.{module}.infrastructure.repository`
  - `com.novel.{module}.infrastructure.mapper`

#### DO（数据对象）示例
```java
/**
 * 用户信息数据对象
 * - 包含 MyBatis-Plus 注解
 * - 与数据库表一一对应
 */
@Data
@TableName("user_info")
public class UserInfoDO {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    
    private String nickname;
    private String avatar;
    private Integer vipStatus;
    private String deviceId;
    private String ip;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
```

#### Mapper 示例
```java
@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfoDO> {
    // 继承 BaseMapper，获得基础 CRUD 能力
    // 复杂查询可自定义方法
}
```

#### Repository 实现示例
```java
@Component
@RequiredArgsConstructor
public class UserInfoRepositoryImpl implements UserInfoRepository {

    private final UserInfoMapper userInfoMapper;

    @Override
    public UserInfo findByDeviceId(String deviceId) {
        UserInfoDO userInfoDO = userInfoMapper.selectOne(
                new LambdaQueryWrapper<UserInfoDO>()
                        .eq(UserInfoDO::getDeviceId, deviceId)
                        .last("limit 1")
        );
        if (userInfoDO == null) {
            return null;
        }
        // DO -> 领域对象转换
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(userInfoDO, userInfo);
        return userInfo;
    }

    @Override
    public void save(UserInfo userInfo) {
        // 领域对象 -> DO 转换
        UserInfoDO userInfoDO = new UserInfoDO();
        BeanUtils.copyProperties(userInfo, userInfoDO);
        userInfoMapper.insert(userInfoDO);
    }

    @Override
    public void updateById(UserInfo userInfo) {
        UserInfoDO userInfoDO = new UserInfoDO();
        BeanUtils.copyProperties(userInfo, userInfoDO);
        userInfoMapper.updateById(userInfoDO);
    }
}
```

#### 注意事项
- ✅ DO 对象只用于持久化层，不向上层传递
- ✅ Repository 实现负责 DO 与领域对象的转换
- ✅ 使用 `@Component` 注解仓储实现类
- ✅ 复杂查询可使用 MyBatis-Plus 的 LambdaQueryWrapper
- ❌ 不在 DO 中包含业务逻辑
- ❌ 不让 DO 泄露到上层（App、Domain）

---

### 2.5 DTO 层（数据传输对象）

**职责**：
- 定义 API 请求参数（Dto）
- 定义 API 返回对象（Vo）
- 参数校验注解

**规范**：

#### 命名规范
- 请求对象命名：`XxxDto` 或 `XxxQueryDto`、`XxxCreateDto`
- 返回对象命名：`XxxVo`
- 包路径：`com.novel.{module}.dto`

#### Dto 示例（请求参数）
```java
@Data
public class UserLoginDto {
    
    @NotBlank(message = "设备ID不能为空")
    private String deviceId;
    
    @NotBlank(message = "IP不能为空")
    private String ip;
    
    @NotBlank(message = "应用版本不能为空")
    private String appVersion;
    
    private String country;
    private String language;
}
```

#### Vo 示例（返回对象）
```java
@Data
public class UserLoginVo {
    private String token;
    private Long userId;
    private String nickname;
    private String avatar;
    private Boolean isVip;
}
```

#### 注意事项
- ✅ DTO 独立模块，避免循环依赖
- ✅ 使用 JSR-303 校验注解（`@NotBlank`、`@NotNull`等）
- ✅ DTO 命名语义化，清晰表达用途
- ❌ 不在 DTO 中包含业务逻辑
- ❌ 不直接使用领域对象或 DO 作为接口参数

---

### 2.6 API 层（服务间调用）

**职责**：
- 定义 OpenFeign 接口
- 仅用于微服务间调用
- 不对外暴露给客户端

**规范**：

#### 命名规范
- 接口命名：`XxxOpenFeignApi`
- Controller 命名：`XxxOpenController`
- 包路径：`com.novel.{module}.api`

#### OpenFeign 接口示例
```java
@FeignClient(name = "novel-book", contextId = "bookOpenFeignApi")
public interface BookOpenFeignApi {
    
    @GetMapping("/api/open/book/detail")
    R<BookDetailVo> getBookDetail(@RequestParam("bookId") Long bookId);
}
```

#### Open Controller 示例
```java
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/open/book")
public class BookOpenController {

    private final BookAppService bookAppService;

    @GetMapping("/detail")
    public Mono<R<BookDetailVo>> getBookDetail(@RequestParam Long bookId) {
        return bookAppService.getBookDetail(bookId).map(R::ok);
    }
}
```

#### 注意事项
- ✅ `/api/open/*` 路径用于服务间调用
- ✅ Feign 接口返回 `R<T>`（不使用 Mono）
- ✅ Open Controller 返回 `Mono<R<T>>`
- ❌ 不在 Feign 接口中使用复杂对象作为 `@RequestBody`（建议拆分参数）
- ❌ 避免循环依赖（服务 A 调用 B，B 又调用 A）

---

## 3. 模块依赖关系

```
┌─────────────────────────────────────────────────────┐
│                  novel-xxx-service                  │ ← 启动模块
│                  (Spring Boot 启动类)                │
└───────────────────┬─────────────────────────────────┘
                    │ 依赖所有模块
        ┌───────────┼───────────┬───────────┐
        ↓           ↓           ↓           ↓
┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐
│ adapter  │ │   app    │ │ domain   │ │   infra  │
└─────┬────┘ └─────┬────┘ └─────┬────┘ └─────┬────┘
      │            │            │            │
      └────────────┴────────────┴────────────┘
                   ↓
            ┌──────────┐
            │   dto    │  ← 被所有层依赖
            └──────────┘
```

### 依赖规则
- **adapter** → app + dto
- **app** → domain + dto + infrastructure
- **domain** → 无依赖（纯领域）
- **infrastructure** → domain + dto
- **service** → adapter + app + domain + infrastructure + dto

---

## 4. 代码规范

### 4.1 命名规范

| 类型 | 规范 | 示例 |
|------|------|------|
| 实体类 | XxxEntity 或业务名称 | `UserInfo`、`BookEntity` |
| DO | XxxDO | `UserInfoDO` |
| DTO | XxxDto | `UserLoginDto` |
| VO | XxxVo | `UserLoginVo` |
| Controller | XxxController | `UserController` |
| AppService | XxxAppService | `UserAppService` |
| DomainService | XxxDomainService | `UserDomainService` |
| Repository | XxxRepository | `UserInfoRepository` |
| RepositoryImpl | XxxRepositoryImpl | `UserInfoRepositoryImpl` |
| Mapper | XxxMapper | `UserInfoMapper` |

### 4.2 包命名规范

```
com.novel.{module}
├── adapter.web              # Web 控制器
├── app                      # 应用服务
├── domain
│   ├── entity              # 领域实体
│   ├── repository          # 仓储接口
│   └── service             # 领域服务
├── infrastructure
│   ├── dataobject          # 数据对象
│   ├── mapper              # MyBatis Mapper
│   └── repository          # 仓储实现
├── dto                     # 数据传输对象
└── api                     # OpenFeign 接口
```

### 4.3 接口路径规范

| 类型 | 路径前缀 | 说明 | 示例 |
|------|---------|------|------|
| 客户端接口 | `/api/{module}/*` | 对外暴露给 App/Web | `/api/user/login` |
| 服务间调用 | `/api/open/{module}/*` | 仅供微服务间调用 | `/api/open/payment/create` |

### 4.4 返回值规范

#### 统一返回格式
```java
@Data
public class R<T> {
    private Integer code;
    private String message;
    private T data;
    
    public static <T> R<T> ok(T data) {
        R<T> r = new R<>();
        r.setCode(0);
        r.setMessage("success");
        r.setData(data);
        return r;
    }
}
```

#### 响应式返回
- Controller 层：`Mono<R<T>>`
- AppService 层：`Mono<T>`
- Feign 接口：`R<T>`（非响应式）

---

## 5. 数据库规范

### 5.1 表命名规范
- 全小写，下划线分隔
- 业务模块前缀：`{module}_xxx`
- 示例：`user_info`、`book_chapter`、`payment_order`

### 5.2 字段规范

| 字段类型 | 命名 | 类型 | 说明 |
|---------|------|------|------|
| 主键 | `id` | `BIGINT` | 雪花算法生成 |
| 创建时间 | `create_time` | `DATETIME` | 自动填充 |
| 更新时间 | `update_time` | `DATETIME` | 自动填充 |
| 逻辑删除 | `deleted` | `TINYINT` | 0=正常，1=删除 |

### 5.3 MyBatis-Plus 配置

#### 自动填充
```java
@Component
public class MybatisPlusMetaObjectHandler implements MetaObjectHandler {
    
    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
    }
}
```

---

## 6. 异常处理规范

### 6.1 业务异常

```java
public class BusinessException extends RuntimeException {
    private final Integer code;
    
    public BusinessException(String message) {
        super(message);
        this.code = 500;
    }
    
    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }
}
```

### 6.2 全局异常处理（推荐）

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(BusinessException.class)
    public Mono<R<Void>> handleBusinessException(BusinessException e) {
        return Mono.just(R.fail(e.getCode(), e.getMessage()));
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Mono<R<Void>> handleValidationException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return Mono.just(R.fail(400, message));
    }
}
```

---

## 7. 事务管理规范

### 7.1 事务注解位置
- ✅ 在 **App 层**使用 `@Transactional`
- ❌ 不在 Controller 层使用
- ❌ 不在 Domain 层使用

### 7.2 事务示例

```java
@Service
@RequiredArgsConstructor
public class PaymentAppService {

    private final PaymentRepository paymentRepository;
    private final SubscribeOpenFeignApi subscribeOpenFeignApi;

    @Transactional(rollbackFor = Exception.class)
    public Mono<Void> handlePaymentCallback(PaymentNotifyDto notify) {
        return Mono.fromRunnable(() -> {
            // 1. 更新支付订单状态
            PaymentOrder order = paymentRepository.findByOrderNo(notify.getOrderNo());
            order.updateStatus(PaymentStatus.SUCCESS);
            paymentRepository.updateById(order);
            
            // 2. 调用订阅服务激活
            R<Void> result = subscribeOpenFeignApi.activateSubscribe(
                new SubscribeActivateDto(order.getUserId(), order.getSubscribeId())
            );
            
            if (result.getCode() != 0) {
                throw new BusinessException("激活订阅失败");
            }
        });
    }
}
```

---

## 8. 服务间调用规范

### 8.1 调用流程

```
订阅服务 (subscribe)
    ↓ 创建订阅时调用
支付服务 (payment) 
    ↓ 支付成功后回调
订阅服务 (subscribe)
```

### 8.2 调用示例

#### 订阅服务调用支付服务
```java
@Service
@RequiredArgsConstructor
public class SubscribeAppService {

    private final PaymentOpenFeignApi paymentOpenFeignApi;

    public Mono<SubscribeCreateVo> createSubscribe(SubscribeCreateDto params) {
        return Mono.fromCallable(() -> {
            // 1. 创建订阅记录
            UserSubscribe subscribe = createSubscribeRecord(params);
            
            // 2. 调用支付服务创建订单
            R<PaymentResultVo> paymentResult = paymentOpenFeignApi.createPayment(
                new PaymentCreateDto()
                    .setUserId(params.getUserId())
                    .setAmount(subscribe.getAmount())
                    .setBusinessType("SUBSCRIBE")
                    .setBusinessId(subscribe.getId())
            );
            
            if (paymentResult.getCode() != 0) {
                throw new BusinessException("创建支付订单失败");
            }
            
            return convertToVo(subscribe, paymentResult.getData());
        });
    }
}
```

#### 支付服务回调订阅服务
```java
@Service
@RequiredArgsConstructor
public class PaymentAppService {

    private final SubscribeOpenFeignApi subscribeOpenFeignApi;

    @Transactional(rollbackFor = Exception.class)
    public void handleCallback(PaymentNotifyDto notify) {
        // 1. 更新支付状态
        updatePaymentStatus(notify);
        
        // 2. 回调订阅服务激活
        R<Void> result = subscribeOpenFeignApi.activateSubscribe(
            new SubscribeActivateDto(notify.getUserId(), notify.getBusinessId())
        );
        
        if (result.getCode() != 0) {
            throw new BusinessException("激活订阅失败");
        }
    }
}
```

### 8.3 注意事项
- ✅ 服务间调用在 App 层进行
- ✅ 调用失败抛出 `BusinessException`，触发事务回滚
- ✅ 使用 `/api/open/*` 路径
- ❌ 避免循环依赖
- ❌ 不在 Domain 层调用外部服务

---

## 9. 响应式编程规范

### 9.1 WebFlux 使用场景
- 高并发场景（如书籍列表、搜索）
- I/O 密集型操作（数据库、外部 API）

### 9.2 响应式转换

#### 阻塞代码包装
```java
public Mono<UserLoginVo> login(UserLoginDto params) {
    // 阻塞代码用 Mono.fromCallable 包装
    return Mono.fromCallable(() -> doLogin(params));
}

private UserLoginVo doLogin(UserLoginDto params) {
    // 同步阻塞逻辑
    UserInfo user = userInfoRepository.findByDeviceId(params.getDeviceId());
    // ...
    return vo;
}
```

#### 响应式链式调用
```java
public Mono<BookDetailVo> getBookDetail(Long bookId) {
    return bookRepository.findById(bookId)
        .flatMap(book -> chapterRepository.countByBookId(bookId)
            .map(count -> convertToVo(book, count)))
        .switchIfEmpty(Mono.error(new BusinessException("书籍不存在")));
}
```

### 9.3 注意事项
- ✅ Controller 返回 `Mono<R<T>>`
- ✅ AppService 返回 `Mono<T>`
- ✅ 阻塞代码用 `Mono.fromCallable` 包装
- ❌ 不在 Mono 链中直接使用阻塞调用
- ❌ 不混用响应式和阻塞代码

---

## 10. 参数校验规范

### 10.1 使用 JSR-303 注解

```java
@Data
public class UserLoginDto {
    
    @NotBlank(message = "设备ID不能为空")
    @Size(max = 100, message = "设备ID长度不能超过100")
    private String deviceId;
    
    @NotBlank(message = "IP不能为空")
    @Pattern(regexp = "^((25[0-5]|...)$", message = "IP格式不正确")
    private String ip;
    
    @NotNull(message = "应用版本不能为空")
    @Size(min = 1, max = 20, message = "版本号长度1-20")
    private String appVersion;
    
    @Min(value = 1, message = "用户ID必须大于0")
    private Long userId;
}
```

### 10.2 Controller 层启用校验

```java
@PostMapping("/login")
public Mono<R<UserLoginVo>> login(@Valid @RequestBody UserLoginDto params) {
    return userAppService.login(params).map(R::ok);
}
```

### 10.3 常用校验注解

| 注解 | 说明 |
|------|------|
| `@NotNull` | 不能为 null |
| `@NotBlank` | 不能为空字符串 |
| `@Size(min, max)` | 字符串长度或集合大小 |
| `@Min` / `@Max` | 数值范围 |
| `@Pattern` | 正则匹配 |
| `@Email` | 邮箱格式 |

---

## 11. 日志规范

### 11.1 日志级别使用

```java
@Slf4j
@Service
@RequiredArgsConstructor
public class UserAppService {

    public Mono<UserLoginVo> login(UserLoginDto params) {
        log.info("用户登录请求, deviceId={}", params.getDeviceId());
        
        return Mono.fromCallable(() -> {
            try {
                UserLoginVo result = doLogin(params);
                log.info("用户登录成功, userId={}", result.getUserId());
                return result;
            } catch (Exception e) {
                log.error("用户登录失败, deviceId={}, error={}", params.getDeviceId(), e.getMessage(), e);
                throw e;
            }
        });
    }
}
```

### 11.2 日志级别说明

| 级别 | 使用场景 |
|------|---------|
| `ERROR` | 异常、错误 |
| `WARN` | 警告、降级 |
| `INFO` | 关键业务流程、接口调用 |
| `DEBUG` | 调试信息 |
| `TRACE` | 详细追踪 |

### 11.3 注意事项
- ✅ 使用 `@Slf4j` 注解
- ✅ 关键业务节点记录 INFO 日志
- ✅ 异常记录完整堆栈（`log.error("msg", e)`）
- ❌ 不在循环中大量打印日志
- ❌ 不记录敏感信息（密码、token完整值）

---

## 12. 配置规范

### 12.1 配置文件结构

```
novel-xxx-service/
└── src/main/resources/
    ├── application.yml          # 主配置
    ├── bootstrap.yml            # Nacos 配置（优先加载）
    └── application-dev.yml      # 开发环境配置
```

### 12.2 application.yml 示例

```yaml
server:
  port: 8081

spring:
  application:
    name: novel-user
  datasource:
    url: jdbc:mysql://localhost:3306/novel_user?useUnicode=true&characterEncoding=utf8
    username: root
    password: ${DB_PASSWORD:root}
    driver-class-name: com.mysql.cj.jdbc.Driver

mybatis-plus:
  configuration:
    map-underscore-to-camel-case: true
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
  global-config:
    db-config:
      logic-delete-field: deleted
      logic-delete-value: 1
      logic-not-delete-value: 0
```

### 12.3 bootstrap.yml 示例（Nacos）

```yaml
spring:
  cloud:
    nacos:
      discovery:
        server-addr: 127.0.0.1:8848
        namespace: dev
      config:
        server-addr: 127.0.0.1:8848
        namespace: dev
        file-extension: yml
```

---

## 13. 测试规范

### 13.1 单元测试

```java
@SpringBootTest
class UserAppServiceTest {

    @Autowired
    private UserAppService userAppService;
    
    @MockBean
    private UserInfoRepository userInfoRepository;

    @Test
    void testLogin_NewUser_Success() {
        // Given
        UserLoginDto params = new UserLoginDto();
        params.setDeviceId("test-device-001");
        params.setIp("127.0.0.1");
        
        when(userInfoRepository.findByDeviceId(anyString())).thenReturn(null);
        
        // When
        UserLoginVo result = userAppService.login(params).block();
        
        // Then
        assertNotNull(result);
        assertNotNull(result.getToken());
        verify(userInfoRepository, times(1)).save(any(UserInfo.class));
    }
}
```

### 13.2 集成测试

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class UserControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void testLogin() {
        UserLoginDto params = new UserLoginDto();
        params.setDeviceId("test-device-001");
        params.setIp("127.0.0.1");
        
        webTestClient.post()
            .uri("/api/user/login")
            .bodyValue(params)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.code").isEqualTo(0)
            .jsonPath("$.data.token").isNotEmpty();
    }
}
```

---

## 14. Git 提交规范

### 14.1 提交信息格式

```
<type>(<scope>): <subject>

<body>

<footer>
```

### 14.2 类型（type）

| 类型 | 说明 |
|------|------|
| `feat` | 新功能 |
| `fix` | 修复bug |
| `docs` | 文档更新 |
| `style` | 代码格式（不影响功能） |
| `refactor` | 重构 |
| `test` | 测试相关 |
| `chore` | 构建、依赖等 |

### 14.3 示例

```
feat(user): 新增游客登录功能

- 实现根据 deviceId 查询或创建用户
- 集成 Sa-Token 签发 JWT
- 返回用户基础信息

Closes #123
```

---

## 15. 开发流程

### 15.1 新增功能开发流程

1. **需求分析** → 明确功能边界和业务规则
2. **领域建模** → 识别实体、聚合根、领域服务
3. **编写 DTO** → 定义请求参数和返回对象
4. **实现 Domain 层**
   - 定义实体（Entity）
   - 编写领域服务（DomainService）
   - 定义仓储接口（Repository）
5. **实现 Infrastructure 层**
   - 创建 DO（Data Object）
   - 编写 Mapper
   - 实现 Repository
6. **实现 App 层** → 编排业务流程
7. **实现 Adapter 层** → 暴露接口
8. **编写测试** → 单元测试 + 集成测试
9. **联调验证** → 本地环境验证
10. **提交代码** → 按 Git 规范提交

### 15.2 开发检查清单

- [ ] 是否按 DDD 分层实现？
- [ ] 领域逻辑是否在 Domain 层？
- [ ] DO 是否没有泄露到上层？
- [ ] 是否使用了统一返回格式 `R<T>`？
- [ ] 是否添加了参数校验？
- [ ] 是否添加了异常处理？
- [ ] 是否添加了关键日志？
- [ ] 是否编写了单元测试？
- [ ] 接口路径是否符合规范？
- [ ] 是否避免了循环依赖？

---

## 16. 常见问题

### Q1: 什么时候用领域服务，什么时候用应用服务？

**领域服务（DomainService）**：
- 封装领域逻辑和业务规则
- 不依赖外部服务
- 可以被多个应用服务复用

**应用服务（AppService）**：
- 编排业务流程
- 调用领域服务、外部服务
- 管理事务

**示例**：
```java
// 领域服务：创建访客的业务规则
userDomainService.createVisitorUser(...)

// 应用服务：编排登录流程
userAppService.login(...) {
    // 调用领域服务
    // 调用仓储保存
    // 调用 Sa-Token 签发
}
```

### Q2: 为什么要分 Entity 和 DO？

- **Entity（领域实体）**：业务模型，纯 POJO，不依赖框架
- **DO（数据对象）**：持久化模型，包含数据库注解

**好处**：
- 领域模型不受数据库变化影响
- 便于单元测试（不需要数据库）
- 领域模型更清晰表达业务含义

### Q3: Repository 接口为什么定义在 Domain 层？

**原因**：
- Repository 是领域概念，描述"如何获取领域对象"
- 实现细节（MyBatis、JPA）属于基础设施层
- 符合依赖倒置原则（高层不依赖低层）

### Q4: 何时使用响应式编程？

**适合场景**：
- 高并发读操作（列表、搜索）
- I/O 密集型（数据库、外部 API）
- 需要背压控制的场景

**不适合场景**：
- 简单 CRUD 操作
- 复杂事务处理
- 团队不熟悉响应式

**建议**：
- Controller 统一返回 `Mono<R<T>>`
- 内部阻塞代码用 `Mono.fromCallable` 包装
- 逐步引入响应式，不强求全栈响应式

---

## 17. 参考资料

- [DDD 领域驱动设计](https://domain-driven-design.org/)
- [Spring Boot 官方文档](https://spring.io/projects/spring-boot)
- [MyBatis-Plus 官方文档](https://baomidou.com/)
- [WebFlux 响应式编程](https://docs.spring.io/spring-framework/reference/web/webflux.html)
- [Sa-Token 官方文档](https://sa-token.cc/)

---

**文档版本**：v1.0  
**更新日期**：2026-06-05  
**维护者**：Novel-Cloud 开发团队
