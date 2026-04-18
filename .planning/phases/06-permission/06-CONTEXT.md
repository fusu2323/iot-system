# Phase 6: 权限体系重构 - Context

**Gathered:** 2026-04-18
**Status:** Ready for planning

<domain>
## Phase Boundary

实现管理员/普通用户权限分离，接口层面实施权限校验。

**This phase delivers:**
- 管理员/普通用户角色分离
- 用户列表权限控制（管理员看全部，普通用户只看自己）
- 用户删除权限控制
- 日志接口访问控制（仅管理员）

</domain>

<decisions>
## Implementation Decisions

### 权限校验方式
- **D-01:** 使用 `@PreAuthorize` 注解进行方法级权限控制
  - 在需要权限的方法上添加 `@PreAuthorize("hasRole('ADMIN')")`
  - 已启用 `@EnableMethodSecurity` 配置
  - 替代方案：URL路径配置（粒度粗）、Service层校验（耦合高）

### 用户列表过滤
- **D-02:** Service层根据当前用户角色动态过滤
  - 管理员：返回所有用户
  - 普通用户：自动添加 `user_id = 当前用户` 条件
  - 需要从 SecurityContext 获取当前登录用户信息

### 删除权限控制
- **D-03:** 管理员删除用户限制
  - 管理员 **不能** 删除管理员账户
  - 管理员 **不能** 删除自己
  - 管理员 **可以** 删除普通用户
  - 普通用户只能删除自己

### 日志接口权限
- **D-04:** `/api/logs` 接口使用 `@PreAuthorize("hasRole('ADMIN')")`
  - 普通用户访问返回 403 Forbidden
  - 与问题1方案保持一致

### 当前用户获取
- **D-05:** 需要实现从 SecurityContext 获取当前登录用户ID
  - UserController.getCurrentUser() 目前是TODO状态
  - JwtUserDetailsService 已实现 loadUserByUsername

</decisions>

<canonical_refs>
## Canonical References

**Downstream agents MUST read these before planning or implementing.**

### Core Project Files
- `.planning/PROJECT.md` — 项目概述、技术栈(Spring Boot 3.x + Spring Security + JWT)
- `.planning/REQUIREMENTS.md` — Phase 6相关需求：AUTH-03~06, PERMISSION-01~03
- `.planning/ROADMAP.md` — Phase 6目标：实现管理员/普通用户权限分离

### Existing Code
- `backend/src/main/java/com/example/iot/config/SecurityConfig.java` — 已启用@EnableMethodSecurity
- `backend/src/main/java/com/example/iot/entity/User.java` — role字段(ADMIN/USER)已定义
- `backend/src/main/java/com/example/iot/security/JwtAuthenticationFilter.java` — JWT认证流程
- `backend/src/main/java/com/example/iot/controller/UserController.java` — getCurrentUser() TODO未实现
- `backend/src/main/java/com/example/iot/controller/LogController.java` — 日志接口，需要权限控制

</canonical_refs>

<code_context>
## Existing Code Insights

### Reusable Assets
- `JwtAuthenticationFilter` — 已实现JWT token解析和Authentication设置
- `JwtUserDetailsService` — 已实现UserDetailsService接口
- `User` entity — 已有 role 字段

### Established Patterns
- `@EnableMethodSecurity` 已启用，支持 @PreAuthorize
- BCrypt密码加密已配置
- JWT stateless认证已配置

### Integration Points
- UserService.list() — 需要增加当前用户ID参数用于过滤
- UserService.delete() — 需要增加权限校验逻辑
- LogService — 无需修改，只需在Controller层添加@PreAuthorize

</code_context>

<specifics>
## Specific Ideas

**UserController.getCurrentUser()** 目前返回TODO错误，需要实现从SecurityContext获取当前用户。

**User实体role字段** 已有，值为 "ADMIN" 或 "USER"。

</specifics>

<deferred>
## Deferred Ideas

None — discussion stayed within phase scope.

</deferred>

---

*Phase: 06-permission*
*Context gathered: 2026-04-18*
