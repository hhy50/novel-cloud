# ShenYu 网关模块

该模块作为 [`novel-cloud`](novel-cloud/pom.xml) 的统一入口网关，基于 ShenYu + WebFlux。

## 当前职责

- 服务统一入口
- 对接 Nacos 服务发现
- 为后续鉴权、限流、灰度、熔断预留能力
- 为 [`novel-user-server`](novel-cloud/novel-user/novel-user-server/pom.xml) 与 [`novel-book-server`](novel-cloud/novel-book/novel-book-server/pom.xml) 提供路由入口

## 当前说明

当前阶段先生成骨架与基础配置，后续需要再补充：

- ShenYu Admin / Bootstrap 完整联调配置
- 路由规则初始化
- 认证插件配置
- 跨域、日志追踪、统一异常处理
