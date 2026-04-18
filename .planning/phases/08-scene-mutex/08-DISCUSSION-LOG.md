# Phase 8: 场景互斥机制 - Discussion Log

> **Audit trail only.** Do not use as input to planning, research, or execution agents.
> Decisions are captured in CONTEXT.md — this log preserves the alternatives considered.

**Date:** 2026-04-18
**Phase:** 08-scene-mutex
**Areas discussed:** mutex_group 字段类型, 事务边界, 互斥组标注格式, Migration策略

---

## mutex_group 字段类型

| Option | Description | Selected |
|--------|-------------|----------|
| VARCHAR (推荐) | String like '居家模式', '离家模式' — simple, human-readable, no extra table needed | ✓ |
| Integer FK | Integer referencing a mutex_group table — more normalized, but extra table & join | |
| Integer enum | Integer with app-level enum constants — type-safe but less flexible for user-defined groups | |

**User's choice:** VARCHAR (推荐)
**Notes:** VARCHAR(50) selected — matches icon field length, human-readable, no join needed

---

## VARCHAR长度

| Option | Description | Selected |
|--------|-------------|----------|
| 50 chars (推荐) | Same as icon field — enough for Chinese names like '居家模式' | ✓ |
| 100 chars | More headroom for longer group names | |
| 20 chars | Shorter, but '居家模式' is already 4 chars UTF-8 | |

**User's choice:** 50 chars (推荐)

---

## 事务边界

| Option | Description | Selected |
|--------|-------------|----------|
| Yes, same transaction (推荐) | All-or-nothing: enable A + disable B,C succeed or rollback together | ✓ |
| No, separate operations | Enable A, then independently disable B,C — could leave inconsistent state if second step fails | |

**User's choice:** Yes, same transaction (推荐)
**Notes:** Toggle method will use @Transactional to ensure atomicity

---

## 互斥组标注格式

| Option | Description | Selected |
|--------|-------------|----------|
| Per-scene activeGroupSceneId (推荐) | Each SceneVO has field: activeGroupSceneId (ID of currently-enabled scene in same group, or null) | ✓ |
| Per-scene activeGroupName | Each SceneVO has field: activeGroupSceneName — name of the active scene in same group | |
| Separate activeGroups list | Response includes top-level field: activeMutexGroups: [{group, activeSceneId, activeSceneName}] | |
| Frontend calculates | Just return mutex_group per scene — frontend groups and determines active scene client-side | |

**User's choice:** Per-scene activeGroupSceneId (推荐)
**Notes:** Each SceneVO has activeGroupSceneId — null if no other scene in the same mutex_group is currently enabled

---

## Migration策略

| Option | Description | Selected |
|--------|-------------|----------|
| NULL (推荐) | NULL = non-exclusive (no mutex). Scenes don't auto-conflict unless explicitly grouped. Cleanest default. | ✓ |
| Pre-grouped by category | Assign groups: '居家模式'=home场景, '睡眠模式'=sleep场景, etc. — shows real mutex behavior | |
| All same group | All 5 scenes in one group — enables mutex immediately but may be unexpected | |

**User's choice:** NULL (推荐)
**Notes:** Existing preset scenes (回家模式, 离家模式, 影院模式, 睡眠模式, 阅读模式) all get NULL for mutex_group, meaning they are non-exclusive by default

---

## Claude's Discretion

No areas deferred to Claude — all decisions made by user.

## Deferred Ideas

None — discussion stayed within phase scope.
