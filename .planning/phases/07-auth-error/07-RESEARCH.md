# Phase 7: 注册错误提示优化 - Research

**Researched:** 2026-04-18
**Domain:** Spring Boot registration validation with user/email uniqueness enforcement
**Confidence:** HIGH

## Summary

Phase 7 addresses ambiguous error messages during user registration. Currently, when a username is already taken, the system returns a generic "用户已存在" message via `ResultCode.USER_ALREADY_EXISTS`. The email field does not exist at all in the User entity or database, so there is no email uniqueness check. This phase adds: (1) a database-level email column with unique constraint, (2) proper uniqueness checks for both username and email with user-friendly Chinese messages, and (3) the necessary DTO, entity, mapper, and service changes.

**Primary recommendation:** Add email column via Flyway V1.3 migration, use `BusinessException(Integer code, String message)` to override error messages for username conflicts, and add a new `EMAIL_ALREADY_EXISTS` result code for email conflicts.

## User Constraints (from CONTEXT.md)

### Locked Decisions

- **D-01:** Username conflict returns exact message: `"用户名已存在，请尝试其他用户名"`
- **D-02:** Email conflict returns exact message: `"该邮箱已被注册"`
- **D-03:** Email field is optional during registration (not required)
- **D-04:** Email unique constraint at DB level using `UNIQUE KEY uk_email (email)` -- multiple NULLs allowed
- **D-05:** Use `BusinessException` + existing `ResultCode`体系
  - Username conflict: reuse `ResultCode.USER_ALREADY_EXISTS` with custom message override
  - Email conflict: new `ResultCode.EMAIL_ALREADY_EXISTS` error code
- **D-06:** Flyway migration `V1.3__add_user_email.sql` adding `email VARCHAR(100) UNIQUE KEY`

### Deferred Ideas

None.

## Phase Requirements

| ID | Description | Research Support |
|----|-------------|------------------|
| AUTH-01 | Username already exists returns "用户名已存在，请尝试其他用户名" | `BusinessException(code, message)` override pattern confirmed at lines 15-18 of BusinessException.java |
| AUTH-02 | Email already registered returns "该邮箱已被注册" | New `EMAIL_ALREADY_EXISTS` code needed in ResultCode; email field missing in User entity at User.java |

## Architectural Responsibility Map

| Capability | Primary Tier | Secondary Tier | Rationale |
|------------|-------------|----------------|-----------|
| User entity | API/Backend | Database | User.java entity lives in backend; field added via DB migration |
| Registration validation | API/Backend | -- | UserServiceImpl.register() owns uniqueness checks |
| Error code enum | API/Backend | -- | ResultCode.java lives in common module |
| DB schema migration | Database | -- | Flyway migration V1.3 modifies schema |
| Mapper query | API/Backend | -- | UserMapper.selectByEmail added for email lookup |

## Standard Stack

| Library | Version | Purpose | Why Standard |
|---------|---------|---------|--------------|
| Spring Boot | 3.x | Backend framework | Per CLAUDE.md |
| MyBatis-Plus | (existing) | ORM mapper base | UserMapper extends BaseMapper; lambdaQuery() available |
| Flyway | (existing) | Database migrations | V1.2 already in use; next version V1.3 |
| Spring Security | (existing) | Password encoding | BCrypt already in use via PasswordEncoder |

No new dependencies required for this phase.

## Architecture Patterns

### System Architecture Diagram

```
[Registration Request: POST /api/auth/register]
          |
          v
[AuthController] --> [UserServiceImpl.register(UserRegisterDTO)]
          |                         |
          |                         v
          |              [UserMapper.selectByUsername(username)]
          |                         | (checks existence)
          |                         v
          |              [UserMapper.selectByEmail(email)] -- if email provided
          |                         | (checks existence)
          |                         v
          |              [BusinessException with custom message] -- if conflict
          |                         |
          |                         v
          |              [UserMapper.insert(user)] -- if valid
          |                         |
          v                         v
[GlobalExceptionHandler] <--- [HTTP 200 / error response]
```

### Recommended Project Structure

