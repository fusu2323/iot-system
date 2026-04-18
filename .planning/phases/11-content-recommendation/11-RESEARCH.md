# Phase 11: 内容推荐增强 - Research

**Researched:** 2026-04-18
**Domain:** Spring Boot recommendation system enhancement
**Confidence:** HIGH

## User Constraints (from CONTEXT.md)

### Locked Decisions

- **D-01:** `getRecommendations()` 增加可选 `type` 查询参数
  - `GET /api/recommendations?userId=1&type=MOVIE` - 只返回该类型的推荐
  - `GET /api/recommendations?userId=1` - 返回所有类型（向后兼容）
  - 与 `ContentController.list()` 的 `type` 参数模式保持一致

- **D-02:** 推荐结果按内容类型分组返回
  - 新增 `GroupedRecommendationVO` 或类似结构
  - 结构示例: `{ groups: { MOVIE: [...], MUSIC: [...], GAME: [...] }, total: N }`
  - 每个分组内按推荐分数排序
  - 移除顶层 `IPage` 分页，改为分组内部分页（可选）

- **D-03:** `UserPreference` 表增加影响力权重字段
  - `click_weight` - 点击行为对该类型推荐的影响力权重
  - `like_weight` - 收藏行为对该类型推荐的影响力权重
  - `dislike_weight` - 不喜欢行为对该类型推荐的影响力权重
  - 默认值保持现有硬编码值（CLICK=5, LIKE=10, DISLIKE=-20）
  - 用户可个性化配置不同类型的行为影响力
  - `RecommendationServiceImpl.calculateRecommendationScore()` 和 `updateUserPreferenceScore()` 需要修改以使用可配置的权重

- **D-04:** `PreferenceController` 或 `RecommendationController` 需要支持设置权重
  - 可能需要新增 `PUT /api/preferences/{contentType}/weights` 或扩展现有 `POST /api/preferences` 接口

### Deferred Ideas

None - discussion stayed within phase scope.

## Summary

Phase 11 implements four requirements (CONTENT-04, RECOMMEND-01, RECOMMEND-02, RECOMMEND-03) to enhance the content recommendation system with type filtering, grouped results, and configurable weights. The implementation requires: (1) adding an optional `type` parameter to `getRecommendations()`, (2) creating a new grouped response structure replacing `IPage<RecommendationVO>`, (3) adding weight columns to `user_preference` table, and (4) updating services to use configurable weights instead of hardcoded constants.

**Primary recommendation:** Follow the established Spring patterns in the codebase (optional `@RequestParam`, `LambdaQueryWrapper`, snake_case DB columns) and create a migration V1.6 for the weight columns.

## Phase Requirements

| ID | Description | Research Support |
|----|-------------|------------------|
| CONTENT-04 | 内容列表增加类型筛选功能 | Type filter pattern already exists in ContentServiceImpl.list() |
| RECOMMEND-01 | 推荐列表增加内容类型筛选 | Optional type param to getRecommendations() with same pattern |
| RECOMMEND-02 | 推荐结果按内容类型分组展示 | New GroupedRecommendationVO structure replacing IPage |
| RECOMMEND-03 | 用户偏好设置增加内容类型权重配置 | DB migration + entity + service updates |

## Architectural Responsibility Map

| Capability | Primary Tier | Secondary Tier | Rationale |
|------------|-------------|----------------|-----------|
| Type filtering in recommendations | API/Backend | - | RecommendationServiceImpl.getRecommendations() is the sole consumer |
| Grouped response structure | API/Backend | - | New VO created in backend, returned via REST API |
| Configurable weight storage | Database/Storage | API/Backend | user_preference table stores weights; service reads them |
| Weight configuration API | API/Backend | - | PreferenceController extended to handle weight updates |

## Technical Approach

### CONTENT-04 / RECOMMEND-01: Type Filtering in getRecommendations()

**Current state:** `RecommendationController.getRecommendations(userId, page, size)` returns `IPage<RecommendationVO>`

**Changes required:**

1. **Controller layer** (`RecommendationController.java`):
   - Add `@RequestParam(required = false) String type` parameter
   - Pass type to service method
   - Response type changes to `GroupedRecommendationResponse` (new)

