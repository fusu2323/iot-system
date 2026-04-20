# Phase 7: 注册错误提示优化 - Discussion Log (Assumptions Mode)

> **Audit trail only.** Do not use as input to planning, research, or execution agents.
> Decisions captured in CONTEXT.md — this log preserves the analysis.

**Date:** 2026-04-18
**Phase:** 07-auth-error
**Mode:** assumptions
**Areas analyzed:** 错误消息内容, Email字段设计, 唯一性约束, 错误处理方式, 数据库迁移

## Assumptions Presented

### 错误消息内容
| Assumption | Confidence | Evidence |
|------------|-----------|----------|
| 用户名冲突返回"用户名已存在，请尝试其他用户名" | Confident | AUTH-01 requirement in REQUIREMENTS.md |
| 邮箱冲突返回"该邮箱已被注册" | Confident | AUTH-02 requirement in REQUIREMENTS.md |

### Email字段设计
| Assumption | Confidence | Evidence |
|------------|-----------|----------|
| email字段为可选（非必填） | Likely | 当前系统以用户名为主要标识，email为辅助；无强制要求 |
| 注册时填写的email必须唯一 | Confident | AUTH-02要求"邮箱已被注册"检查 |

### 唯一性约束
| Assumption | Confidence | Evidence |
|------------|-----------|----------|
| 数据库层添加email唯一约束（可NULL） | Confident | 与uk_username模式一致；SQL标准允许多个NULL |
| 使用Flyway migration添加列 | Confident | 项目使用Flyway管理数据库版本（V1.0~V1.2） |

### 错误处理方式
| Assumption | Confidence | Evidence |
|------------|-----------|----------|
| 复用BusinessException + ResultCode体系 | Confident | Phase 6 established this pattern |
| 用户名冲突复用USER_ALREADY_EXISTS但覆盖消息 | Confident | BusinessException支持自定义消息参数 |
| 邮箱冲突新增EMAIL_ALREADY_EXISTS错误码 | Confident | ResultCode结构支持 |

## Corrections Made

No corrections — all assumptions confirmed.

## External Research Applied

None required — requirements were explicit and codebase analysis provided sufficient evidence.

---

*Discussion log: 2026-04-18*