```
backend/src/main/
├── java/com/example/iot/
│   ├── entity/User.java                    -- add email field
│   ├── dto/UserRegisterDTO.java            -- add email field (optional)
│   ├── mapper/UserMapper.java              -- add selectByEmail()
│   ├── service/impl/UserServiceImpl.java   -- update register() with checks
│   └── common/result/ResultCode.java       -- add EMAIL_ALREADY_EXISTS
└── resources/db/migration/
    └── V1.3__add_user_email.sql            -- NEW: add email column
```

### Pattern: BusinessException with Message Override

**What:** Reuse existing `ResultCode` enum code while overriding the message string.
**When to use:** When a locked error code must produce a different user-facing message.
**Example:** [VERIFIED: BusinessException.java lines 15-18]

```java
// Username conflict -- override message on existing code
throw new BusinessException(ResultCode.USER_ALREADY_EXISTS.getCode(), "用户名已存在，请尝试其他用户名");

// Email conflict -- new error code
throw new BusinessException(ResultCode.EMAIL_ALREADY_EXISTS);
```

### Pattern: MyBatis-Plus lambdaQuery() for Email Check

**What:** Use MyBatis-Plus `lambdaQuery()` instead of a custom mapper method for email uniqueness check.
**When to use:** When a simple existence check is needed and a custom `@Select` is not required.
**Example:**

```java
// In UserServiceImpl.register()
if (StringUtils.hasText(dto.getEmail())) {
    Long emailCount = userMapper.lambdaQuery()
        .eq(User::getEmail, dto.getEmail())
        .ne(User::getDeleted, 1)  // exclude logically deleted
        .count();
    if (emailCount > 0) {
        throw new BusinessException(ResultCode.EMAIL_ALREADY_EXISTS);
    }
}
```

**Alternative considered:** Add `UserMapper.selectByEmail()` (following existing `selectByUsername` pattern). Per D-05 the team decides to reuse existing ResultCode + BusinessException, and the 07-CONTEXT.md canonical refs specify `selectByEmail` should be added to UserMapper. Use the mapper method for consistency with existing code.

## Don't Hand-Roll

| Problem | Don't Build | Use Instead | Why |
|---------|-------------|-------------|-----|
| Unique email check | Build raw SQL COUNT query | MyBatis-Plus `lambdaQuery()` or existing `@Select` pattern | Already available in codebase; consistency |
| Exception with custom message | Build new exception class | `BusinessException(code, message)` constructor | Already supports override per BusinessException.java:15-18 |
| Email uniqueness constraint | Application-level only check | DB `UNIQUE KEY uk_email (email)` | D-04 locks DB-level constraint; needed for data integrity |

## Common Pitfalls

### Pitfall 1: Forgetting to Set Email on User Entity

**What goes wrong:** Email is validated for uniqueness but never persisted to the `user` record.
**Why it happens:** The `register()` method creates a `User` entity and inserts it, but the email field on User entity is not yet added, so even after adding the column it may be omitted from the insert.
**How to avoid:** After adding `email` field to `User.java`, add `user.setEmail(dto.getEmail())` in `UserServiceImpl.register()` when email is provided.
**Warning signs:** Email field is null in database after registration despite being provided.

### Pitfall 2: Not Excluding Logically Deleted Users in Email Check

**What goes wrong:** A user who registered, deleted their account, and tries to re-register with the same email gets a false "email already exists" error.
**Why it happens:** The email uniqueness check may not filter `deleted = 1` records.
**How to avoid:** Use `lambdaQuery().ne(User::getDeleted, 1)` or `@Select("SELECT * FROM user WHERE email = #{email} AND deleted = 0")`.
**Warning signs:** Re-registration fails for deleted accounts.

### Pitfall 3: Nullable UNIQUE Key in MySQL

**What goes wrong:** MySQL `UNIQUE KEY` allows multiple NULL values, but some ORMs or query patterns may behave unexpectedly with NULL.
**Why it happens:** Per D-04, email is optional and the DB constraint `UNIQUE KEY uk_email (email)` is used. Multiple rows with `NULL` email are valid in MySQL.
**How to avoid:** The application-level check should only validate uniqueness when email is non-null (already implied by D-03 email is optional). The `@Select` query for `selectByEmail` should only query when email is provided.
**Warning signs:** Duplicate insert attempts with `NULL` email succeed (expected behavior, not a bug).