```java
// RecommendationController.java
@GetMapping
@Operation(summary = "获取推荐列表")
public Result<GroupedRecommendationResponse> getRecommendations(
    @Parameter(description = "用户 ID", example = "1")
    @RequestParam Long userId,
    @Parameter(description = "内容类型筛选", example = "MOVIE")
    @RequestParam(required = false) String type,  // NEW
    @Parameter(description = "页码", example = "1")
    @RequestParam(defaultValue = "1") Integer page,
    @Parameter(description = "每页大小", example = "10")
    @RequestParam(defaultValue = "10") Integer size
) {
    GroupedRecommendationResponse response = recommendationService.getRecommendations(userId, type, page, size);
    return Result.success(response);
}
```

2. **Service interface** (`RecommendationService.java`):
   - Update signature to include `String type` parameter
   - Change return type from `IPage<RecommendationVO>` to `GroupedRecommendationResponse`

3. **Service implementation** (`RecommendationServiceImpl.java`):
   - Add type filter in step 3 (获取所有内容): filter by `content.getType().equals(type)` when type is not null
   - Uses same pattern as `ContentServiceImpl.list()`: `StringUtils.hasText(type)` for conditional filter

**Pattern reference:** `ContentServiceImpl.list()` lines 42-47:
```java
if (StringUtils.hasText(type)) {
    wrapper.eq(Content::getType, type);
}
```

### RECOMMEND-02: Grouped Response Structure

**New files required:**

1. **GroupedRecommendationVO.java** - represents a single group's data:
```java
@Schema(description = "分组推荐内容视图对象")
public class GroupedRecommendationVO {
    @Schema(description = "内容类型", example = "MOVIE")
    private String type;

    @Schema(description = "该类型的推荐列表")
    private List<RecommendationVO> items;

    @Schema(description = "该类型的总数")
    private Integer total;

    // getters/setters
}
```

2. **GroupedRecommendationResponse.java** - wrapper for all groups:
```java
@Schema(description = "分组推荐响应")
public class GroupedRecommendationResponse {
    @Schema(description = "按类型分组的推荐列表")
    private Map<String, GroupedRecommendationVO> groups;

    @Schema(description = "推荐内容总数")
    private Integer total;

    // getters/setters
}
```

**Implementation in `RecommendationServiceImpl.getRecommendations()`:**
- After calculating all `ContentScore` objects (step 4 in existing code)
- Group by `content.getType()` using `Collectors.groupingBy()`
- Sort each group by score descending
- Apply pagination per group (optional per D-02)
- Build `GroupedRecommendationResponse` with all groups

**Key design decision:** Response uses `Map<String, GroupedRecommendationVO>` where keys are content types (MOVIE, MUSIC, GAME). This matches D-02 structure: `{ groups: { MOVIE: [...], MUSIC: [...], GAME: [...] }, total: N }`.

### RECOMMEND-03: Configurable Preference Weights

**DB Migration (V1.6__add_preference_weights.sql):**

```sql
-- Phase 11: Add configurable weight columns to user_preference
-- Supports RECOMMEND-03

ALTER TABLE user_preference
    ADD COLUMN click_weight INT DEFAULT 5 COMMENT '点击行为权重',
    ADD COLUMN like_weight INT DEFAULT 10 COMMENT '收藏行为权重',
    ADD COLUMN dislike_weight INT DEFAULT -20 COMMENT '不喜欢行为权重';
```

**Entity changes** (`UserPreference.java`):

```java
@Schema(description = "点击行为权重")
private Integer clickWeight;

@Schema(description = "收藏行为权重")
private Integer likeWeight;

@Schema(description = "不喜欢行为权重")
private Integer dislikeWeight;
```

**VO changes** (`UserPreferenceVO.java`):

```java
@Schema(description = "点击行为权重", example = "5")
private Integer clickWeight;

@Schema(description = "收藏行为权重", example = "10")
private Integer likeWeight;

@Schema(description = "不喜欢行为权重", example = "-20")
private Integer dislikeWeight;
```

**DTO changes** (`UserPreferenceDTO.java`):

```java
@Schema(description = "点击行为权重", example = "5")
private Integer clickWeight;

@Schema(description = "收藏行为权重", example = "10")
private Integer likeWeight;

@Schema(description = "不喜欢行为权重", example = "-20")
private Integer dislikeWeight;
```

