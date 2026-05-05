# 智能家居娱乐管理系统 - PROJECT.md

## 1. What This Is

基于 Spring Boot 的智能家居娱乐管理系统，提供设备信息管理、场景配置、内容推荐和用户管理的 Web 应用系统，配套微信小程序客户端。本系统专注于业务逻辑的 CRUD 操作，不涉及真实物联网设备通信。

## 2. Core Value

- 提供设备信息的集中化管理
- 支持场景配置与管理（互斥场景支持）
- 提供个性化内容推荐
- 多用户权限管理（管理员/普通用户）
- 微信小程序便捷访问

## 3. Validated Requirements

### v1.0 Foundation
- ✓ 用户注册与认证 (AUTH-01, AUTH-02)
- ✓ 设备管理基础 (DEVICE-01, DEVICE-02)
- ✓ 场景管理基础 (SCENE-01 via 08)

### v1.1 Bug修复与权限增强
- ✓ AUTH-01/02: 明确的注册错误提示
- ✓ AUTH-03/04/05/06: 管理员/普通用户权限分离
- ✓ SCENE-01/02/04: 场景互斥机制
- ✓ SCENE-03: 场景设备联动
- ✓ CONTENT-01/02/03/04: 内容数据扩充与类型筛选
- ✓ RECOMMEND-01/02/03: 推荐增强与权重配置
- ✓ PERMISSION-01/02/03: 接口权限校验

### v1.2 论文初稿撰写
- ✓ THESIS-01~18: 论文各章节完成

## 4. Active Requirements (v2.0)

### 定时任务 (Scheduled Tasks)
- [ ] **SCHED-01**: 管理员可创建定时任务，指定场景和时间计划
- [ ] **SCHED-02**: 支持多种时间类型：每日、工作日、周末、自定义cron
- [ ] **SCHED-03**: 定时任务到点自动触发关联场景
- [ ] **SCHED-04**: 管理员可启用/禁用定时任务
- [ ] **SCHED-05**: 定时任务可查看执行历史记录

### 使用统计 (Usage Analytics)
- [ ] **STATS-01**: 系统记录设备和场景的每次激活
- [ ] **STATS-02**: 用户可查看设备和场景的使用次数统计
- [ ] **STATS-03**: 支持按日/周/月维度查看使用趋势
- [ ] **STATS-04**: 管理员可查看全局使用统计概览

## 5. Out of Scope

| Feature | Reason |
|---------|--------|
| 物联网设备真实通信 | 论文聚焦软件系统设计，不涉及硬件通信 |
| 微信小程序开发 | 已列入未来工作 |
| 设备分组/区域管理 | 下个版本考虑 |
| 通知系统 | 下个版本考虑 |
| 预设场景模板 | 下个版本考虑 |

## 6. Key Decisions

| Decision | Rationale | Outcome |
|----------|-----------|---------|
| Spring Security + JWT 认证 | 继续使用 | ✅ |
| MyBatis-Plus ORM | 继续使用 | ✅ |
| 场景互斥通过 scene_mutex_group 字段 | 继续使用 | ✅ |
| Device sync in trigger(), not toggle() | 集中管理 | ✅ |
| getRecommendations() 返回 GroupedRecommendationResponse | 继续使用 | ✅ |
| 定时任务用 cron 表达式 | 灵活，支持复杂时间规则 | ✅ |
| 使用统计用独立 stat_log 表 | 不污染核心业务表 | ✅ |

## 7. Context

- 技术栈: Spring Boot 3.x + MySQL 8.x + Spring Security + JWT + MyBatis-Plus
- 项目结构: 前后端分离（Vue 3 前端已初始化）
- 代码规模: v1.0 + v1.1 完成，v1.2 论文完成
- 定时任务实现: 使用 Spring @Scheduled + cron 表达式
- 统计实现: 新建 operation_stat_log 表，记录设备/场景激活事件

## 8. Evolution

This document evolves at phase transitions and milestone boundaries.

**After each phase transition** (via `/gsd-transition`):
1. Requirements invalidated? → Move to Out of Scope with reason
2. Requirements validated? → Move to Validated with phase reference
3. New requirements emerged? → Add to Active
4. Decisions to log? → Add to Key Decisions
5. "What This Is" still accurate? → Update if drifted

**After each milestone** (via `/gsd-complete-milestone`):
1. Full review of all sections
2. Core Value check — still the right priority?
3. Audit Out of Scope — reasons still valid?
4. Update Context with current state

---

*Last updated: 2026-05-05 after v1.2 milestone complete, starting v2.0*