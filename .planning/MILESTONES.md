# MILESTONES.md

## Milestone History

| Version | Name | Completed | Summary |
|---------|------|-----------|---------|
| v1.0 | 初始版本 | 2026-03-?? | 完成基础CRUD功能：用户、设备、场景、内容、推荐、日志管理 |
| v1.1 | Bug修复与权限增强 | 2026-04-19 | 修复注册错误提示、场景互斥、权限分离、内容推荐增强（20/20 requirements） |

---

## v1.0 Summary

**Completed Phases:** 1-5

**Delivered:**
- Spring Boot 后端基础框架
- 用户认证（注册/登录/JWT）
- 设备管理CRUD
- 场景管理CRUD
- 内容管理CRUD
- 推荐管理基础功能
- 操作日志记录
- Vue前端基础页面

---

## v1.1 Summary

**Completed Phases:** 06-11 (6 phases, 10 plans)

**Delivered:**
- Permission system: SecurityContextUtil + @PreAuthorize for admin/normal user separation
- AUTH-01/02: Clear error messages for duplicate username/email
- SCENE-01/04: Scene mutex groups with automatic disable via toggle()
- SCENE-03: Scene-device state sync on trigger() with syncDevices() helper
- CONTENT-01/02/03: 24 new content entries (8 movies, 8 music, 8 games)
- CONTENT-04: Content list type filtering (verified)
- RECOMMEND-01/02/03: Type filtering, grouped response (GroupedRecommendationVO/Response), configurable weights
- V1.6 migration: user_preference table adds click_weight/like_weight/dislike_weight

**Accomplishments:**
1. Permission system with role-based access control (@PreAuthorize)
2. Scene mutex mechanism with automatic same-group disable
3. Scene-device state synchronization on trigger
4. Content data expansion (24 entries across 3 types)
5. Type-filtered and grouped recommendation response
6. Configurable user preference weights per content type

**Key Decisions:**
- Device sync in trigger(), not toggle() — centralized state management
- toggle() uses @Transactional for atomic enable+auto-disable
- getRecommendations() returns GroupedRecommendationResponse with optional type param
- UserPreference weight fields: clickWeight/likeWeight/dislikeWeight (defaults 5/10/-20)

**Known Deferred:** None

**Tech Debt:** Pre-existing build errors in UserServiceImpl.java (BusinessException constructor mismatch) — not from v1.1 changes
