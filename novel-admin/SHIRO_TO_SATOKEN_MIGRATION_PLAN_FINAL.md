## ✅ 迁移执行检查清单

### 准备阶段
- [ ] 阅读完整迁移方案
- [ ] 创建代码分支 `feature/migrate-to-satoken`
- [ ] 备份当前数据库
- [ ] 确认团队成员理解迁移计划
- [ ] 准备测试环境
- [ ] 通知用户计划维护时间

### 依赖替换阶段
- [ ] 修改根pom.xml添加Sa-Token版本号
- [ ] 添加Sa-Token依赖到dependencyManagement
- [ ] 修改ruoyi-framework/pom.xml删除Shiro依赖
- [ ] 修改ruoyi-framework/pom.xml添加Sa-Token依赖
- [ ] 修改ruoyi-common/pom.xml删除Shiro依赖
- [ ] 执行 `mvn clean compile` 验证依赖
- [ ] 提交代码：`git commit -m "阶段一：依赖替换完成"`

### 配置类迁移阶段
- [ ] 创建SaTokenConfig.java配置类
- [ ] 创建application-satoken.yml配置文件
- [ ] 修改application.yml引入satoken配置
- [ ] 删除或注释ShiroConfig.java
- [ ] 测试应用启动是否正常
- [ ] 提交代码：`git commit -m "阶段二：配置类迁移完成"`

### 工具类迁移阶段
- [ ] 创建SaTokenUtils.java
- [ ] 修改ShiroUtils.java为兼容层（可选）
- [ ] 创建或修改SaTokenConstants.java
- [ ] 全局搜索ShiroUtils使用并验证兼容性
- [ ] 提交代码：`git commit -m "阶段三：工具类迁移完成"`

### 业务服务迁移阶段
- [ ] 创建SaTokenLoginService.java
- [ ] 创建SaTokenPasswordService.java
- [ ] 创建SaTokenPermissionService.java（实现StpInterface）
- [ ] 更新PermissionService.java
- [ ] 测试登录服务
- [ ] 测试权限服务
- [ ] 提交代码：`git commit -m "阶段四：业务服务迁移完成"`

### 控制器迁移阶段
- [ ] 全局替换：`@RequiresPermissions` → `@SaCheckPermission`
- [ ] 全局替换：`@RequiresRoles` → `@SaCheckRole`
- [ ] 全局替换：`Logical.AND` → `SaMode.AND`
- [ ] 全局替换：`Logical.OR` → `SaMode.OR`
- [ ] 修改SysLoginController.java
- [ ] 验证所有Controller编译通过
- [ ] 提交代码：`git commit -m "阶段五：控制器迁移完成"`

### 模板文件迁移阶段
- [ ] 在SaTokenConfig中添加ShiroDialect Bean
- [ ] 全局替换：`shiro:hasPermission` → `sa:permission`
- [ ] 全局替换：`shiro:hasRole` → `sa:role`
- [ ] 全局替换：`shiro:lacksPermission` → `sa:notPermission`
- [ ] 添加xmlns:sa命名空间声明
- [ ] 手动验证关键页面（用户、角色、菜单）
- [ ] 提交代码：`git commit -m "阶段七：模板文件迁移完成"`

### 在线用户管理迁移阶段
- [ ] 创建SaTokenOnlineService.java
- [ ] 修改SysUserOnlineController.java
- [ ] 测试在线用户列表
- [ ] 测试强制下线功能
- [ ] 测试批量踢出功能
- [ ] 提交代码：`git commit -m "阶段八：在线用户管理迁移完成"`

### 异常处理迁移阶段
- [ ] 修改GlobalExceptionHandler.java
- [ ] 添加NotLoginException处理
- [ ] 添加NotPermissionException处理
- [ ] 添加NotRoleException处理
- [ ] 测试各类异常处理
- [ ] 提交代码：`git commit -m "阶段十：异常处理迁移完成"`