## Code Examples

### UserMapper.selectByEmail (following existing selectByUsername pattern)

**Source:** [UserMapper.java lines 18-19 -- existing selectByUsername]

```java
// New method to add to UserMapper.java
@Select("SELECT * FROM user WHERE email = #{email} AND deleted = 0")
User selectByEmail(@Param("email") String email);
```

### ResultCode.EMAIL_ALREADY_EXISTS (following existing enum pattern)

**Source:** [ResultCode.java lines 19-27 -- auth error codes]

```java
// Add after REGISTER_FAILED in the auth section (1000-1999 range)
EMAIL_ALREADY_EXISTS(1010, "该邮箱已被注册"),
```

### UserServiceImpl.register() changes (lines 60-82)

**Source:** [UserServiceImpl.java lines 58-82 -- current register() method]

```java
@Override
@Transactional
public Long register(UserRegisterDTO dto) {
    // Check username uniqueness with custom message
    User existingUser = userMapper.selectByUsername(dto.getUsername());
    if (existingUser != null) {
        throw new BusinessException(ResultCode.USER_ALREADY_EXISTS.getCode(),
            "用户名已存在，请尝试其他用户名");
    }

    // Check email uniqueness (only if provided)
    if (StringUtils.hasText(dto.getEmail())) {
        User existingEmail = userMapper.selectByEmail(dto.getEmail());
        if (existingEmail != null) {
            throw new BusinessException(ResultCode.EMAIL_ALREADY_EXISTS);
        }
    }

    // Create user
    User user = new User();
    user.setUsername(dto.getUsername());
    user.setPassword(passwordEncoder.encode(dto.getPassword()));
    user.setNickname(StringUtils.hasText(dto.getNickname()) ? dto.getNickname() : dto.getUsername());
    user.setRole("USER"); // Default user role

    // Set email if provided
    if (StringUtils.hasText(dto.getEmail())) {
        user.setEmail(dto.getEmail());
    }

    userMapper.insert(user);

    log.info("用户注册成功：{}", dto.getUsername());
    operationLogService.log(user.getId(), "REGISTER", "USER", user.getId(), null);

    return user.getId();
}
```

### V1.3 Flyway Migration

**Source:** [V1.2__add_log_statistics_tables.sql -- existing migration pattern]

```sql
-- Phase 7: Add email column to user table
-- Supports AUTH-01 and AUTH-02

ALTER TABLE user ADD COLUMN email VARCHAR(100) DEFAULT NULL COMMENT '邮箱';
CREATE UNIQUE INDEX uk_email ON user(email);
```

### User.java entity changes

**Source:** [User.java -- existing entity, no email field currently]

```java
// Add after openid field (line 37)
@Schema(description = "邮箱")
private String email;

// Add getter/setter after openid getter/setter
public String getEmail() { return email; }
public void setEmail(String email) { this.email = email; }
```

### UserRegisterDTO.java changes

**Source:** [UserRegisterDTO.java -- existing DTO, no email field currently]

```java
// Add after nickname field (line 24)
@Schema(description = "邮箱", example = "user@example.com")
private String email;

// Add getter/setter
public String getEmail() { return email; }
public void setEmail(String email) { this.email = email; }
```

## State of the Art

| Old Approach | Current Approach | When Changed | Impact |
|--------------|------------------|--------------|--------|
| Generic "用户已存在" message for username conflict | Custom message "用户名已存在，请尝试其他用户名" | Phase 7 | Clearer UX |
| No email field in user table | Email added as optional unique field | Phase 7 | Supports AUTH-02, future login by email |
| No email uniqueness check | DB constraint + application check | Phase 7 | Data integrity |

**Deprecated/outdated:**
- None in this phase scope.

## Assumptions Log

All claims in this research were verified by reading the actual source files. No assumptions were made that required user confirmation.

| # | Claim | Section | Risk if Wrong |
|---|-------|---------|---------------|
| -- | None | -- | -- |

## Open Questions

