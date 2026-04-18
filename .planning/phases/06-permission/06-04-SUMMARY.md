# Phase 06 Plan 04: UserService Role-Filtered List and Permission-Checked Delete Summary

**Phase:** 06-permission
**Plan:** 06-04
**Status:** COMPLETED
**Completed:** 2026-04-18

## One-liner

Updated UserService interface and implementation to accept currentUserId and currentUserRole for role-filtered list() queries and permission-checked delete() operations.

## Task Summary

| Task | Name | Status |
|------|------|--------|
| 1 | Update UserService interface with new signatures | COMPLETED |
| 2 | Implement role-filtered list() and permission-checked delete() in UserServiceImpl | COMPLETED |

## Changes Made

### backend/src/main/java/com/example/iot/service/UserService.java

1. **Updated list() method signature:**
   ```java
   IPage<UserVO> list(Integer page, Integer size, String keyword, Long currentUserId, String currentUserRole);
   ```

2. **Updated delete() method signature:**
   ```java
   void delete(Long id, Long currentUserId, String currentUserRole);
   ```

### backend/src/main/java/com/example/iot/service/impl/UserServiceImpl.java

1. **Implemented role-filtered list():**
   - ADMIN role: Returns all users (with keyword filter if provided) via `lambdaQuery()`
   - USER role: Returns only the current user (filtered by currentUserId)

2. **Implemented permission-checked delete():**
   - ADMIN role: Cannot delete admin accounts; cannot delete themselves
   - USER role: Throws FORBIDDEN (endpoint is ADMIN-only via @PreAuthorize in controller)

## Key Decisions Implemented

- **D-02:** list() - ADMIN returns all users (with keyword filter), USER returns only currentUserId
- **D-03:** delete() - ADMIN cannot delete admin or self; ADMIN can delete normal users; USER cannot delete any user

## Verification

- Compilation: PASSED (`mvn compile -q -f backend/pom.xml`)
- All acceptance criteria met

## Dependencies

- **Depends on:** 06-03 (UserController already passes auth params — service now accepts them)

## Deviations

None - plan executed exactly as written.

## Notes

- The `lambdaQuery()` method is available via Mybatis-Plus IService extension
- Logical delete is used (userMapper.deleteById) rather than physical delete
- Operation log is recorded after successful deletion
