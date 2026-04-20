# Phase 8: Verification — 场景互斥机制

**Phase:** 08-scene-mutex
**Plan:** 08-01-PLAN.md
**Status:** Verified

## Goal-Backward Analysis

**Phase Goal (from ROADMAP.md):** 实现场景分组互斥，启用时自动禁用同组场景

**Success Criteria:**
1. 场景表增加 mutex_group 字段，支持NULL表示非互斥场景
2. 启用场景时，同mutex_group的其他场景自动禁用
3. 场景列表返回时标注当前启用的互斥组

## Verification Results

### Criterion 1: mutex_group 字段 ✓

| Check | Evidence |
|-------|----------|
| V1.4 migration exists | `V1.4__add_scene_mutex_group.sql` in `db/migration/` |
| Adds VARCHAR(50) column | `ALTER TABLE scene ADD COLUMN mutex_group VARCHAR(50) DEFAULT NULL` |
| NULL = non-exclusive | `DEFAULT NULL` in migration |

**Artifacts verified:**
- `grep -n "V1.4__add_scene_mutex_group" backend/src/main/resources/db/migration/` → found
- `grep -n "ADD COLUMN mutex_group" backend/src/main/resources/db/migration/V1.4__add_scene_mutex_group.sql` → found
- `grep -n "private String mutexGroup" backend/src/main/java/com/example/iot/entity/Scene.java` → line 37

### Criterion 2: 启用时自动禁用同组场景 ✓

| Check | Evidence |
|-------|----------|
| toggle() has mutex logic | `sameGroupEnabled` variable at SceneServiceImpl.java:174 |
| Queries same userId + mutexGroup | `wrapper.eq(Scene::getUserId, ...).eq(Scene::getMutexGroup, ...)` |
| Batch disables them | `s.setIsEnabled(0)` in loop |
| @Transactional on toggle() | `@Transactional` at SceneServiceImpl.java:155 |
| create() persists mutexGroup | `scene.setMutexGroup(dto.getMutexGroup())` at SceneServiceImpl.java |

**Artifacts verified:**
- `grep -n "sameGroupEnabled" backend/src/main/java/com/example/iot/service/impl/SceneServiceImpl.java` → line 174
- `grep -n "scene.getMutexGroup() != null" backend/src/main/java/com/example/iot/service/impl/SceneServiceImpl.java` → line 170
- `grep -n "@Transactional" backend/src/main/java/com/example/iot/service/impl/SceneServiceImpl.java` → multiple (including toggle at 155)

### Criterion 3: 场景列表标注当前启用的互斥组 ✓

| Check | Evidence |
|-------|----------|
| SceneVO has activeGroupSceneId | `private Long activeGroupSceneId` at SceneVO.java:44 |
| convertToVO() populates it | `vo.setActiveGroupSceneId` at SceneServiceImpl.java:285 |
| Guards with NULL check | `if (scene.getMutexGroup() != null ...)` at SceneServiceImpl.java:276 |

**Artifacts verified:**
- `grep -n "private Long activeGroupSceneId" backend/src/main/java/com/example/iot/vo/SceneVO.java` → line 44
- `grep -n "vo.setActiveGroupSceneId" backend/src/main/java/com/example/iot/service/impl/SceneServiceImpl.java` → line 285
- `grep -n "activeWrapper\|activeScenes" backend/src/main/java/com/example/iot/service/impl/SceneServiceImpl.java` → lines 278-286

## Phase Lock

All 3 success criteria satisfied. Phase 8 is **CLOSED**.

## Next Phase

Phase 9 (场景设备联动 — SCENE-03) is unblocked and ready to plan.
