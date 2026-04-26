# ROADMAP.md — Milestone v1.2

## Milestones

- ✅ **v1.0 MVP** — Initial foundation (Phase 1-5)
- ✅ **v1.1 Bug修复与权限增强** — Phases 06-11 (shipped 2026-04-19)
- 🔄 **v1.2 论文初稿撰写** — Phase 12-17 (in progress)

## Phase Progress

| Phase | Milestone | Plans | Status | Completed |
|-------|-----------|-------|--------|-----------|
| 06 | v1.1 | 4/4 | ✅ Complete | 2026-04-18 |
| 07 | v1.1 | 1/1 | ✅ Complete | 2026-04-18 |
| 08 | v1.1 | 1/1 | ✅ Complete | 2026-04-18 |
| 09 | v1.1 | 1/1 | ✅ Complete | 2026-04-18 |
| 10 | v1.1 | 1/1 | ✅ Complete | 2026-04-18 |
| 11 | v1.1 | 4/4 | ✅ Complete | 2026-04-19 |
| 12 | v1.2 | 1/1 | ✅ Complete | 2026-04-20 |
| 13 | v1.2 | 1/1 | ✅ Complete | 2026-04-20 |
| 14 | v1.2 | 1/1 | ✅ Complete | 2026-04-20 |
| 15 | v1.2 | 1/1 | ✅ Complete | 2026-04-20 |
| 16 | v1.2 | 1/1 | ✅ Complete | 2026-04-20 |
| 17 | v1.2 | 1/1 | ✅ Complete | 2026-04-20 |
| 18 | v1.2 | 1/1 | ✅ Complete | 2026-04-20 |

## v1.2 论文初稿撰写 Phases

### Phase 12: 论文结构规划与研究背景撰写
**Goal:** 完成论文目录结构、研究背景、研究意义、研究现状章节

**Requirements:** THESIS-01, THESIS-02, THESIS-03, THESIS-04

**Success criteria:**
1. 完成论文完整目录结构设计
2. 完成研究背景章节（行业现状、问题陈述）
3. 完成研究意义章节（系统价值阐述）
4. 完成研究现状章节（国内外技术调研）

---

### Phase 13: 可行性分析与需求分析
**Goal:** 完成可行性分析、需求分析章节（用例图、功能分析、性能分析）

**Requirements:** THESIS-05, THESIS-06, THESIS-07, THESIS-08, THESIS-09, THESIS-10

**Success criteria:**
1. 完成技术可行性分析（Spring Boot + MySQL + JWT技术栈）
2. 完成经济可行性分析
3. 完成操作可行性分析
4. 完成用例图设计（管理员/普通用户）
5. 完成功能分析（设备管理、场景管理、内容推荐、用户管理）
6. 完成性能分析（响应时间、并发支持）

---

### Phase 14: 概要设计
**Goal:** 完成概要设计章节（ER图、功能模块图、时序图）

**Requirements:** THESIS-11, THESIS-12, THESIS-13

**Success criteria:**
1. 完成数据库ER图设计（user, device, scene, scene_device, content, user_preference, recommendation, operation_log等实体关系）
2. 完成功能模块图设计（系统架构图）
3. 完成核心时序图（用户登录、设备控制、场景触发、推荐生成）

---

### Phase 15: 详细设计
**Goal:** 完成详细设计章节（程序流程图）

**Requirements:** THESIS-14

**Success criteria:**
1. 完成设备管理程序流程图
2. 完成场景触发程序流程图
3. 完成推荐算法程序流程图

---

### Phase 16: 软件测试
**Goal:** 完成软件测试章节（白盒测试报告、黑盒测试报告）

**Requirements:** THESIS-15, THESIS-16

**Success criteria:**
1. 完成白盒测试报告（单元测试、集成测试用例）
2. 完成黑盒测试报告（功能测试、接口测试用例）
3. 完成测试总结

---

### Phase 17: 总结与展望
**Goal:** 完成总结与展望章节

**Requirements:** THESIS-17, THESIS-18

**Success criteria:**
1. 完成总结章节（系统功能、技术实现总结）
2. 完成展望与未来工作章节

---

### Phase 18: 论文文字表述扩充
**Goal:** 将论文从"以图表为主"转变为"文字为主、图表为辅"，参考论文示例增加段落性叙述

**Requirements:** THESIS-01 ~ THESIS-18 (all)

**Success criteria:**
1. 第1章绪论增加研究背景段落描述、国内外研究现状的对比分析文字
2. 第2章技术介绍增加方法论描述和各技术选型理由的详细文字
3. 第3章系统分析增加可行性论证的详细分析文字、功能需求的用例描述
4. 第4章系统设计增加架构设计决策理由、数据库设计说明、时序图对应的文字分析
5. 第5章详细设计增加程序流程图的文字解释说明
6. 第6章测试增加测试方法和测试结果分析的文字描述
7. 第7章总结增加工作总结和技术心得的详细叙述

---

## Phase Dependency Graph

```
Phase 12 (研究背景与现状)
           │
           ▼
Phase 13 (可行性与需求分析)
           │
           ▼
Phase 14 (概要设计)
           │
           ▼
Phase 15 (详细设计)
           │
           ▼
Phase 16 (软件测试)
           │
           ▼
Phase 17 (总结与展望)
           │
           ▼
Phase 18 (文字表述扩充)
```

## Backlog

（无）

## Completed Milestones

<details>
<summary>✅ v1.1 Bug修复与权限增强 (Phases 06-11) — SHIPPED 2026-04-19</summary>

### Phase 06: 权限体系重构
- [x] 06-01-PLAN.md — Create SecurityContextUtil for current user extraction
- [x] 06-02-PLAN.md — Add @PreAuthorize to LogController (admin-only logs)
- [x] 06-03-PLAN.md — Update UserController with @PreAuthorize and SecurityContextUtil
- [x] 06-04-PLAN.md — Update UserService.list() and delete() with role filtering

### Phase 07: 注册错误提示优化
- [x] 07-01-PLAN.md — Add email field, uniqueness checks, and clear error messages

### Phase 08: 场景互斥机制
- [x] 08-01-PLAN.md — Implement scene mutex mechanism with toggle() and mutex_group field

### Phase 09: 场景设备联动
- [x] 09-01-PLAN.md — Implement scene-device state sync

### Phase 10: 内容数据扩充
- [x] 10-01-PLAN.md — Expand content table with 24 new entries (8 movies, 8 music, 8 games)

### Phase 11: 内容推荐增强
- [x] 11-01-PLAN.md — DB migration V1.6 + entity/VO foundation (weight fields, GroupedRecommendationVO/Response)
- [x] 11-02-PLAN.md — RecommendationService/Controller type filtering and grouped response
- [x] 11-03-PLAN.md — Preference weight configuration API
- [x] 11-04-PLAN.md — CONTENT-04: Content list type filtering verification

</details>