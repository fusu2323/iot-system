# 智能家居娱乐管理系统 - PROJECT.md

## 1. What This Is

基于 Spring Boot 的智能家居娱乐管理系统，提供设备信息管理、场景配置、内容推荐和用户管理的 Web 应用系统，配套微信小程序客户端。本系统专注于业务逻辑的 CRUD 操作，不涉及真实物联网设备通信。

## 2. Core Value

- 提供设备信息的集中化管理
- 支持场景配置与管理（互斥场景支持）
- 提供个性化内容推荐
- 多用户权限管理（管理员/普通用户）
- 微信小程序便捷访问

## 3. Current Milestone: v1.1 COMPLETE

**v1.1 Bug修复与权限增强 — SHIPPED 2026-04-19**

6 phases completed (06-11), 10 plans, 20/20 requirements validated.

## 4. Validated Requirements

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

## 5. Active Requirements

（无 — v1.1 完成，下一 milestone 尚未规划）

## 6. Out of Scope

- 物联网设备真实通信
- 微信小程序开发
- Web管理后台Vue开发
- 定时触发场景功能
- 预设场景模板

## 7. Key Decisions

| Decision | Outcome |
|----------|---------|
| Spring Security + JWT 认证 | ✅ 继续使用 |
| MyBatis-Plus ORM | ✅ 继续使用 |
| 场景互斥通过 scene_mutex_group 字段 | ✅ 已实现 |
| Device sync in trigger(), not toggle() | ✅ 集中管理 |
| getRecommendations() 返回 GroupedRecommendationResponse | ✅ 已实现 |
| UserPreference weight 字段: clickWeight/likeWeight/dislikeWeight | ✅ 已实现 |

## 8. Context

- 技术栈: Spring Boot 3.x + MySQL 8.x + Spring Security + JWT
- 项目结构: 前后端分离
- 当前进度: v1.1 完成 (Phase 06-11), 20/20 requirements validated
- 代码规模: ~15 commits across v1.1 phases
- v1.1 Timeline: 2026-04-18 → 2026-04-19 (1 day)

## 9. Next Milestone

尚未规划。使用 `/gsd-new-milestone` 开始 v1.2 或后续版本规划。

---

**Last updated:** 2026-04-19 after v1.1 milestone completion