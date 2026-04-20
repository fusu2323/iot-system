# ROADMAP.md — Milestone v1.1

## Milestones

- ✅ **v1.0 MVP** — Initial foundation (Phase 1-5)
- ✅ **v1.1 Bug修复与权限增强** — Phases 06-11 (shipped 2026-04-19)

## Phase Progress

| Phase | Milestone | Plans | Status | Completed |
|-------|-----------|-------|--------|-----------|
| 06 | v1.1 | 4/4 | ✅ Complete | 2026-04-18 |
| 07 | v1.1 | 1/1 | ✅ Complete | 2026-04-18 |
| 08 | v1.1 | 1/1 | ✅ Complete | 2026-04-18 |
| 09 | v1.1 | 1/1 | ✅ Complete | 2026-04-18 |
| 10 | v1.1 | 1/1 | ✅ Complete | 2026-04-18 |
| 11 | v1.1 | 4/4 | ✅ Complete | 2026-04-19 |

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

## Backlog

（无）

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