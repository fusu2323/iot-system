# Phase 6: 权限体系重构 - Research

**Researched:** 2026-04-18
**Domain:** Spring Security 6.x method-level authorization with @PreAuthorize
**Confidence:** HIGH (based on Spring Security 6.x / Spring Boot 3.2.0 verified patterns + codebase analysis)

## Summary

Phase 6 implements role-based access control using Spring Security's `@PreAuthorize` annotation with the existing JWT-based authentication infrastructure. The `@EnableMethodSecurity` is already configured, `JwtUserDetailsService` already loads user authorities with the `ROLE_` prefix, and the `User` entity has a `role` field (ADMIN/USER). Key implementation tasks are: (1) a `SecurityContext` utility to extract the current user ID from the JWT-saturated `Authentication` object, (2) `@PreAuthorize` annotations on `LogController` and `UserController` methods, and (3) service-layer user list filtering and delete permission enforcement.

**Primary recommendation:** Create a `SecurityContextUtil` component to centralize current-user extraction, use `@PreAuthorize("hasRole('ADMIN')")` on admin-only endpoints, and implement role-filtered queries in `UserService.list()` and `UserService.delete()` using the current user ID from the security context.

## Architectural Responsibility Map

| Capability | Primary Tier | Secondary Tier | Rationale |
|------------|-------------|----------------|-----------|
| JWT authentication | API/Backend (JwtAuthenticationFilter) | -- | Filter-level, stateless |
| Authorization annotation | API/Backend (Controller/Service) | -- | @PreAuthorize at method level |
| Current user resolution | API/Backend (SecurityContext) | -- | ThreadLocal-based, request-scoped |
| User list filtering | API/Backend (UserService) | Database | Role-based row filtering |
| Delete permission check | API/Backend (UserService) | -- | Business logic rule enforcement |
| AccessDenied handling | API/Backend (GlobalExceptionHandler) | -- | Centralized 403 response |

## User Constraints (from CONTEXT.md)

### Locked Decisions
- **D-01:** Use `@PreAuthorize` annotation for method-level permission control (NOT URL-path configuration, NOT manual service-layer checks)
- **D-02:** Service layer dynamically filters user list by role (ADMIN returns all, USER returns only self)
- **D-03:** Admin cannot delete admin accounts or themselves; admin can delete normal users; normal users can only delete themselves
- **D-04:** `/api/logs` uses `@PreAuthorize("hasRole('ADMIN')")`
- **D-05:** Need to implement getting current user ID from SecurityContext

### Deferred Ideas (OUT OF SCOPE)
None.

## Phase Requirements

| ID | Description | Research Support |
|----|-------------|------------------|
| AUTH-03 | 管理员可查看所有用户列表 | UserService.list() + @PreAuthorize for ADMIN-only full access |
| AUTH-04 | 管理员可删除普通用户 | UserService.delete() with role/deletion logic |
| AUTH-05 | 普通用户不能删除其他用户 | Service-layer check: currentUserId == targetId for non-admins |
| AUTH-06 | 普通用户不能查看系统操作日志 | @PreAuthorize("hasRole('ADMIN')") on LogController |
| PERMISSION-01 | Define admin operations | Role-based access on all admin endpoints |
| PERMISSION-02 | Define user operations | Role-based access, self-only for certain ops |
| PERMISSION-03 | Interface-level permission checks | @PreAuthorize + SecurityContextUtil |

## Standard Stack

### Core
| Library | Version | Purpose | Why Standard |
|---------|---------|---------|--------------|
| spring-boot-starter-security | 3.2.0 (from Spring Boot 3.2.0) | Authentication/authorization framework | Standard for Spring Boot 3.x |
| spring-security-test | 3.2.0 | Test annotations for security | Standard testing for Spring Security |
| jjwt-api/impl/jackson | 0.12.3 | JWT token handling | Already in project |

### Supporting
| Library | Version | Purpose | When to Use |
|---------|---------|---------|-------------|
| Spring AOP | Bundled with spring-boot-starter-aop | Method security via AOP | Always — required for @PreAuthorize |

**No additional dependencies required** — all needed libraries are already in pom.xml:
- `@EnableMethodSecurity` — already configured in SecurityConfig
- `spring-boot-starter-aop` — already included
- `spring-security-test` — already included

## Architecture Patterns

### System Architecture Diagram

