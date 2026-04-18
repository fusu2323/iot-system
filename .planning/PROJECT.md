# 智能家居娱乐管理系统 - PROJECT.md

## 1. What This Is

基于 Spring Boot 的智能家居娱乐管理系统，提供设备信息管理、场景配置、内容推荐和用户管理的 Web 应用系统，配套微信小程序客户端。本系统专注于业务逻辑的 CRUD 操作，不涉及真实物联网设备通信。

## 2. Core Value

- 提供设备信息的集中化管理
- 支持场景配置与管理（互斥场景支持）
- 提供个性化内容推荐
- 多用户权限管理（管理员/普通用户）
- 微信小程序便捷访问

## 3. Current Milestone: v1.1 Bug修复与权限增强

**目标:** 修复关键Bug并增强用户权限与内容管理

**Target features:**
- AUTH-01: 注册失败时显示具体原因（用户名重复/已注册）
- SCENE-01: 场景互斥 - 居家/离家等互斥场景不能同时启用
- SCENE-02: 触发场景时，关联设备的启用/禁用状态应同步更新
- CONTENT-01: 补充更多内容管理示例数据
- RECOMMEND-01: 推荐管理细化，明确分类
- AUTH-02: 管理员权限定义（可添加/删除哪些）
- AUTH-03: 普通用户权限限制（不能删除他人信息，不能查看所有日志）

## 4. Active Requirements

### v1.1 Bug修复与权限增强

- [ ] **AUTH-01**: 注册时如果用户名已存在，返回明确错误信息"用户名已存在"
- [ ] **SCENE-01**: 场景管理实现互斥机制 - 标记互斥场景组，启用一个时自动禁用同组其他场景
- [ ] **SCENE-02**: 触发场景时，同步更新关联设备的启用/禁用状态
- [ ] **CONTENT-01**: 补充内容管理示例数据（电影/音乐/游戏各增加5-10条）
- [ ] **RECOMMEND-01**: 推荐管理增加分类筛选功能
- [ ] **AUTH-02**: 定义管理员可执行的操作（用户管理、日志查看、系统配置）
- [ ] **AUTH-03**: 普通用户权限限制（只能管理自己的设备和场景，不能删除其他用户，不能查看他人日志）

## 5. Validated Requirements

（无）

## 6. Out of Scope

- 物联网设备真实通信
- 微信小程序开发（Phase 8）
- Web管理后台Vue开发（Phase 7）

## 7. Key Decisions

- 采用 Spring Security + JWT 实现认证授权
- 使用 MyBatis-Plus 作为 ORM
- 场景互斥通过 scene_mutex_group 字段实现

## 8. Context

- 技术栈: Spring Boot 3.x + MySQL 8.x + Vue 3
- 项目结构: 前后端分离
- 当前进度: Phase 1-6 已完成，v1.0 发布

## 9. Evolution

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

**Last updated:** 2026-04-18
