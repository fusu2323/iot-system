# ROADMAP.md — Milestone v1.1

## v1.1 Bug修复与权限增强

**8 phases** | **20 requirements mapped** | All covered ✓

| # | Phase | Goal | Requirements | Success Criteria |
|---|-------|------|--------------|------------------|
| 6 | 权限体系重构 | 实现管理员/普通用户权限分离 | AUTH-03, AUTH-04, AUTH-05, AUTH-06, PERMISSION-01, PERMISSION-02, PERMISSION-03 | 3 |
| 7 | 注册错误提示优化 | 修复重复注册错误提示不明确问题 | AUTH-01, AUTH-02 | 2 |
| 8 | 场景互斥机制 | 实现场景分组互斥，启用时自动禁用同组场景 | SCENE-01, SCENE-02, SCENE-04 | 3 |
| 9 | 场景设备联动 | 触发场景时同步更新关联设备状态 | SCENE-03 | 2 |
| 10 | 内容数据扩充 | 补充电影/音乐/游戏示例数据 | CONTENT-01, CONTENT-02, CONTENT-03 | 3 |
| 11 | 内容推荐增强 | 推荐列表分类筛选与分组展示 | CONTENT-04, RECOMMEND-01, RECOMMEND-02, RECOMMEND-03 | 3 |

---

## Phase Details

### Phase 6: 权限体系重构

**Goal:** 实现管理员/普通用户权限分离，接口层面实施权限校验

**Requirements:** AUTH-03, AUTH-04, AUTH-05, AUTH-06, PERMISSION-01, PERMISSION-02, PERMISSION-03

**Success criteria:**
1. 管理员可以查看所有用户列表，普通用户只能查看自己
2. 管理员可以删除普通用户，普通用户不能删除任何用户
3. 普通用户不能访问 /api/logs 接口（返回403）

**Plans:**
- [x] 06-01-PLAN.md — Create SecurityContextUtil for current user extraction (2026-04-18)
- [x] 06-02-PLAN.md — Add @PreAuthorize to LogController (admin-only logs) (2026-04-18)
- [x] 06-03-PLAN.md — Update UserController with @PreAuthorize and SecurityContextUtil (2026-04-18)
- [x] 06-04-PLAN.md — Update UserService.list() and delete() with role filtering (2026-04-18)

---

### Phase 7: 注册错误提示优化

**Goal:** 修复重复注册错误提示不明确问题

**Requirements:** AUTH-01, AUTH-02

**Success criteria:**
1. 用户名已存在时返回"用户名已存在，请尝试其他用户名"
2. 邮箱已被注册时返回"该邮箱已被注册"

---

### Phase 8: 场景互斥机制

**Goal:** 实现场景分组互斥，启用时自动禁用同组场景

**Requirements:** SCENE-01, SCENE-02, SCENE-04

**Success criteria:**
1. 场景表增加 mutex_group 字段，支持NULL表示非互斥场景
2. 启用场景时，同mutex_group的其他场景自动禁用
3. 场景列表返回时标注当前启用的互斥组

---

### Phase 9: 场景设备联动

**Goal:** 触发场景时同步更新关联设备状态

**Requirements:** SCENE-03

**Success criteria:**
1. 触发场景时，场景关联的设备状态同步更新为启用
2. 禁用场景时，关联设备状态同步更新为禁用

---

### Phase 10: 内容数据扩充

**Goal:** 补充电影/音乐/游戏示例数据

**Requirements:** CONTENT-01, CONTENT-02, CONTENT-03

**Success criteria:**
1. 电影类型内容增加5-10条示例数据
2. 音乐类型内容增加5-10条示例数据
3. 游戏类型内容增加5-10条示例数据

---

### Phase 11: 内容推荐增强

**Goal:** 推荐列表分类筛选与分组展示

**Requirements:** CONTENT-04, RECOMMEND-01, RECOMMEND-02, RECOMMEND-03

**Success criteria:**
1. 内容列表增加类型筛选参数
2. 推荐列表按内容类型分组展示
3. 用户偏好设置支持内容类型权重配置

---

## Phase Dependency Graph

```
Phase 6 (权限体系)
       │
       ▼
Phase 7 (注册错误提示) ←─┐
       │                │
       ▼                │
Phase 8 (场景互斥) ─────┘
       │
       ▼
Phase 9 (场景设备联动)
       │
       ▼
Phase 10 (内容数据扩充)
       │
       ▼
Phase 11 (内容推荐增强)
```