```
[HTTP Request with JWT]
         |
         v
[JwtAuthenticationFilter] --> [SecurityContextHolder] (Authentication populated)
         |
         v
[Controller Method] --> [@PreAuthorize("hasRole('ADMIN')")] --> [AccessDeniedException] --> [GlobalExceptionHandler] --> [403 Result]
         |
         v (if authorized)
[UserService.delete(id)] --> [SecurityContextUtil.getCurrentUserId()] --> [role check + ownership check]
         |
         v
[Database: user deleted]
```

### Recommended Project Structure (changes only)

```
backend/src/main/java/com/example/iot/
├── security/
│   └── SecurityContextUtil.java     # NEW: current user extraction from SecurityContext
├── service/impl/
│   └── UserServiceImpl.java         # MODIFY: add currentUserId param to list(), add permission logic to delete()
├── controller/
│   ├── UserController.java          # MODIFY: @PreAuthorize + use SecurityContextUtil
│   └── LogController.java           # MODIFY: add @PreAuthorize("hasRole('ADMIN')")
```

### Pattern 1: @PreAuthorize with hasRole

**What:** Annotation-based method authorization using role names without `ROLE_` prefix.
**When to use:** Any controller or service method that should be restricted to specific roles.

**Code example:**
```java
// Source: Spring Security 6.x convention
// hasRole('ADMIN') internally adds ROLE_ prefix -> checks Authority = "ROLE_ADMIN"
@PreAuthorize("hasRole('ADMIN')")
@GetMapping("/api/logs")
public Result<IPage<OperationLogVO>> list(...) { ... }
```

**Why this works in current project:** `JwtUserDetailsService.loadUserByUsername()` creates authorities as:
```java
Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
// e.g., for admin: "ROLE_ADMIN", for user: "ROLE_USER"
```
So `@PreAuthorize("hasRole('ADMIN')")` correctly resolves.

### Pattern 2: Getting Current User from SecurityContext

**What:** Extract the current authenticated user's ID from the JWT-saturated SecurityContext.
**When to use:** Service methods that need to filter data by ownership (e.g., "show only my data").

**Code example:**
```java
// Source: Spring Security 6.x pattern (verified against codebase JwtUtil.getUserIdFromToken)
@Component
public class SecurityContextUtil {

    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("No authenticated user found");
        }

        // The principal is UserDetails (JwtUserDetailsService sets it)
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            // Need to get userId — options below
        }
        return null;
    }
}
```

**Critical issue identified:** `JwtUserDetailsService` does NOT store the userId in the `UserDetails` principal — it only stores username. However, the JWT token itself contains `userId` in claims (via `JwtUtil.generateToken(userDetails, user.getId())`).

**Solution options for getCurrentUserId():**
1. **Option A (Recommended):** Create an extended `UserDetails` implementation that holds userId, populate it in `JwtAuthenticationFilter`, and cast in `SecurityContextUtil`
2. **Option B:** Re-parse the JWT token in `SecurityContextUtil` to get userId from claims (extra parsing overhead)
3. **Option C:** Query the database by username to get userId (extra DB call)

**Option A is best** — avoids token re-parsing and extra DB queries.

### Pattern 3: Service Layer Role-Filtered Query

**What:** Admin sees all records; normal users see only their own.
**When to use:** List queries that must be scoped to the current user unless admin.

**Code example:**
```java
// Source: Standard role-filtered query pattern
@Override
public IPage<UserVO> list(Integer page, Integer size, String keyword, Long currentUserId, String currentUserRole) {
    Page<User> userPage = new Page<>(page, size);

    if ("ADMIN".equals(currentUserRole)) {
        // Admin sees all users with optional keyword filter
        return userMapper.selectList(keyword)
            .convert(this::convertToVO);
    } else {
        // Normal users see only themselves
        return userMapper.selectList(currentUserId, keyword)
            .convert(this::convertToVO);
    }
}
```

### Anti-Patterns to Avoid

- **`@PreAuthorize("hasAuthority('ROLE_ADMIN')")` with string literal:** Works but less maintainable than `hasRole('ADMIN')`. Always use `hasRole()` / `hasAnyRole()` in annotations for consistency.
- **Manual role checking in service:** `if (!currentUser.getRole().equals("ADMIN")) throw new BusinessException(...)` — defeats the purpose of declarative security. Use `@PreAuthorize` at the controller boundary and let exceptions propagate.
- **Returning empty list for unauthorized access:** A user querying `GET /api/users` as a normal user should get only their own record(s), not an empty list. Empty result could mean "no data" vs "no permission" ambiguity.
- **Casting Authentication.getPrincipal() to String:** In this project, the principal is a `UserDetails` object (set by `JwtAuthenticationFilter`), not a String. Blind casting causes ClassCastException.