**Service changes** (`RecommendationServiceImpl.java`):

1. Remove hardcoded constants:
```java
// REMOVE these:
// private static final int CLICK_WEIGHT = 5;
// private static final int LIKE_WEIGHT = 10;
// private static final int DISLIKE_WEIGHT = -20;
```

2. Modify `updateUserPreferenceScore()` to fetch weights from UserPreference:
```java
@Override
@Transactional
public void updateUserPreferenceScore(Long userId, String contentType, int deltaScore) {
    UserPreference existing = userPreferenceMapper.selectByUserIdAndContentType(userId, contentType);

    if (existing != null) {
        int newScore = Math.max(0, Math.min(100, existing.getPreferenceScore() + deltaScore));
        existing.setPreferenceScore(newScore);
        userPreferenceMapper.updateById(existing);
    } else {
        UserPreference preference = new UserPreference();
        preference.setUserId(userId);
        preference.setContentType(contentType);
        preference.setPreferenceScore(Math.max(0, deltaScore));
        // Set default weights for new preferences
        preference.setClickWeight(5);
        preference.setLikeWeight(10);
        preference.setDislikeWeight(-20);
        userPreferenceMapper.insert(preference);
    }
}
```

3. Modify `recordClick()`, `recordLike()`, `recordDislike()` to use configurable weights:
```java
@Override
@Transactional
public void recordClick(Long userId, Long contentId) {
    updateRecommendation(userId, contentId, "isClicked", 1);

    Content content = contentMapper.selectById(contentId);
    if (content != null) {
        // Fetch user's weight configuration for this content type
        UserPreference pref = userPreferenceMapper.selectByUserIdAndContentType(userId, content.getType());
        int weight = (pref != null && pref.getClickWeight() != null) ? pref.getClickWeight() : 5;
        updateUserPreferenceScore(userId, content.getType(), weight);
    }

    saveFeedback(userId, contentId, null, "CLICK", null);
}
```

4. Modify `getFeedbackWeight()` to use configurable weights:
```java
private int getFeedbackWeight(Long userId, String contentType, String feedbackType) {
    UserPreference pref = userPreferenceMapper.selectByUserIdAndContentType(userId, contentType);

    switch (feedbackType) {
        case "LIKE":
        case "COLLECT":
            return (pref != null && pref.getLikeWeight() != null) ? pref.getLikeWeight() : 10;
        case "DISLIKE":
            return (pref != null && pref.getDislikeWeight() != null) ? pref.getDislikeWeight() : -20;
        case "CLICK":
            return (pref != null && pref.getClickWeight() != null) ? pref.getClickWeight() : 5;
        default:
            return 0;
    }
}
```

### Weight Configuration API Endpoint

**Option A: Extend existing POST /api/preferences** (D-04 suggests this)

Update `PreferenceController.setPreference()` to also accept weight fields in `UserPreferenceDTO`. When user sends:
```json
{
    "contentType": "MOVIE",
    "preferenceScore": 80,
    "clickWeight": 8,
    "likeWeight": 15,
    "dislikeWeight": -25
}
```

Update `UserPreferenceService.setPreference()` signature to accept weight parameters.

**Option B: New PUT /api/preferences/{contentType}/weights**

If weights should be updated separately from preference score, add new endpoint:
```java
@PutMapping("/{contentType}/weights")
@Operation(summary = "更新用户偏好权重配置")
public Result<Void> updatePreferenceWeights(
    @RequestParam Long userId,
    @PathVariable String contentType,
    @RequestBody PreferenceWeightDTO dto  // new DTO with clickWeight, likeWeight, dislikeWeight
)
```

**Recommendation:** Option A is simpler and keeps weight configuration with existing preference logic. Per D-04, either approach is valid.

## Integration Points

| File | Change Type | Description |
|------|-------------|-------------|
| `RecommendationController.java` | Modify | Add `type` param to `getRecommendations()`, change return type |
| `RecommendationService.java` | Modify | Update interface signature |
| `RecommendationServiceImpl.java` | Modify | Add type filtering, grouped response, configurable weights |
| `GroupedRecommendationVO.java` | New | Single group structure |
| `GroupedRecommendationResponse.java` | New | Wrapper for all groups |
| `UserPreference.java` | Modify | Add clickWeight, likeWeight, dislikeWeight fields |
| `UserPreferenceVO.java` | Modify | Add weight fields |
| `UserPreferenceDTO.java` | Modify | Add weight fields |
| `UserPreferenceService.java` | Modify | May need new method for weight updates |
| `UserPreferenceServiceImpl.java` | Modify | Implement weight update logic |
| `PreferenceController.java` | Modify | Support weight configuration |
| `V1.6__add_preference_weights.sql` | New | Migration for weight columns |

