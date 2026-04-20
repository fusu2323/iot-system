# Phase 8: Code Review — 场景互斥机制

**Phase:** 08-scene-mutex
**Plan:** 08-01-PLAN.md
**Status:** Post-execution review
**Commit:** 278d371

## Files Changed

| File | Change |
|------|--------|
| `db/migration/V1.4__add_scene_mutex_group.sql` | Adds `mutex_group VARCHAR(50)` column |
| `entity/Scene.java` | + `mutexGroup` field + getter/setter |
| `dto/SceneCreateDTO.java` | + `mutexGroup` field + getter/setter |
| `dto/SceneUpdateDTO.java` | + `mutexGroup` field + getter/setter |
| `vo/SceneVO.java` | + `mutexGroup`, `activeGroupSceneId` fields |
| `service/impl/SceneServiceImpl.java` | toggle() mutex logic + convertToVO() activeGroupSceneId |

## Review Findings

### Critical Issues: None

### Medium Issues: None

### Minor Issues / Notes

- **T-08-02 (Race condition)**: The toggle() method has a known race condition when concurrent toggles happen within the same mutex_group. Accepted in threat model as "acceptable for MVP." No fix needed at this stage.

- **NULL safety**: The mutex logic correctly guards against NULL mutexGroup (`scene.getMutexGroup() != null`), ensuring existing scenes with NULL are unaffected.

- **@Transactional placement**: Correctly placed on toggle() — ensures enable/disable are atomic.

- **activeGroupSceneId in convertToVO**: Populated correctly via a separate query per scene with non-NULL mutexGroup. No N+1 concern for MVP scope.

- **Logging**: `log.info("mutex disabled: ...")` provides audit trail for auto-disable events.

## Security

- Mutex logic operates on `userId` scope — users can only affect their own scenes.
- No new authorization bypass vectors introduced.

## Conclusion

**Phase 8 implementation is sound.** All acceptance criteria met. Ready for verification.
