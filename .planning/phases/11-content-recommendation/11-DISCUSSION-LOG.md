# Phase 11: 内容推荐增强 - Discussion Log

> **Audit trail only.** Do not use as input to planning, research, or execution agents.
> Decisions are captured in CONTEXT.md — this log preserves the alternatives considered.

**Date:** 2026-04-18
**Phase:** 11-content-recommendation
**Areas discussed:** Recommendation type filtering, Grouped response structure, Preference weight configuration

---

## Recommendation type filtering

| Option | Description | Selected |
|--------|-------------|----------|
| Add optional type param | Add ?type=MOVIE param to /api/recommendations. Returns only matching type if specified, all types if omitted. Consistent with ContentController.list() pattern. | ✓ |
| Always filter (required) | Make type a required parameter. Changes API contract — breaking change for existing clients. | |
| Keep flat, frontend filters | No change to backend. Returns all types. Frontend handles type filtering/grouping from grouped response. | |

**User's choice:** Add optional type param
**Notes:** Consistent with existing ContentController.list() pattern. Backward compatible.

---

## Grouped response structure

| Option | Description | Selected |
|--------|-------------|----------|
| New grouped VO wrapper | Create new response structure like Map<type, List<RecommendationVO>> or a GroupedRecommendationVO with type + items list. Backend does the grouping. | ✓ |
| Flat + frontend grouping | Keep flat IPage<RecommendationVO>. Each item has type field. Frontend groups by type. Less work for backend. | |
| Hybrid: type in pagination | Return IPage but each record has type field. Add top-level 'group' metadata showing types available with counts. Compromise approach. | |

**User's choice:** New grouped VO wrapper
**Notes:** Backend handles the grouping work, frontend gets structured data.

---

## Preference weight configuration

| Option | Description | Selected |
|--------|-------------|----------|
| Influence weight per type | New columns: clickWeight, likeWeight, dislikeWeight per content type in UserPreference. User sets how much each action influences their recommendations for that type. | ✓ |
| Type priority/boost | Add priority/boostScore to UserPreference. Higher = more of that type appears in recommendations. Doesn't change feedback weight calculation. | |
| Both influence + priority | Add separate influence weights AND priority/boost. Maximum flexibility but more complex. | |

**User's choice:** Influence weight per type
**Notes:** Most aligned with RECOMMEND-03 "内容类型权重配置" intent.

---

## Deferred Ideas

None — discussion stayed within phase scope.