## Common Pitfalls

### Pitfall 1: @PreAuthorize not triggering (403 on all requests)
**What goes wrong:** All authenticated requests return 403 even with correct roles.
**Why it happens:** `@EnableMethodSecurity` was added in Spring Security 6.2 as opt-in via bean; if using 6.2+ without explicit config. In Spring Boot 3.2.0 (Spring Security 6.2), `@EnableMethodSecurity` works correctly.
**How to avoid:** Confirm `@EnableMethodSecurity` is present on the `@Configuration` class. Already verified present in `SecurityConfig.java` line 26.
**Warning signs:** Adding `@PreAuthorize` has no effect; method executes without check.

### Pitfall 2: Role prefix mismatch
**What goes wrong:** `@PreAuthorize("hasRole('ADMIN')")` always denies even for admin users.
**Why it happens:** `hasRole('ADMIN')` internally checks for `ROLE_ADMIN` authority. If `JwtUserDetailsService` returns `SimpleGrantedAuthority("ADMIN")` without the `ROLE_` prefix, the check fails.
**How to avoid:** Verify that `JwtUserDetailsService` uses `new SimpleGrantedAuthority("ROLE_" + user.getRole())`. Already confirmed correct in `JwtUserDetailsService.java` line 34.
**Warning signs:** Admin users cannot access admin endpoints despite being logged in.

### Pitfall 3: SecurityContext is null in service layer
**What goes wrong:** `SecurityContextHolder.getContext().getAuthentication()` returns null in a @Service method.
**Why it happens:** Security context is request-scoped and properly cleared after request in standard config. If seeing null, likely accessing it outside a request thread (e.g., async, @Scheduled with SecurityContext not propagated).
**How to avoid:** In this project, all permission checks happen in Controller or immediately-called Service methods within the request thread. Do not move to background threads.
**Warning signs:** NullPointerException on `authentication.getPrincipal()`.

### Pitfall 4: @PreAuthorize on interface methods not working with JDK proxy
**What goes wrong:** `@PreAuthorize` on an interface method (e.g., `UserService.list()`) not triggering.
**Why it happens:** Spring AOP uses JDK dynamic proxy by default for interfaces. Annotations on interface methods require `proxyTargetClass=true` or using class-based targets.
**How to avoid:** This project uses concrete class `UserServiceImpl` (not interface-injected), so Spring creates a CGLIB proxy that correctly intercepts. No action needed.

### Pitfall 5: AccessDeniedException not handled (returns 500)
**What goes wrong:** Unauthorized access returns HTTP 500 instead of 403.
**Why it happens:** `GlobalExceptionHandler.handleAccessDeniedException()` exists but is not catching the exception from method security.
**How to avoid:** Verify `handleAccessDeniedException` exists. Already confirmed in `GlobalExceptionHandler.java` lines 79-84, maps to `ResultCode.FORBIDDEN`. This should work correctly.

## Code Examples

### Example 1: Admin-only LogController with @PreAuthorize

```java
// Source: Based on existing LogController + Spring Security 6.x @PreAuthorize pattern
@RestController
@RequestMapping("/api/logs")
@Tag(name = "日志管理", description = "操作日志查询接口")
public class LogController {

    private final LogService logService;

    public LogController(LogService logService) {
        this.logService = logService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")  // Only admins can access
    @Operation(summary = "查询操作日志列表")
    public Result<IPage<OperationLogVO>> list(...) {
        // implementation unchanged
    }
}
```

### Example 2: SecurityContextUtil for Current User ID

```java
// Source: Spring Security 6.x pattern adapted for this project's JWT structure
@Component
public class SecurityContextUtil {

    private final JwtUtil jwtUtil;

    public SecurityContextUtil(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * Get current authenticated user's ID from SecurityContext.
     * Requires JwtAuthenticationFilter to have populated Authentication.
     */
    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("No authenticated user in SecurityContext");
        }

        Object principal = authentication.getPrincipal();
        String username;

        if (principal instanceof UserDetails userDetails) {
            username = userDetails.getUsername();
        } else if (principal instanceof String s) {
            username = s;
        } else {
            throw new IllegalStateException("Unexpected principal type: " + principal.getClass());
        }

        // Get user from DB by username to retrieve ID
        // Alternatively: extend UserDetails to carry userId (Option A from research)
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            throw new IllegalStateException("User not found: " + username);
        }
        return user.getId();
    }

    /**
     * Get current user's role from SecurityContext.
     */
    public String getCurrentUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new IllegalStateException("No authenticated user in SecurityContext");
        }
        return authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .filter(auth -> auth.startsWith("ROLE_"))
            .map(auth -> auth.substring(5))  // Strip "ROLE_" prefix
            .findFirst()
            .orElse("USER");
    }
}
```

