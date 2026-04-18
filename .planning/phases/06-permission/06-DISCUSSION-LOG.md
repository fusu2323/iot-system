# Phase 6: 权限体系重构 - Discussion Log

> **Audit trail only.** Do not use as input to planning, research, or execution agents.
> Decisions are captured in CONTEXT.md — this log preserves the alternatives considered.

**Date:** 2026-04-18
**Phase:** 06-permission
**Areas discussed:** 权限校验方式, 用户列表过滤, 删除权限控制, 日志接口权限

---

## 权限校验方式

| Option | Description | Selected |
|--------|-------------|----------|
| A. @PreAuthorize注解 (推荐) | 细粒度控制，符合Spring Security最佳实践 | ✓ |
| B. URL路径配置 | 简单配置，但粒度粗 | |
| C. Service层校验 | 可做复杂逻辑，但与安全框架耦合 | |

**User's choice:** A. @PreAuthorize注解 (推荐)
**Notes:** 与Spring Security最佳实践一致，方法级细粒度控制

---

## 用户列表过滤

| Option | Description | Selected |
|--------|-------------|----------|
| A. Service层过滤 (推荐) | 在Service层根据角色动态添加过滤条件，更安全 | ✓ |
| B. Controller层过滤 | 在Controller层根据角色调用不同方法 | |
| C. 统一查询+结果过滤 | 先查所有，再按角色过滤返回数据 | |

**User's choice:** A. Service层过滤 (推荐)
**Notes:** 更安全，避免数据泄露到Controller层

---

## 删除权限控制

| Option | Description | Selected |
|--------|-------------|----------|
| A. 不能删除管理员和自己 (推荐) | 安全限制，防止无管理员账户 | ✓ |
| B. 不能删除自己，可删其他管理员 | 更宽松，允许删除其他管理员 | |
| C. 管理员可删除任何人 | 完全开放，不做限制 | |

**User's choice:** A. 不能删除管理员和自己 (推荐)
**Notes:** 安全限制，防止误删导致系统无管理员

---

## 日志接口权限

| Option | Description | Selected |
|--------|-------------|----------|
| A. @PreAuthorize注解 (推荐) | 最直接，与问题1方案一致 | ✓ |
| B. SecurityConfig URL配置 | 全局配置，一次性生效 | |
| C. 两者结合 | 双重保护，最严格 | |

**User's choice:** A. @PreAuthorize注解 (推荐)
**Notes:** 与问题1方案保持一致，统一使用@PreAuthorize

---

## Claude's Discretion

All 4 gray areas were fully discussed with user. No areas deferred to Claude discretion.

## Deferred Ideas

None — all 4 areas discussed and decided.
