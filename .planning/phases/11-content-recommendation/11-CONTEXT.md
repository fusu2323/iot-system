# Phase 11: 内容推荐增强 - Context

**Gathered:** 2026-04-18
**Status:** Ready for planning

<domain>
## Phase Boundary

实现 CONTENT-04, RECOMMEND-01, RECOMMEND-02, RECOMMEND-03：推荐列表分类筛选与分组展示。

**This phase delivers:**
- `getRecommendations()` 增加可选 `type` 参数，支持按内容类型筛选推荐结果
- 推荐结果按内容类型分组返回（新增分组 VO 结构）
- 用户偏好设置增加内容类型影响力权重配置

</domain>

<decisions>
## Implementation Decisions

### Recommendation Type Filtering
- **D-01:** `getRecommendations()` 增加可选 `type` 查询参数
  - `GET /api/recommendations?userId=1&type=MOVIE` → 只返回该类型的推荐
  - `GET /api/recommendations?userId=1` → 返回所有类型（向后兼容）
  - 与 `ContentController.list()` 的 `type` 参数模式保持一致

### Grouped Response Structure
- **D-02:** 推荐结果按内容类型分组返回
  - 新增 `GroupedRecommendationVO` 或类似结构
  - 结构示例: `{ groups: { MOVIE: [...], MUSIC: [...], GAME: [...] }, total: N }`
  - 每个分组内按推荐分数排序
  - 移除顶层 `IPage` 分页，改为分组内部分页（可选）

### Preference Weight Configuration
- **D-03:** `UserPreference` 表增加影响力权重字段
  - `click_weight` — 点击行为对该类型推荐的影响力权重
  - `like_weight` — 收藏行为对该类型推荐的影响力权重
  - `dislike_weight` — 不喜欢行为对该类型推荐的影响力权重
  - 默认值保持现有硬编码值（CLICK=5, LIKE=10, DISLIKE=-20）
  - 用户可个性化配置不同类型的行为影响力
  - `RecommendationServiceImpl.calculateRecommendationScore()` 和 `updateUserPreferenceScore()` 需要修改以使用可配置的权重

### API Endpoint Changes
- **D-04:** `PreferenceController` 或 `RecommendationController` 需要支持设置权重
  - 可能需要新增 `PUT /api/preferences/{contentType}/weights` 或扩展现有 `POST /api/preferences` 接口

</decisions>

<canonical_refs>
## Canonical References

**Downstream agents MUST read these before planning or implementing.**

### Core Project Files
- `.planning/PROJECT.md` — 项目概述、技术栈
- `.planning/REQUIREMENTS.md` — Phase 11 需求：CONTENT-04, RECOMMEND-01, RECOMMEND-02, RECOMMEND-03
- `.planning/ROADMAP.md` — Phase 11 目标：内容推荐增强

### Prior Phase Context
- `.planning/phases/10-content-data/10-CONTEXT.md` — Phase 10 内容数据扩充完整上下文
- `.planning/phases/09-scene-device/09-CONTEXT.md` — Phase 9 场景设备联动完整上下文

### Existing Code
- `backend/src/main/java/com/example/iot/entity/UserPreference.java` — 现有字段：userId, contentType, preferenceScore
- `backend/src/main/java/com/example/iot/entity/Recommendation.java` — 推荐记录实体
- `backend/src/main/java/com/example/iot/entity/Content.java` — 内容实体（type 字段：MOVIE/MUSIC/GAME）
- `backend/src/main/java/com/example/iot/service/impl/RecommendationServiceImpl.java` — 硬编码权重常量 CLICK_WEIGHT=5, LIKE_WEIGHT=10, DISLIKE_WEIGHT=-20
- `backend/src/main/java/com/example/iot/service/impl/ContentServiceImpl.java` — `type` 参数过滤模式参考
- `backend/src/main/java/com/example/iot/controller/RecommendationController.java` — getRecommendations() 当前签名
- `backend/src/main/java/com/example/iot/controller/PreferenceController.java` — 用户偏好 CRUD 接口
- `backend/src/main/resources/db/migration/V1.4__add_scene_mutex_group.sql` — 最新 migration 参考

</canonical_refs>

<codebase_context>
## Existing Code Insights

### Reusable Assets
- `ContentServiceImpl.list()` — `type` 可选参数过滤模式，直接复用
- `UserPreferenceMapper` — 已有的 selectByUserId, selectByUserIdAndContentType 方法可复用
- `RecommendationVO` — 现有 VO 结构，可在分组中复用

### Established Patterns
- 可选查询参数：Spring `@RequestParam(required = false)` 模式
- LambdaQueryWrapper 用于动态条件构建
- @Transactional on service mutation methods
- snake_case DB columns ↔ camelCase Java fields

### Integration Points
- `RecommendationServiceImpl.getRecommendations()` — 添加 type 过滤逻辑
- `RecommendationServiceImpl.updateUserPreferenceScore()` — 使用可配置权重替代硬编码
- `RecommendationController.getRecommendations()` — 添加 type 参数
- 新增 `PreferenceController` 或扩展现有接口支持权重配置
- 可能需要新的 migration 添加 weight 字段

</codebase_context>

<specifics>
## Specific Ideas

- GroupedRecommendationVO 结构: `{ type: "MOVIE", items: [...], total: N }` 数组
- 权重字段默认值: click_weight=5, like_weight=10, dislike_weight=-20（保持现有行为）
- UserPreference 表 migration: `ALTER TABLE user_preference ADD COLUMN click_weight INT DEFAULT 5, ADD COLUMN like_weight INT DEFAULT 10, ADD COLUMN dislike_weight INT DEFAULT -20`

</specifics>

<deferred>
## Deferred Ideas

None — discussion stayed within phase scope.

</deferred>

---
*Phase: 11-content-recommendation*
*Context gathered: 2026-04-18*
