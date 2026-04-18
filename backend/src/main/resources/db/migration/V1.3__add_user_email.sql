-- Phase 7: Add email column to user table
-- Supports AUTH-01 and AUTH-02

ALTER TABLE user ADD COLUMN email VARCHAR(100) DEFAULT NULL COMMENT '邮箱';
CREATE UNIQUE INDEX uk_email ON user(email);