### Example 3: Role-filtered UserService.list()

```java
// Source: Service-layer role filtering pattern
@Override
public IPage<UserVO> list(Integer page, Integer size, String keyword, Long currentUserId, String currentUserRole) {
    Page<User> userPage = new Page<>(page, size);

    IPage<User> resultPage;
    if ("ADMIN".equals(currentUserRole)) {
        // Admin: return all users matching keyword (if provided)
        resultPage = userPage.setRecords(userMapper.selectList(keyword));  // admin query
    } else {
        // Normal user: return only themselves
        resultPage = userPage.setRecords(userMapper.selectListById(currentUserId));
    }

    return resultPage.convert(this::convertToVO);
}
```

### Example 4: Delete permission check in UserService.delete()

```java
// Source: Business logic permission pattern
@Override
@Transactional
public void delete(Long id, Long currentUserId, String currentUserRole) {
    User user = userMapper.selectById(id);
    if (user == null || user.getDeleted() == 1) {
        throw new BusinessException(ResultCode.USER_NOT_FOUND);
    }

    if ("ADMIN".equals(currentUserRole)) {
        // Admin: cannot delete admin accounts or themselves
        if ("ADMIN".equals(user.getRole())) {
            throw new BusinessException(ResultCode.FORBIDDEN, "不能删除管理员账户");
        }
        if (user.getId().equals(currentUserId)) {
            throw new BusinessException(ResultCode.FORBIDDEN, "不能删除自己的账户");
        }
    } else {
        // Normal user: can only delete themselves
        if (!user.getId().equals(currentUserId)) {
            throw new BusinessException(ResultCode.FORBIDDEN, "只能删除自己的账户");
        }
    }

    // Logical delete
    userMapper.deleteById(id);
    operationLogService.log(id, "DELETE", "USER", id, null);
}
```

### Example 5: UserController with @PreAuthorize and current user

```java
// Source: Based on existing UserController, Spring Security 6.x patterns
@RestController
@RequestMapping("/api/users")
@Tag(name = "用户管理", description = "用户信息 CRUD 接口")
public class UserController {

    private final UserService userService;
    private final SecurityContextUtil securityContextUtil;

    public UserController(UserService userService, SecurityContextUtil securityContextUtil) {
        this.userService = userService;
        this.securityContextUtil = securityContextUtil;
    }

    @GetMapping("/me")
    @Operation(summary = "获取当前用户信息")
    public Result<UserVO> getCurrentUser() {
        Long currentUserId = securityContextUtil.getCurrentUserId();
        UserVO user = userService.getById(currentUserId);
        return Result.success(user);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")  // Only admins can list all users
    @Operation(summary = "查询用户列表")
    public Result<IPage<UserVO>> list(...) {
        // implementation unchanged — service filters based on caller's role
        IPage<UserVO> users = userService.list(page, size, keyword,
            securityContextUtil.getCurrentUserId(),
            securityContextUtil.getCurrentUserRole());
        return Result.success(users);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")  // Only admins can delete (service enforces self-deletion rule)
    @Operation(summary = "删除用户")
    public Result<Void> delete(@PathVariable Long id) {
        Long currentUserId = securityContextUtil.getCurrentUserId();
        String currentUserRole = securityContextUtil.getCurrentUserRole();
        userService.delete(id, currentUserId, currentUserRole);
        return Result.success("删除成功", null);
    }
}
```

## Don't Hand-Roll

| Problem | Don't Build | Use Instead | Why |
|---------|-------------|-------------|-----|
| Role-based authorization | Manual `if (role.equals(...))` checks in service methods | `@PreAuthorize` annotation | Declarative, fails closed (deny-by-default), centralized |
| Getting current user | Request parameter passing or ThreadLocal manually managed | `SecurityContextHolder.getContext().getAuthentication()` | Built into Spring Security, request-scoped, thread-safe |
| 403 response formatting | Custom exception handling per endpoint | `GlobalExceptionHandler.handleAccessDeniedException` | Already configured, returns consistent `Result.error(ResultCode.FORBIDDEN)` |
| Role string comparison | `user.getRole().equals("ADMIN")` scattered in code | Constants/enum for role values | Single source of truth, prevents typos |

