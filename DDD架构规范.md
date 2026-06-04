严格遵循COLA四层DDD架构约束，分层依赖、代码编写强制遵守以下规则：
【分层依赖铁律】
依赖方向：Adapter → Application → Domain ← Infrastructure
1. Adapter(接入层)：仅Controller、VO、外部接口适配；只做参数校验、请求转发、结果组装，无任何业务逻辑、无DB操作。
2. Application(应用层)：只用例编排、事务控制、组装多个领域动作；
   ✅ 只能依赖Domain包下：领域Service、Gateway接口
   ❌ 禁止直接引入Infrastructure、Mapper、DAO、DB相关实现类，禁止直接操作数据库
   极简CRUD可直接调用Domain.Gateway接口，禁止绕过Domain直连仓储实现。
3. Domain(领域层，DDD核心)：唯一存放业务规则、实体、聚合根、领域服务；定义Gateway仓储接口，**领域不能依赖任何外层(Application/Adapter/Infra)代码**；所有业务校验、业务规则下沉到Domain，不准写在Controller、Application。
4. Infrastructure(基础设施层)：实现Domain定义的Gateway接口，封装Mapper、缓存、第三方API；仅被Domain间接依赖，上层不能直接依赖本层实现。

【轻量化约束（当前项目初期简单业务）】
1. 简单单表CRUD无需强制拆分Command/Query，无需强制Extension扩展点、领域事件；
2. 精简对象转换，避免VO/DTO/DO/Entity四层全冗余映射，按需复用对象；
3. 实体优先充血，简单场景无复杂聚合可单实体落地。

【禁止项】
1. 业务逻辑逃逸到Controller、Application；
2. Application/Adapter直接注入Mapper、Infra实现类；
3. Domain引入Mybatis、数据库、外部SDK依赖。
生成代码全部符合上述约束。