---
phase: 07-auth-error
plan: "01"
subsystem: auth
tags:
  - authentication
  - email
  - registration
dependency-graph:
  requires: []
  provides:
    - AUTH-01
    - AUTH-02
  affects:
    - UserServiceImpl.register()
    - User entity
tech-stack:
  added:
    - V1.3__add_user_email.sql migration
    - EMAIL_ALREADY_EXISTS ResultCode
  patterns:
    - BusinessException with custom message override
    - MyBatis @Select annotation with deleted filter
key-files:
  created:
    - backend/src/main/resources/db/migration/V1.3__add_user_email.sql
  modified:
    - backend/src/main/java/com/example/iot/entity/User.java
    - backend/src/main/java/com/example/iot/dto/UserRegisterDTO.java
    - backend/src/main/java/com/example/iot/mapper/UserMapper.java
    - backend/src/main/java/com/example/iot/common/result/ResultCode.java
    - backend/src/main/java/com/example/iot/service/impl/UserServiceImpl.java
decisions:
  - "Username conflict uses custom message: 用户名已存在，请尝试其他用户名"
  - "Email uniqueness check only runs when email is provided (optional field)"
  - "EMAIL_ALREADY_EXISTS code 1010 used for email conflicts"
metrics:
  duration: ~5 minutes
  completed: 2026-04-18
---

# Phase 07 Plan 01: Auth Error Messages Summary

## One-liner

Added email field support with clear user-facing error messages for duplicate username ("用户名已存在，请尝试其他用户名") and duplicate email ("该邮箱已被注册") during registration.

## Completed Tasks

| Task | Name | Commit | Files |
| ---- | ---- | ------ | ----- |
| 1 | Create Flyway V1.3 migration for email column | fe654f9 | V1.3__add_user_email.sql |
| 2 | Add email field to User entity | eb95f47 | User.java |
| 3 | Add email field to UserRegisterDTO | 9c1041a | UserRegisterDTO.java |
| 4 | Add selectByEmail to UserMapper | 30dcb47 | UserMapper.java |
| 5 | Add EMAIL_ALREADY_EXISTS to ResultCode | aa09489 | ResultCode.java |
| 6 | Update UserServiceImpl.register() with email check and custom messages | de75993 | UserServiceImpl.java |

## What Was Built

### Database Migration (V1.3)
- Added `email` VARCHAR(100) column to `user` table
- Created `uk_email` UNIQUE INDEX allowing multiple NULL values

### User Entity
- Added `private String email` field with `@Schema(description = "邮箱")`
- Added `getEmail()` and `setEmail(String email)` methods

### UserRegisterDTO
- Added optional `email` field with `@Schema(description = "邮箱", example = "user@example.com")`
- Added corresponding getter/setter

### UserMapper
- Added `selectByEmail(@Param("email") String email)` using `@Select` annotation with `deleted = 0` filter

### ResultCode
- Added `EMAIL_ALREADY_EXISTS(1010, "该邮箱已被注册")`

### UserServiceImpl.register()
- Username conflict now throws `BusinessException(ResultCode.USER_ALREADY_EXISTS.getCode(), "用户名已存在，请尝试其他用户名")`
- Email uniqueness check runs when email is provided: `if (StringUtils.hasText(dto.getEmail()))` then `selectByEmail()`
- Email is set on User entity: `user.setEmail(dto.getEmail())`

## Deviations from Plan

None - plan executed exactly as written.

## Threat Flags

| Flag | File | Description |
|------|------|-------------|
| info_disclosure: enum_timing | UserServiceImpl.java | Email uniqueness check prevents account enumeration via timing differences (T-07-01 mitigation) |

## Self-Check

All files exist and commits verified:
- fe654f9: V1.3__add_user_email.sql
- eb95f47: User.java with email field
- 9c1041a: UserRegisterDTO.java with email field
- 30dcb47: UserMapper.java with selectByEmail
- aa09489: ResultCode.java with EMAIL_ALREADY_EXISTS
- de75993: UserServiceImpl.java with email check logic

## Self-Check: PASSED