### 清理阶段
- [ ] 删除ruoyi-framework/src/.../framework/shiro/目录
- [ ] 删除ShiroConfig.java
- [ ] 删除ehcache-shiro.xml
- [ ] 清理无用的import语句
- [ ] 验证编译无错误
- [ ] 提交代码：`git commit -m "阶段十一：清理旧代码完成"`

### 测试验证阶段
- [ ] 功能测试：用户登录
- [ ] 功能测试：权限控制
- [ ] 功能测试：会话管理
- [ ] 功能测试：在线用户
- [ ] 功能测试：注销登出
- [ ] 功能测试：异常处理
- [ ] 性能测试：并发登录
- [ ] 性能测试：权限检查
- [ ] 回归测试：所有业务功能
- [ ] 记录测试结果

### 部署上线阶段
- [ ] 代码Review
- [ ] 合并到主分支
- [ ] 准备回滚方案
- [ ] 通知用户系统维护
- [ ] 备份生产数据库
- [ ] 部署到生产环境
- [ ] 验证生产环境功能
- [ ] 监控系统日志
- [ ] 收集用户反馈

### 后续优化阶段
- [ ] 配置Redis集成
- [ ] 优化性能参数
- [ ] 添加监控告警
- [ ] 编写使用文档
- [ ] 团队培训
- [ ] 移除ShiroUtils兼容层（可选）

---

## 📝 迁移总结

### 技术收益

**1. 性能提升**
- 更轻量级的框架
- 更高效的权限检查
- 支持分布式Session（Redis）
- 降低内存占用

**2. 功能增强**
- 更丰富的API
- 更灵活的配置
- 内置多种token风格
- 支持单点登录（SSO）
- 支持OAuth2.0
- 支持微服务架构

**3. 开发体验**
- 更简洁的API
- 更好的文档
- 更活跃的社区
- 更快的问题响应

**4. 维护成本**
- 减少代码量（约30%）
- 更少的配置
- 更少的bug
- 更好的扩展性

### 迁移统计

**代码变更**：
- 删除文件：约20个
- 新增文件：约10个
- 修改文件：约50个
- 代码行数变化：-1500行

**配置变更**：
- 删除配置：约30项
- 新增配置：约15项
- 配置文件：3个

**依赖变更**：
- 删除依赖：5个
- 新增依赖：3个

### 经验总结

**成功关键因素**：
1. ✅ 详细的迁移方案
2. ✅ 充分的测试验证
3. ✅ 完善的回滚方案
4. ✅ 团队充分沟通
5. ✅ 分阶段逐步迁移

**遇到的挑战**：
1. ⚠️ Session存储方式差异
2. ⚠️ 权限缓存机制不同
3. ⚠️ 在线用户数据迁移
4. ⚠️ 前端模板标签替换

**解决方案**：
1. ✅ 提供兼容层过渡
2. ✅ 使用Redis统一存储
3. ✅ 编写数据迁移脚本
4. ✅ 使用自动化替换工具

### 后续建议

**短期（1个月内）**：
- 密切监控系统运行
- 收集用户反馈
- 修复发现的问题
- 优化性能参数

**中期（3个月内）**：
- 移除兼容层代码
- 集成Redis缓存
- 添加监控告警
- 完善文档

**长期（6个月后）**：
- 考虑微服务改造
- 实现单点登录
- 集成OAuth2.0
- 性能深度优化

---

## 🎉 迁移完成

恭喜！您已经成功完成从Apache Shiro到Sa-Token的迁移。

**下一步行动**：
1. 📧 通知用户系统已恢复
2. 📊 监控系统运行状态
3. 📝 更新项目文档
4. 🎓 组织团队培训
5. 🔍 持续优化改进

**技术支持**：
- Sa-Token官方文档：https://sa-token.cc/
- 问题反馈：https://gitee.com/dromara/sa-token/issues
- 社区交流群：见官网

**项目信息**：
- 迁移方案版本：1.0
- 创建日期：2026-06-09
- 目标系统：RuoYi 4.8.3
- Sa-Token版本：1.39.0

---

**祝您的项目运行顺利！** 🚀

