# Phase 7: 注册错误提示优化 - Context

**Gathered:** 2026-04-18
**Status:** Ready for planning

<domain>
## Phase Boundary

修复重复注册错误提示不明确问题，让用户在用户名或邮箱已被注册时得到清晰的错误提示。

**This phase delivers:**
- 用户名已存在时返回"用户名已存在，请尝试其他用户名"
- 邮箱已被注册时返回"该邮箱已被注册"
- 数据库层增加email字段支持

</domain>

<decisions>
## Implementation Decisions

### 错误消息内容
- **D-01:** 用户名冲突时返回精确消息：`"用户名已存在，请尝试其他用户名"`
  - 对应 REQUIREMENTS: AUTH-01
- **D-02:** 邮箱冲突时返回精确消息：`"该邮箱已被注册"`
  - 对应 REQUIREMENTS: AUTH-02

### Email字段设计
- **D-03:** 注册时email字段为可选（非必填）
  - 当前系统设计以用户名为主要身份标识，email为辅助联系信息
  - 保持向后兼容，不需要强制用户填写email
  - 但注册时如果填写了email，则必须唯一

### 唯一性约束
- **D-04:** email字段在数据库层添加唯一约束（可NULL，允许多个NULL值）
  - 使用 `UNIQUE KEY uk_email (email)` 语法
  - 与现有 `uk_username` 约束模式保持一致

### 错误处理方式
- **D-05:** 使用BusinessException + 现有ResultCode体系
  - 用户名冲突：复用 `ResultCode.USER_ALREADY_EXISTS` 但自定义消息覆盖
  - 邮箱冲突：新增 `ResultCode.EMAIL_ALREADY_EXISTS` 错误码

### 数据库迁移
- **D-06:** 使用Flyway migration添加email列
  - 新建 `V1.3__add_user_email.sql` 迁移文件
  - ALTER TABLE user ADD COLUMN email VARCHAR(100) UNIQUE KEY

</decisions>

<canonical_refs>
## Canonical References

**Downstream agents MUST read these before planning or implementing.**

### Core Project Files
- `.planning/PROJECT.md` — 项目概述、技术栈(Spring Boot 3.x + MySQL)
- `.planning/REQUIREMENTS.md` — Phase 7需求：AUTH-01, AUTH-02
- `.planning/ROADMAP.md` — Phase 7目标：注册错误提示优化

### Prior Phase Context
- `.planning/phases/06-permission/06-CONTEXT.md` — Phase 6权限体系重构的完整上下文
- `.planning/phases/06-permission/06-DISCUSSION-LOG.md` — Phase 6讨论记录

### Existing Code
- `backend/src/main/java/com/example/iot/entity/User.java` — 需要添加email字段
- `backend/src/main/java/com/example/iot/dto/UserRegisterDTO.java` — 需要添加email字段
- `backend/src/main/java/com/example/iot/service/impl/UserServiceImpl.java` — register()方法需要增加email唯一性检查
- `backend/src/main/java/com/example/iot/mapper/UserMapper.java` — 需要添加selectByEmail方法
- `backend/src/main/java/com/example/iot/common/result/ResultCode.java` — 需要添加EMAIL_ALREADY_EXISTS错误码
- `backend/src/main/resources/db/init.sql` — user表结构参考（无email列）

</canonical_refs>

<codebase_context>
## Existing Code Insights

### Reusable Assets
- `ResultCode` enum — 已有完整的错误码枚举体系
- `BusinessException` — 已有异常处理机制，支持自定义消息
- MyBatis-Plus `BaseMapper` — 已有UserMapper继承，可直接用lambdaQuery()

### Established Patterns
- 数据库字段命名：`create_time`, `update_time` (snake_case)
- Entity字段命名：`createTime`, `updateTime` (camelCase)
- Mapper使用 `@Select` 注解定义自定义SQL
- Service层校验 + 抛出BusinessException

### Integration Points
- `AuthController.register()` → `UserService.register()` — 修改点
- `UserRegisterDTO` → `UserServiceImpl.register()` — 增加email参数或单独查询email
- `UserMapper` — 需要增加selectByEmail方法

</codebase_context>

<specifics>
## Specific Ideas

**UserMapper.selectByUsername** 已有实现（line 18-19），参考此模式添加selectByEmail。

**ResultCode.USER_ALREADY_EXISTS** (code=1007, message="用户已存在") 需要保持，但BusinessException构造函数支持覆盖消息。

</specifics>

<deferred>
## Deferred Ideas

None — discussion stayed within phase scope.

</deferred>

---

*Phase: 07-auth-error*
*Context gathered: 2026-04-18*
