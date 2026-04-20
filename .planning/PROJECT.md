# 智能家居娱乐管理系统 - PROJECT.md

## 1. What This Is

基于 Spring Boot 的智能家居娱乐管理系统，提供设备信息管理、场景配置、内容推荐和用户管理的 Web 应用系统，配套微信小程序客户端。本系统专注于业务逻辑的 CRUD 操作，不涉及真实物联网设备通信。

## 2. Core Value

- 提供设备信息的集中化管理
- 支持场景配置与管理（互斥场景支持）
- 提供个性化内容推荐
- 多用户权限管理（管理员/普通用户）
- 微信小程序便捷访问

## 3. Current Milestone: v1.2 论文初稿撰写

**Goal:** 完成智能家居娱乐管理系统论文初稿，涵盖研究背景、可行性分析、需求分析、概要设计、详细设计、软件测试等内容。

**Target features:**
- 研究背景与意义
- 研究现状分析
- 可行性分析（技术、经济、操作）
- 需求分析（用例图、功能分析、性能分析）
- 概要设计（数据库ER图、功能模块图、时序图）
- 详细设计（程序流程图）
- 软件测试（白盒测试报告、黑盒测试报告）

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

### v1.2 论文初稿撰写
- [ ] **THESIS-01**: 研究背景章节
- [ ] **THESIS-02**: 研究意义章节
- [ ] **THESIS-03**: 研究现状章节
- [ ] **THESIS-04**: 可行性分析章节（技术/经济/操作）
- [ ] **THESIS-05**: 需求分析（用例图、功能分析、性能分析）
- [ ] **THESIS-06**: 概要设计（ER图、功能模块图、时序图）
- [ ] **THESIS-07**: 详细设计（程序流程图）
- [ ] **THESIS-08**: 软件测试（白盒测试报告、黑盒测试报告）

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
- 当前进度: v1.2 论文初稿撰写中
- 代码规模: v1.0 + v1.1 完成

## 9. Next Milestone

v1.2 论文初稿撰写 — 进行中

---

**Last updated:** 2026-04-20 after v1.2 milestone start