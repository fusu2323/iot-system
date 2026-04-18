# Phase 8: 场景互斥机制 - Context

**Gathered:** 2026-04-18
**Status:** Ready for planning

<domain>
## Phase Boundary

实现场景分组互斥，启用时自动禁用同组场景。

**This phase delivers:**
- 场景表增加 `mutex_group` VARCHAR(50) 字段，支持NULL表示非互斥场景
- 启用场景时，同mutex_group的其他场景自动禁用（同一事务内）
- 场景列表返回时通过 `activeGroupSceneId` 字段标注当前启用的互斥组信息

</domain>

<decisions>
## Implementation Decisions

### mutex_group 字段设计
- **D-01:** `mutex_group` 字段类型为 `VARCHAR(50)`
  - 使用人类可读的字符串名称，如 "居家模式"、"睡眠模式"
  - 不使用外键关联独立表，保持简单
  - 不使用整数枚举，避免修改枚举值需要代码变更
  - 与现有 `icon` VARCHAR(50) 字段长度保持一致

### 事务边界
- **D-02:** 启用场景时的互斥处理在同一数据库事务内完成
  - `toggle()` 方法使用 `@Transactional`
  - 启用目标场景 + 查询并禁用同组场景 在同一个事务中
  - 保证原子性：同组场景要么全部启用成功，要么全部保持原状

### 互斥组标注格式
- **D-03:** `SceneVO` 增加 `activeGroupSceneId` 字段
  - 当场景的 `mutex_group` 不为 NULL 时，该字段标识同组内当前已启用的场景ID
  - 如果同组无其他启用场景，`activeGroupSceneId` 为 null
  - 前端可据此显示 "同组中 [某场景] 已启用" 等状态

### Migration策略
- **D-04:** 迁移脚本中，现有预设场景的 `mutex_group` 全部设为 NULL
  - NULL 表示非互斥场景，该场景不参与任何互斥组
  - 不预设分组，避免对现有行为造成意外影响
  - 用户可后续编辑场景时手动设置 mutex_group

</decisions>

<canonical_refs>
## Canonical References

**Downstream agents MUST read these before planning or implementing.**

### Core Project Files
- `.planning/PROJECT.md` — 项目概述、技术栈(Spring Boot 3.x + MyBatis-Plus)
- `.planning/REQUIREMENTS.md` — Phase 8相关需求：SCENE-01, SCENE-02, SCENE-04
- `.planning/ROADMAP.md` — Phase 8目标：场景互斥机制

### Prior Phase Context
- `.planning/phases/06-permission/06-CONTEXT.md` — Phase 6权限体系重构完整上下文
- `.planning/phases/07-auth-error/07-CONTEXT.md` — Phase 7注册错误提示完整上下文

### Existing Code
- `backend/src/main/java/com/example/iot/entity/Scene.java` — 需增加 mutexGroup 字段
- `backend/src/main/java/com/example/iot/vo/SceneVO.java` — 需增加 activeGroupSceneId 字段
- `backend/src/main/java/com/example/iot/dto/SceneCreateDTO.java` — 需增加 mutexGroup 字段
- `backend/src/main/java/com/example/iot/dto/SceneUpdateDTO.java` — 需增加 mutexGroup 字段
- `backend/src/main/java/com/example/iot/service/impl/SceneServiceImpl.java` — toggle() 需改写互斥逻辑
- `backend/src/main/java/com/example/iot/controller/SceneController.java` — toggle 接口
- `backend/src/main/resources/db/init.sql` — scene表结构（无mutex_group列）
- `backend/src/main/resources/db/migration/V1.0__create_scene_content_tables.sql` — 预设场景数据参考

</canonical_refs>

<codebase_context>
## Existing Code Insights

### Reusable Assets
- `Scene` entity — 已有 `isEnabled` 字段，toggle 逻辑已存在
- `SceneVO` — 已有 `devices` 列表字段，可以扩展
- `@Transactional` — Spring 已配置，Service 层可直接使用
- `BusinessException` — 已有错误处理机制

### Established Patterns
- 数据库字段命名：`create_time`, `update_time` (snake_case)
- Entity字段命名：`createTime`, `updateTime` (camelCase)
- Service 层使用 `LambdaQueryWrapper` 进行条件查询
- DTO 使用 Swagger `@Schema` 注解

### Integration Points
- `SceneServiceImpl.toggle()` — 需重构，增加互斥组处理
- `SceneVO` — 需增加 activeGroupSceneId 字段
- `SceneCreateDTO` / `SceneUpdateDTO` — 需增加 mutexGroup 字段
- 数据库 migration — 需新建 V1.4__add_scene_mutex_group.sql

</codebase_context>

<specifics>
## Specific Ideas

**toggle() 重构思路：** 当启用场景时（isEnabled=1），查询同 userId + 同 mutexGroup + isEnabled=1 的其他场景并批量禁用。禁用时无需处理互斥（只检查是否在同一组）。

**NULL semantics：** mutex_group 为 NULL 的场景不参与任何互斥组，启用时不影响其他场景。

</specifics>

<deferred>
## Deferred Ideas

None — discussion stayed within phase scope.

</deferred>

---

*Phase: 08-scene-mutex*
*Context gathered: 2026-04-18*