1. **Should selectByEmail use MyBatis-Plus lambdaQuery or @Select annotation?**
   - What we know: The existing `selectByUsername` uses `@Select` annotation (lines 18-19 of UserMapper.java). The 07-CONTEXT.md canonical refs also specify `selectByEmail` as a mapper method to add.
   - What's unclear: Whether the team prefers staying consistent with `@Select` annotation style vs. using `lambdaQuery()` in the service layer.
   - Recommendation: Use `@Select` annotation for `selectByEmail` to match existing `selectByUsername` pattern.

## Environment Availability

Step 2.6: SKIPPED (no external dependencies -- this is a pure code/config change phase with no CLI tools, databases, or external services required beyond what is already running the Spring Boot application).

## Validation Architecture

### Test Framework

| Property | Value |
|----------|-------|
| Framework | None detected |
| Config file | None -- project lacks test infrastructure |
| Quick run command | N/A |
| Full suite command | N/A |

### Phase Requirements to Test Map

| Req ID | Behavior | Test Type | Automated Command | File Exists? |
|--------|----------|-----------|-------------------|-------------|
| AUTH-01 | Username conflict returns specific message | unit | Manual -- verify BusinessException message | NO |
| AUTH-02 | Email conflict returns specific message | unit | Manual -- verify BusinessException message | NO |

### Wave 0 Gaps

- [ ] `backend/src/test/java/com/example/iot/service/UserServiceImplTest.java` -- covers AUTH-01 (username conflict check with custom message) and AUTH-02 (email conflict check)
- [ ] `backend/src/test/java/com/example/iot/mapper/UserMapperTest.java` -- covers selectByEmail query
- [ ] Framework install: `mvn test` ready -- if test infrastructure added

### Test Implementation Notes

Tests should verify:
1. `register()` throws `BusinessException` with message `"用户名已存在，请尝试其他用户名"` when username is duplicate
2. `register()` throws `BusinessException(EMAIL_ALREADY_EXISTS)` when email is duplicate
3. `register()` succeeds when email is not provided (per D-03: optional)
4. `register()` succeeds when email is provided and unique

## Security Domain

### Applicable ASVS Categories

| ASVS Category | Applies | Standard Control |
|---------------|---------|-----------------|
| V2 Authentication | yes | Email uniqueness prevents account takeover via duplicate registration |
| V5 Input Validation | yes | Email format should be validated if added to DTO (see below) |
| V4 Access Control | no | This phase does not change access control |

### Input Validation Note

The DTO does not currently validate email format. If email validation is desired in the future, add `@Email` annotation from `jakarta.validation.constraints`:

```java
@Schema(description = "邮箱", example = "user@example.com")
private String email;
```

This is not required by AUTH-01/AUTH-02 but would improve data quality.

## Sources

### Primary (HIGH confidence)
- `backend/src/main/java/com/example/iot/entity/User.java` -- current User entity without email field
- `backend/src/main/java/com/example/iot/dto/UserRegisterDTO.java` -- current DTO without email field
- `backend/src/main/java/com/example/iot/mapper/UserMapper.java` -- existing selectByUsername pattern
- `backend/src/main/java/com/example/iot/common/result/ResultCode.java` -- existing ResultCode enum structure
- `backend/src/main/java/com/example/iot/common/exception/BusinessException.java` -- BusinessException constructors including (Integer, String) override
- `backend/src/main/java/com/example/iot/service/impl/UserServiceImpl.java` -- register() method lines 58-82
- `backend/src/main/resources/db/init.sql` -- user table schema without email column
- `backend/src/main/resources/db/migration/V1.2__add_log_statistics_tables.sql` -- latest migration (V1.2), next is V1.3

### Secondary (MEDIUM confidence)
- None -- all findings verified against primary source files.

### Tertiary (LOW confidence)
- None.

## Metadata

**Confidence breakdown:**
- Standard stack: HIGH -- all libraries (Spring Boot, MyBatis-Plus, Flyway, BCrypt) confirmed in codebase
- Architecture: HIGH -- all change points verified in source files
- Pitfalls: HIGH -- identified from code review of existing patterns

**Research date:** 2026-04-18
**Valid until:** 2026-05-18 (30 days -- Spring Boot/MyBatis-Plus patterns are stable)
