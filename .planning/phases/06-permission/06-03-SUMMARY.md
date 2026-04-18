# Phase 06 Plan 03: UserController Security Integration Summary

**Phase:** 06-permission
**Plan:** 06-03
**Status:** COMPLETED
**Completed:** 2026-04-18

## One-liner

Injected SecurityContextUtil into UserController, implemented getCurrentUser() to return authenticated user's UserVO, and added @PreAuthorize("hasRole('ADMIN')") to list() and delete() endpoints.

## Commits

| Commit | Description |
|--------|-------------|
| 4f3abbf | feat(06-03): add SecurityContextUtil and @PreAuthorize to UserController |

## Task Summary

| Task | Name | Status | Commit |
|------|------|--------|--------|
| 1 | Update UserController with SecurityContextUtil and @PreAuthorize | COMPLETED | 4f3abbf |

## Changes Made

### backend/src/main/java/com/example/iot/controller/UserController.java

1. **Added imports:**
   - `import com.example.iot.security.SecurityContextUtil;`
   - `import org.springframework.security.access.prepost.PreAuthorize;`

2. **Injected SecurityContextUtil via constructor:**
   ```java
   private final UserService userService;
   private final SecurityContextUtil securityContextUtil;

   public UserController(UserService userService, SecurityContextUtil securityContextUtil) {
       this.userService = userService;
       this.securityContextUtil = securityContextUtil;
   }
   ```

3. **Implemented getCurrentUser():**
   ```java
   @GetMapping("/me")
   @Operation(summary = "获取当前用户信息")
   public Result<UserVO> getCurrentUser() {
       Long currentUserId = securityContextUtil.getCurrentUserId();
       UserVO user = userService.getById(currentUserId);
       return Result.success(user);
   }
   ```

4. **Added @PreAuthorize to list() and updated to pass auth context:**
   ```java
   @GetMapping
   @PreAuthorize("hasRole('ADMIN')")
   public Result<IPage<UserVO>> list(...) {
       IPage<UserVO> users = userService.list(page, size, keyword,
           securityContextUtil.getCurrentUserId(),
           securityContextUtil.getCurrentUserRole());
       return Result.success(users);
   }
   ```

5. **Added @PreAuthorize to delete() and updated to pass auth context:**
   ```java
   @DeleteMapping("/{id}")
   @PreAuthorize("hasRole('ADMIN')")
   public Result<Void> delete(@PathVariable Long id) {
       userService.delete(id,
           securityContextUtil.getCurrentUserId(),
           securityContextUtil.getCurrentUserRole());
       return Result.success("删除成功", null);
   }
   ```

## Verification

- Compilation: PASSED (`mvn compile -q -f backend/pom.xml`)
- All acceptance criteria met

## Dependencies

- **Depends on:** 06-01 (SecurityContextUtil must exist first)

## Deviations

None - plan executed exactly as written.

## Notes

- Plan 06-04 will update UserService interface to accept the new parameters (currentUserId, currentUserRole)
- Service layer methods currently have different signatures but controller passes extra parameters - this is expected per plan and will be resolved in 06-04