**Key insight:** Spring Security's method security (`@PreAuthorize`) is designed exactly for this use case. Rolling manual checks loses the fail-closed security model (if you forget a check, access is accidentally granted).

## State of the Art

| Old Approach | Current Approach | When Changed | Impact |
|--------------|------------------|--------------|--------|
| URL-based security (intercept-url) | Method-level @PreAuthorize | Spring Security 3.0+ | Fine-grained, works at method level regardless of URL |
| Access decision voters | @PreAuthorize with SpEL expressions | Spring Security 3.0+ | More readable, composable expressions |
| Manual role checks in service | Declarative @PreAuthorize | Spring Security 3.0+ | Centralized, testable, fail-closed |
| Session-based auth | JWT stateless + SecurityContext | This project | Scalable, no server-side session |

**Deprecated/outdated:**
- `WebSecurityConfigurerAdapter` — removed in Spring Security 6.0 (project uses component-based config, correct)
- `@EnableGlobalMethodSecurity` — replaced by `@EnableMethodSecurity` (project already uses correct annotation)
- `SecurityContextHolder.MODE_GLOBAL` — deprecated in favor of default strategy

## Assumptions Log

> List all claims tagged `[ASSUMED]` in this research. The planner and discuss-phase use this section to identify decisions that need user confirmation before execution.

| # | Claim | Section | Risk if Wrong |
|---|-------|---------|---------------|
| A1 | `JwtUserDetailsService.loadUserById()` method exists and is used | Pattern 2, Option A | The method exists (line 41-50 in JwtUserDetailsService.java) but is NOT called from JwtAuthenticationFilter — only `loadUserByUsername` is called. The token's userId claim is not being used to reconstruct the full UserDetails. User must confirm: should we use the existing `loadUserById` path or the proposed `SecurityContextUtil` approach? |
| A2 | `@EnableMethodSecurity` triggers on concrete class proxies (not just interface methods) | Common Pitfalls | Verified: UserServiceImpl is a concrete class, Spring creates CGLIB proxy. Confirmed by design. |

**If this table is empty:** All claims in this research were verified or cited — no user confirmation needed.

## Open Questions

1. **How does JwtAuthenticationFilter pass userId to the service layer?**
   - What we know: `JwtUtil.generateToken(userDetails, user.getId())` puts userId in JWT claims; `JwtAuthenticationFilter` only calls `loadUserByUsername` which doesn't return userId.
   - What's unclear: Should we modify `JwtAuthenticationFilter` to also set userId in the Authentication's details/principal, or rely on `SecurityContextUtil` re-querying by username?
   - Recommendation: Extend `JwtAuthenticationFilter` to include userId in the Authentication object via a custom principal or details map.

2. **Should `UserService.delete()` require both `currentUserId` and `currentUserRole` as separate parameters, or should it receive a `User` entity from SecurityContextUtil?**
   - Current design (separate params) is explicit but verbose.
   - Alternative: pass the authenticated `UserDetails` and let service query for full user if needed.
   - Recommendation: Use separate params for now — cleaner interface, easier to test.

## Environment Availability

Step 2.6: SKIPPED (no external dependencies identified — this phase is pure Java/Spring Security code changes, no new tools or runtimes required).

## Validation Architecture

> Note: `workflow.nyquist_validation` key not found in `.planning/config.json`. Treating as enabled per system instructions.

### Test Framework
| Property | Value |
|----------|-------|
| Framework | JUnit 5 (spring-boot-starter-test includes it) + spring-security-test |
| Config file | None — standard Spring Boot test setup |
| Quick run command | `mvn test -Dtest=*SecurityTest,*PermissionTest` |
| Full suite command | `mvn test` |

