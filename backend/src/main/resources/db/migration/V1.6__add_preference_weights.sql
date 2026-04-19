-- Phase 11: Add preference weight columns to user_preference table
-- Supports RECOMMEND-02, RECOMMEND-04

ALTER TABLE user_preference ADD COLUMN click_weight INT DEFAULT 5 COMMENT '点击行为权重';
ALTER TABLE user_preference ADD COLUMN like_weight INT DEFAULT 10 COMMENT '收藏行为权重';
ALTER TABLE user_preference ADD COLUMN dislike_weight INT DEFAULT -20 COMMENT '不喜欢行为权重';
