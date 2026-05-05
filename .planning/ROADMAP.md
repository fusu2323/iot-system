# ROADMAP.md — Milestone v2.0

## Milestones

- ✅ **v1.0 MVP** — Initial foundation (Phase 1-5)
- ✅ **v1.1 Bug修复与权限增强** — Phases 06-11 (shipped 2026-04-19)
- ✅ **v1.2 论文初稿撰写** — Phases 12-19 (shipped 2026-05-05)
- 🔄 **v2.0 新功能开发** — Phase 20-23 (in progress)

## Phase Progress

| Phase | Milestone | Plans | Status | Completed |
|-------|-----------|-------|--------|-----------|
| 20 | v2.0 | 0/3 | Not started | — |
| 21 | v2.0 | 0/3 | Not started | — |
| 22 | v2.0 | 0/3 | Not started | — |
| 23 | v2.0 | 0/4 | Not started | — |

---

## Phases

- [ ] **Phase 20: 定时任务CRUD + 启用/禁用** — 管理员创建/编辑/删除定时任务，支持多种时间类型，启用/禁用控制
- [ ] **Phase 21: cron调度执行 + 执行记录** — 定时任务按cron自动触发场景，记录执行结果供查看
- [ ] **Phase 22: 统计事件记录** — 设备激活和场景触发自动写入stat_log
- [ ] **Phase 23: 统计查询API** — 用户查看使用统计（按日/周/月），管理员查看全局排行，单条时间线

---

## Phase Details

### Phase 20: 定时任务CRUD + 启用/禁用

**Goal:** 管理员可创建、编辑、删除定时任务，指定关联场景和时间计划，支持启用/禁用控制

**Depends on:** Nothing (first v2.0 phase)

**Requirements:** SCHED-01, SCHED-02, SCHED-04

**Success Criteria** (what must be TRUE):
1. 管理员可在任务管理页面创建新定时任务，填写任务名称、关联场景、时间计划（每日/工作日/周末/cron）
2. 管理员可编辑已有定时任务的时间计划和关联场景
3. 管理员可删除不再需要的定时任务
4. 管理员可为每个任务单独启用或禁用，禁用后任务不参与调度
5. 定时任务列表页显示所有任务及其当前启用状态、关联场景名称

**Plans:** TBD

---

### Phase 21: cron调度执行 + 执行记录

**Goal:** 定时任务按cron表达式自动触发关联场景，记录每次执行结果

**Depends on:** Phase 20

**Requirements:** SCHED-03, SCHED-05

**Success Criteria** (what must be TRUE):
1. 启用的定时任务在对应时间自动触发其关联场景
2. 系统记录每次触发的执行结果（成功/失败）和触发时间
3. 管理员可查看某定时任务的历史执行记录列表
4. 执行失败时记录失败原因（错误信息）

**Plans:** TBD

---

### Phase 22: 统计事件记录

**Goal:** 系统自动记录设备激活和场景触发事件，作为后续统计查询的数据源

**Depends on:** Nothing (可与Phase 20并行开发)

**Requirements:** STATS-01, STATS-02

**Success Criteria** (what must be TRUE):
1. 设备status变为1时，系统自动写入stat_log一条激活记录（设备ID、类型、时间）
2. 场景trigger()被调用时，系统自动写入stat_log一条触发记录（场景ID、类型、时间）
3. stat_log表包含足够字段支持后续按用户/日/周/月聚合查询
4. 事件记录不影响原有设备控制和场景触发功能

**Plans:** TBD

---

### Phase 23: 统计查询API

**Goal:** 用户查看设备和场景的使用统计（按日/周/月），管理员查看全局使用概览，单条历史时间线

**Depends on:** Phase 22

**Requirements:** STATS-03, STATS-04, STATS-05

**Success Criteria** (what must be TRUE):
1. 普通用户可查询自己设备和场景的使用次数，按日/周/月维度返回
2. 管理员可查询全局设备和场景的使用次数排行（前10名）
3. 用户可查看单条设备或单条场景的历史激活时间线（时间倒序列表）
4. 查询接口返回的数据格式一致，支持前端直接渲染

**Plans:** TBD

---

## Phase Dependency Graph

```
Phase 20 (定时任务CRUD)
         │
         ▼
Phase 21 (cron调度执行)
         │
         │
         ▼
Phase 23 (统计查询API)
         ▲           │
         │           │
Phase 22 (事件记录) ─┘
 (可与20并行)
```

---

## Backlog

（无）

---

## Completed Milestones

<details>
<summary>✅ v1.2 论文初稿撰写 (Phases 12-19) — SHIPPED 2026-05-05</summary>

### Phase 12-18: 论文各章节
- [x] Phase 12-18 — 论文结构规划、研究背景、可行性分析、需求分析、概要设计、详细设计、软件测试、总结与展望

### Phase 19: 场景互斥前端暴露
- [x] Phase 19 — 前端UI暴露互斥组选择器和状态提示

</details>