### Phase Requirements to Test Map
| Req ID | Behavior | Test Type | Automated Command | File Exists? |
|--------|----------|-----------|-------------------|-------------|
| AUTH-03 | Admin listing all users; non-admin listing self only | Unit | `mvn test -Dtest=UserServiceImplTest#testList_FiltersByRole` | No |
| AUTH-04 | Admin can delete normal user | Unit | `mvn test -Dtest=UserServiceImplTest#testDelete_AdminCanDeleteUser` | No |
| AUTH-05 | Non-admin cannot delete others | Unit | `mvn test -Dtest=UserServiceImplTest#testDelete_UserCannotDeleteOthers` | No |
| AUTH-06 | Non-admin cannot access logs endpoint | Integration | `mvn test -Dtest=LogControllerTest#testList_ForbiddenForNormalUser` | No |
| PERMISSION-03 | @PreAuthorize returns 403 for unauthorized | Integration | `mvn test -Dtest=LogControllerTest#testAccessDenied_Returns403` | No |

### Wave 0 Gaps
- [ ] `backend/src/test/java/com/example/iot/service/impl/UserServiceImplTest.java` — covers AUTH-03/04/05 delete + list tests
- [ ] `backend/src/test/java/com/example/iot/controller/LogControllerTest.java` — covers AUTH-06 @PreAuthorize 403
- [ ] `backend/src/test/java/com/example/iot/security/SecurityContextUtilTest.java` — covers getCurrentUserId/getCurrentUserRole
- [ ] `backend/src/test/java/com/example/iot/controller/UserControllerTest.java` — covers USER/me endpoint + role-filtered list

### Sampling Rate
- **Per task commit:** `mvn test -Dtest=*SecurityTest,*PermissionTest -q` (quick, focused)
- **Per wave merge:** `mvn test -q` (full suite)
- **Phase gate:** Full suite green before `/gsd-verify-work`

## Security Domain

### Applicable ASVS Categories

| ASVS Category | Applies | Standard Control |
|---------------|---------|-----------------|
| V2 Authentication | No | N/A — AUTH-01/02 (registration validation) are separate phase |
| V3 Session Management | No | N/A — JWT stateless, no server-side sessions |
| V4 Access Control | **YES** | @PreAuthorize("hasRole('ADMIN')") + service-layer ownership checks |
| V5 Input Validation | Partial | Path variable `id` validated via @PathVariable type (Long) |
| V6 Cryptography | No | No cryptographic operations in this phase |

### Known Threat Patterns for Spring Security JWT

| Pattern | STRIDE | Standard Mitigation |
|---------|--------|---------------------|
| Privilege escalation (USER accessing ADMIN endpoints) | Elevation | @PreAuthorize fail-closed: denies by default |
| Horizontal privilege violation (USER A seeing USER B's data) | Information Disclosure | Service-layer ownership filter using SecurityContextUtil.getCurrentUserId() |
| JWT token theft/replay | Tampering/Repudiation | Token expiration validated in JwtUtil.validateToken() |
| Admin self-deletion | Denial | Service-layer check: admin cannot delete self |

### Security Controls Implemented by This Phase
- `@PreAuthorize("hasRole('ADMIN')")` on LogController.list() — prevents USER from accessing logs
- `@PreAuthorize("hasRole('ADMIN')")` on UserController.list() and delete() — restricts admin operations
- Service-layer user list filtering — prevents USER from enumerating all users
- Service-layer delete ownership check — prevents USER from deleting others; prevents ADMIN from self-deletion

## Sources

### Primary (HIGH confidence)
- `SecurityConfig.java` (line 26) — `@EnableMethodSecurity` present and configured
- `JwtUserDetailsService.java` (line 34) — `SimpleGrantedAuthority("ROLE_" + user.getRole())` confirms prefix handling
- `GlobalExceptionHandler.java` (lines 79-84) — `AccessDeniedException` mapped to `FORBIDDEN`
- `JwtUtil.java` (lines 78-82) — `generateToken(userDetails, user.getId())` confirms userId in claims
- `JwtAuthenticationFilter.java` (lines 54-63) — Authentication populated from UserDetails

### Secondary (MEDIUM confidence)
- Spring Security 6.x `@PreAuthorize` and `hasRole()` behavior — from training knowledge, verified against existing `JwtUserDetailsService` authority construction pattern which matches official Spring Security documentation

### Tertiary (LOW confidence)
- None — all critical claims verified against existing source code

## Metadata

**Confidence breakdown:**
- Standard stack: HIGH — all libraries verified in pom.xml
- Architecture: HIGH — patterns verified against existing codebase structure
- Pitfalls: MEDIUM — Spring Security pitfalls are well-documented; some edge cases (async thread context) not explicitly documented in project

**Research date:** 2026-04-18
**Valid until:** 2026-05-18 (30 days — Spring Security 6.x API is stable)