## Don't Hand-Roll

| Problem | Don't Build | Use Instead | Why |
|---------|-------------|-------------|-----|
| Type filtering logic | Custom conditional SQL | Same LambdaQueryWrapper pattern as ContentServiceImpl.list() | Established pattern already in codebase |
| Weight defaults | Application-level constants | DB column DEFAULT values + null checks in service | Ensures defaults persist even if code changes |
| Grouping logic | Multiple separate queries | Collectors.groupingBy() on in-memory list | Already used in existing codebase for similar transformations |

## Common Pitfalls

### Pitfall 1: Backward Compatibility Breaking Change
**What goes wrong:** Changing `getRecommendations()` return type from `IPage<RecommendationVO>` to `GroupedRecommendationResponse` breaks existing API clients.
**Why it happens:** D-02 explicitly specifies removing top-level pagination, which requires structural change.
**How to avoid:** Document the breaking change clearly. If backward compatibility is critical, consider adding a new endpoint `/api/recommendations/grouped` while keeping old endpoint.
**Warning signs:** Frontend/mobile clients directly parsing `IPage` fields (records, total, pages).

### Pitfall 2: Null Weight Fields for Existing Preferences
**What goes wrong:** Existing `user_preference` records have NULL weight values, causing NullPointerException when `getFeedbackWeight()` calls `pref.getClickWeight()`.
**Why it happens:** Migration adds columns with DEFAULT, but existing rows get NULL until updated.
**How to avoid:** Always use null-safe access: `(pref != null && pref.getClickWeight() != null) ? pref.getClickWeight() : 5`
**Warning signs:** Existing users with preferences trigger NPE when they click/like/dislike content.

### Pitfall 3: Grouped Response Missing Empty Types
**What goes wrong:** If user has no preference for a content type, that type's group is missing from response instead of showing empty list.
**Why it happens:** Grouping only includes types that have matching content after filtering.
**How to avoid:** After grouping, ensure all three types (MOVIE, MUSIC, GAME) are present in the Map, even if empty.

## Code Examples

### Type Filtering Pattern (from ContentServiceImpl.list())
```java
// ContentServiceImpl.java lines 42-47
if (StringUtils.hasText(type)) {
    wrapper.eq(Content::getType, type);
}
```

### Grouping Pattern (to adapt for recommendations)
```java
// After calculating List<ContentScore> contentScores:
Map<String, List<ContentScore>> grouped = contentScores.stream()
    .collect(Collectors.groupingBy(cs -> cs.content.getType()));

Map<String, GroupedRecommendationVO> groups = new LinkedHashMap<>();
for (String contentType : Arrays.asList("MOVIE", "MUSIC", "GAME")) {
    List<ContentScore> typeScores = grouped.getOrDefault(contentType, Collections.emptyList());
    typeScores.sort((a, b) -> b.score - a.score); // Sort within group

    GroupedRecommendationVO vo = new GroupedRecommendationVO();
    vo.setType(contentType);
    vo.setItems(typeScores.subList(0, Math.min(typeScores.size(), size)).stream()
        .map(cs -> convertToVO(cs.content, cs.score, userId))
        .collect(Collectors.toList()));
    vo.setTotal(typeScores.size());
    groups.put(contentType, vo);
}
```

### Null-Safe Weight Access Pattern
```java
private int getWeightOrDefault(UserPreference pref, String weightField) {
    if (pref == null) return getDefaultWeight(weightField);
    Integer value = null;
    if ("clickWeight".equals(weightField)) value = pref.getClickWeight();
    else if ("likeWeight".equals(weightField)) value = pref.getLikeWeight();
    else if ("dislikeWeight".equals(weightField)) value = pref.getDislikeWeight();
    return value != null ? value : getDefaultWeight(weightField);
}

private int getDefaultWeight(String weightField) {
    return switch (weightField) {
        case "clickWeight" -> 5;
        case "likeWeight" -> 10;
        case "dislikeWeight" -> -20;
        default -> 0;
    };
}
```

