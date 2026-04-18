# Phase 9: 场景设备联动 - Context

**Gathered:** 2026-04-18
**Status:** Ready for planning

<domain>
## Phase Boundary

实现 SCENE-03：触发场景时，根据场景配置同步更新关联设备的启用/禁用状态。触发场景则关联设备同步启用，禁用场景则关联设备同步禁用。

</domain>

<decisions>
## Implementation Decisions

### Device Sync Trigger Point
- **D-01:** Device sync happens in `trigger()`, not `toggle()`. The requirement says "触发场景时" — trigger() is the explicit execution action. toggle() handles mutex + scene state only. trigger() will be implemented to query linked SceneDevice records and update each Device.status to match scene state.

### Device Target State on Enable
- **D-02:** When scene is triggered (isEnabled=1), all linked devices are set to `status=1` (enabled). No per-device config parsing — keep it simple for SCENE-03 scope.

### Device State on Disable
- **D-03:** When scene is disabled (isEnabled=0 via toggle), all linked devices are set to `status=0` (disabled). Symmetric lifecycle: enable scene → enable devices, disable scene → disable devices.

### Implementation Approach
- **D-04:** trigger() already exists as a TODO stub at SceneServiceImpl:192. It will query all SceneDevice records for the scene, then batch update Device.status for each linked device.
- **D-05:** toggle() already exists. When `newEnabled=1`, it will add device sync (set all linked devices to status=1). When `newEnabled=0`, it will set all linked devices to status=0. All within the existing `@Transactional`.
- **D-06:** Both trigger() and toggle() call the same device sync helper method to avoid duplication.

### Error Handling
- **D-07:** If any device update fails, the @Transactional on toggle()/trigger() will roll back — scene state and device states stay consistent.

### Existing Patterns to Follow
- LambdaQueryWrapper for conditional queries
- @Transactional on service methods (already on toggle() and trigger())
- operationLogService.log() for audit trail
- snake_case DB columns ↔ camelCase Java fields

</decisions>

<canonical_refs>
## Canonical References

**Downstream agents MUST read these before planning or implementing.**

- `backend/src/main/java/com/example/iot/entity/Device.java` — Device.status field (0=disabled, 1=enabled)
- `backend/src/main/java/com/example/iot/entity/SceneDevice.java` — scene_device linkage table with config field
- `backend/src/main/java/com/example/iot/service/impl/SceneServiceImpl.java` — existing toggle() (line 156-189), trigger() TODO stub (line 192-207), getDeviceIds() (line 253-258)
- `backend/src/main/resources/db/migration/V1.4__add_scene_mutex_group.sql` — most recent migration pattern

</canonical_refs>

<code_context>
## Existing Code Insights

### Reusable Assets
- `SceneServiceImpl.getDeviceIds(Long sceneId)` — already queries SceneDevice for a scene's device IDs (line 253-258). Can be reused or extended for batch status update.
- `DeviceMapper` — available for `updateById()` calls
- `sceneDeviceMapper.selectList(wrapper)` — used in getDeviceVOs() to fetch all SceneDevice records for a scene

### Established Patterns
- toggle() pattern: fetch entity → modify → updateById → log operation
- @Transactional on toggle() already ensures atomicity
- Device.status values: 0=disabled, 1=enabled (matches isEnabled semantics)

### Integration Points
- trigger() at line 192-207 — replace TODO comment with device sync logic
- toggle() at line 156-189 — add device sync call after scene state update
- Both use scene.getUserId() for operation log

</code_context>

<specifics>
## Specific Ideas

- Batch device updates via for loop over SceneDevice records
- Reuse getDeviceIds() pattern but update Device.status instead of just returning IDs
- No new migration needed — Device.status and SceneDevice tables already exist

</specifics>

<deferred>
## Deferred Ideas

### Config per-device target state
- SceneDevice.config (JSON) could store per-device target status in future (e.g. `{"status": 0}` for some devices, `{"status": 1}` for others). Not in SCENE-03 scope — noted for Phase 12+ if content/recommendation phases expand scene config.

</deferred>

---
*Phase: 09-scene-device*
*Context gathered: 2026-04-18*
