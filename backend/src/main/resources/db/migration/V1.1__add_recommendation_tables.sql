-- =====================================================
-- 数据库变更脚本 - Phase 5: 推荐算法与反馈模块
-- =====================================================
-- 变更时间：2026-03-12
-- 变更说明：新增推荐记录表和推荐反馈表
-- 前置版本：v1.0 (Phase 1-3)
-- 当前版本：v1.1 (Phase 5)
-- =====================================================

USE `iot_system`;

-- =============================================
-- 8. 推荐记录表 (recommendation)
-- =============================================
-- 说明：记录系统向用户推荐的内容及用户反馈状态
-- 用于推荐算法优化和个性化推荐
DROP TABLE IF EXISTS `recommendation`;
CREATE TABLE `recommendation` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '推荐 ID',
    `user_id` BIGINT NOT NULL COMMENT '用户 ID',
    `content_id` BIGINT NOT NULL COMMENT '内容 ID',
    `reason` VARCHAR(100) DEFAULT NULL COMMENT '推荐原因，如：基于您的偏好、热门推荐等',
    `score` INT DEFAULT 50 COMMENT '推荐分数 (0-100)，分数越高表示越推荐',
    `is_clicked` TINYINT DEFAULT 0 COMMENT '是否已点击：0-未点击，1-已点击',
    `is_liked` TINYINT DEFAULT 0 COMMENT '是否已收藏/喜欢：0-未收藏，1-已收藏',
    `is_disliked` TINYINT DEFAULT 0 COMMENT '是否不喜欢：0-否，1-是',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`) COMMENT '用户 ID 索引，用于查询用户推荐列表',
    KEY `idx_content_id` (`content_id`) COMMENT '内容 ID 索引，用于查询内容推荐情况',
    KEY `idx_create_time` (`create_time`) COMMENT '创建时间索引，用于按时间排序'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='推荐记录表';

-- =============================================
-- 9. 推荐反馈表 (recommendation_feedback)
-- =============================================
-- 说明：记录用户对推荐内容的详细反馈信息
-- 用于推荐算法优化和用户偏好分析
DROP TABLE IF EXISTS `recommendation_feedback`;
CREATE TABLE `recommendation_feedback` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '反馈 ID',
    `user_id` BIGINT NOT NULL COMMENT '用户 ID',
    `content_id` BIGINT NOT NULL COMMENT '内容 ID',
    `recommendation_id` BIGINT DEFAULT NULL COMMENT '推荐记录 ID，关联 recommendation 表',
    `feedback_type` VARCHAR(20) NOT NULL COMMENT '反馈类型：LIKE-喜欢，DISLIKE-不喜欢，CLICK-点击，COLLECT-收藏',
    `score` INT DEFAULT NULL COMMENT '反馈分数 (1-5)，用户对内容的评分',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`) COMMENT '用户 ID 索引，用于查询用户反馈历史',
    KEY `idx_content_id` (`content_id`) COMMENT '内容 ID 索引，用于查询内容反馈情况',
    KEY `idx_feedback_type` (`feedback_type`) COMMENT '反馈类型索引，用于统计分析',
    KEY `idx_create_time` (`create_time`) COMMENT '创建时间索引，用于按时间排序'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='推荐反馈表';

-- =============================================
-- 数据变更（可选）
-- =============================================

-- 为管理员用户初始化偏好设置（用户 ID=1）
-- 假设管理员喜欢电影类型
INSERT INTO `user_preference` (`user_id`, `content_type`, `preference_score`, `create_time`, `update_time`)
VALUES
    (1, 'MOVIE', 80, NOW(), NOW()),
    (1, 'GAME', 60, NOW(), NOW())
ON DUPLICATE KEY UPDATE `preference_score` = VALUES(`preference_score`);

-- 为测试用户初始化偏好设置（用户 ID=2）
-- 假设测试用户喜欢音乐类型
INSERT INTO `user_preference` (`user_id`, `content_type`, `preference_score`, `create_time`, `update_time`)
VALUES
    (2, 'MUSIC', 85, NOW(), NOW()),
    (2, 'MOVIE', 50, NOW(), NOW())
ON DUPLICATE KEY UPDATE `preference_score` = VALUES(`preference_score`);

-- =============================================
-- 变更完成
-- =============================================