## State of the Art

| Old Approach | Current Approach | When Changed | Impact |
|--------------|------------------|--------------|--------|
| Hardcoded CLICK_WEIGHT=5, LIKE_WEIGHT=10, DISLIKE_WEIGHT=-20 | Configurable per-content-type weights in user_preference table | Phase 11 | Users can customize how their interactions affect recommendations |
| Flat IPage<RecommendationVO> response | GroupedRecommendationResponse with Map<String, GroupedRecommendationVO> | Phase 11 | Frontend can display recommendations organized by content type |
| No type filter on recommendations | Optional type parameter on getRecommendations() | Phase 11 | Users can filter to specific content types |

## Assumptions Log

| # | Claim | Section | Risk if Wrong |
|---|-------|---------|---------------|
| A1 | Content types are limited to MOVIE, MUSIC, GAME enum values | RECOMMEND-02 grouping | If new types added, grouped response may not include them |
| A2 | Existing user_preference records will have NULL weights after migration | RECOMMEND-03 null handling | NPE risk mitigated by null checks, but weights won't take effect until explicitly set |
| A3 | Weight configuration via extending POST /api/preferences is acceptable | Weight Configuration API | If user expects separate endpoints, API design may need revision |

## Open Questions

1. **Pagination strategy for grouped response**
   - What we know: D-02 says "移除顶层IPage分页，改为分组内部分页（可选）"
   - What's unclear: Is per-group pagination required or optional? If required, what page/size applies to each group?
   - Recommendation: Make pagination per-group with consistent size parameter applied to each type

2. **Should existing recommendations be retroactively recalculated when user changes weights?**
   - What we know: Currently recommendations are calculated on-the-fly in getRecommendations()
   - What's unclear: If user changes weights, should existing recommendation scores change immediately?
   - Recommendation: Since calculation is on-the-fly, weight changes take effect on next getRecommendations() call

3. **Default weights per content type or global defaults?**
   - What we know: D-03 says "默认值保持现有硬编码值（CLICK=5, LIKE=10, DISLIKE=-20）"
   - What's unclear: Are defaults global (same for all types) or per-type (each type can have different defaults)?
   - Recommendation: Global defaults initially (5/10/-20), users can customize per content type

## Environment Availability

Step 2.6: SKIPPED (no external dependencies identified - pure Java/Spring Boot changes)

## Security Domain

### Applicable ASVS Categories

| ASVS Category | Applies | Standard Control |
|---------------|---------|-----------------|
| V4 Access Control | Yes | User can only set their own preferences - userId from auth context, not request body |
| V5 Input Validation | Yes | @Valid on DTOs, type enum validation (MOVIE/MUSIC/GAME) |

### Known Threat Patterns for Spring Boot Recommendation

| Pattern | STRIDE | Standard Mitigation |
|---------|--------|---------------------|
| Manipulating weight values to bias recommendations | Tampering | Validate weight ranges (e.g., -100 to 100) |
| Accessing other users' preference data | Information Disclosure | userId from authenticated principal, not request parameter |

**Note:** Current implementation uses request parameter for userId - in production, this should come from JWT token authentication context (Spring Security principal).

## Sources

### Primary (HIGH confidence)
- Existing codebase: `ContentServiceImpl.list()` - verified type filtering pattern
- Existing codebase: `RecommendationServiceImpl.java` - verified hardcoded weight constants
- Existing codebase: `UserPreferenceMapper.java` - verified mapper methods available
- Migration pattern: `V1.4__add_scene_mutex_group.sql` - verified ALTER TABLE pattern

### Secondary (MEDIUM confidence)
- CONTEXT.md decisions D-01 through D-04 - user-locked implementation decisions

## Metadata

**Confidence breakdown:**
- Standard stack: HIGH - pure Spring Boot, MyBatis-Plus patterns already in codebase
- Architecture: HIGH - follows established project patterns exactly
- Pitfalls: MEDIUM - potential backward compatibility issue identified, but explicitly requested in D-02

**Research date:** 2026-04-18
**Valid until:** 2026-05-18 (30 days - Spring Boot recommendation patterns are stable)
