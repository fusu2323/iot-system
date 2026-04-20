# Phase 7: Code Review Report

**Reviewed:** 2026-04-18T00:00:00Z
**Depth:** standard
**Files Reviewed:** 6
**Status:** issues_found

## Summary

Phase 7 adds email field support to the user registration system (AUTH-01, AUTH-02). The implementation correctly:
- Adds email as an optional field during registration
- Checks email uniqueness only when email is provided
- Uses parameterized queries (safe from SQL injection)
- Uses BusinessException with ResultCode error codes

However, there is one **race condition issue** that could cause unhandled exceptions when two concurrent registrations use the same email.

## Changes Reviewed

| File | Change |
|------|--------|
| `V1.3__add_user_email.sql` | Adds email column with unique index |
| `User.java` | Added email field |
| `UserRegisterDTO.java` | Added optional email field |
| `UserMapper.java` | Added `selectByEmail` method |
| `ResultCode.java` | Added `EMAIL_ALREADY_EXISTS` error code |
| `UserServiceImpl.java` | Email uniqueness check in register() |

## Critical Issues

None identified.

## Warnings

### WR-01: Race Condition in Email Uniqueness Check

**File:** `backend/src/main/java/com/example/iot/service/impl/UserServiceImpl.java:68-74`

**Issue:** The check-then-insert pattern in `register()` has a Time-of-Check to Time-of-Use (TOCTOU) race condition:

1. Thread A: Checks email - not found
2. Thread B: Checks email - not found
3. Thread A: Inserts user with email
4. Thread B: Inserts user with same email --> **DuplicateKeyException unhandled**

The database has a unique constraint (`uk_email`), so the second insert will fail with a `DuplicateKeyException`. However, the code does not catch this exception in the `register()` method, causing it to propagate as an unhandled 500 error instead of returning a user-friendly error.

**Fix:**
```java
import org.springframework.dao.DuplicateKeyException;

// In register() method:
try {
    userMapper.insert(user);
} catch (DuplicateKeyException e) {
    throw new BusinessException(ResultCode.EMAIL_ALREADY_EXISTS);
}
```

---

## Info

### IN-01: Inconsistent Error Message Style

**File:** `backend/src/main/java/com/example/iot/service/impl/UserServiceImpl.java:63-72`

**Issue:** Error messages for duplicate username use a custom message, while duplicate email uses the enum default:

```java
// Line 63-65 - Username: custom message
throw new BusinessException(ResultCode.USER_ALREADY_EXISTS.getCode(),
    "用户名已存在，请尝试其他用户名");

// Line 72 - Email: enum default message
throw new BusinessException(ResultCode.EMAIL_ALREADY_EXISTS);
```

**Fix:** Consider using a consistent style. Either both should use custom messages or both should use enum defaults:
```java
throw new BusinessException(ResultCode.EMAIL_ALREADY_EXISTS.getCode(),
    "该邮箱已被注册，请尝试其他邮箱");
```

---

## Security Analysis

### SQL Injection: PASS

**File:** `backend/src/main/java/com/example/iot/mapper/UserMapper.java:30-31`

```java
@Select("SELECT * FROM user WHERE email = #{email} AND deleted = 0")
User selectByEmail(@Param("email") String email);
```

Uses MyBatis `#{}` syntax (PreparedStatement binding), which is safe from SQL injection.

### Null Safety: PASS

**File:** `backend/src/main/java/com/example/iot/service/impl/UserServiceImpl.java:69`

```java
if (StringUtils.hasText(dto.getEmail())) {
    User existingEmail = userMapper.selectByEmail(dto.getEmail());
    ...
}
```

Correctly checks email uniqueness only when email is provided, as required by D-03.

---

## Verification Checklist

| Requirement | Status |
|-------------|--------|
| D-03: Email field is optional during registration | Implemented |
| D-05: Use BusinessException + ResultCode | Implemented |
| Email uniqueness checked only when provided | Implemented |
| SQL injection safe (parameterized query) | Verified |
| Race condition handled | **Not handled** |

---

_Reviewed: 2026-04-18T00:00:00Z_
_Reviewer: Claude (gsd-code-reviewer)_
_Depth: standard_
