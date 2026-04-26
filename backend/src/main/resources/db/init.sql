-- 智能家居娱乐管理系统 - 数据库初始化脚本
-- MySQL 8.x

-- 设置字符集
SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

-- 创建数据库
CREATE DATABASE IF NOT EXISTS `iot_system` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE `iot_system`;

-- =============================================
-- 1. 用户表 (user)
-- =============================================
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户 ID',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名',
    `password` VARCHAR(255) NOT NULL COMMENT '密码（BCrypt 加密）',
    `nickname` VARCHAR(100) DEFAULT NULL COMMENT '昵称',
    `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像 URL',
    `role` VARCHAR(20) DEFAULT 'USER' COMMENT '角色：ADMIN/USER',
    `openid` VARCHAR(100) DEFAULT NULL COMMENT '微信 openid',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- =============================================
-- 2. 设备表 (device)
-- =============================================
DROP TABLE IF EXISTS `device`;
CREATE TABLE `device` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '设备 ID',
    `name` VARCHAR(100) NOT NULL COMMENT '设备名称',
    `type` VARCHAR(50) NOT NULL COMMENT '设备类型：TV/SPEAKER/LIGHT/ etc.',
    `room` VARCHAR(50) DEFAULT NULL COMMENT '房间',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `is_online` TINYINT DEFAULT 0 COMMENT '在线状态：0-离线，1-在线',
    `user_id` BIGINT DEFAULT NULL COMMENT '所属用户 ID',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_type` (`type`),
    KEY `idx_room` (`room`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='设备表';

-- =============================================
-- 3. 场景表 (scene)
-- =============================================
DROP TABLE IF EXISTS `scene`;
CREATE TABLE `scene` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '场景 ID',
    `name` VARCHAR(100) NOT NULL COMMENT '场景名称',
    `description` VARCHAR(255) DEFAULT NULL COMMENT '场景描述',
    `icon` VARCHAR(50) DEFAULT NULL COMMENT '场景图标',
    `is_enabled` TINYINT DEFAULT 1 COMMENT '是否启用：0-禁用，1-启用',
    `user_id` BIGINT DEFAULT NULL COMMENT '所属用户 ID',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='场景表';

-- =============================================
-- 4. 场景设备关联表 (scene_device)
-- =============================================
DROP TABLE IF EXISTS `scene_device`;
CREATE TABLE `scene_device` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '关联 ID',
    `scene_id` BIGINT NOT NULL COMMENT '场景 ID',
    `device_id` BIGINT NOT NULL COMMENT '设备 ID',
    `config` JSON DEFAULT NULL COMMENT '设备配置',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_scene_id` (`scene_id`),
    KEY `idx_device_id` (`device_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='场景设备关联表';

-- =============================================
-- 5. 内容表 (content)
-- =============================================
DROP TABLE IF EXISTS `content`;
CREATE TABLE `content` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '内容 ID',
    `title` VARCHAR(100) NOT NULL COMMENT '内容标题',
    `type` VARCHAR(20) NOT NULL COMMENT '内容类型：MOVIE/MUSIC/GAME',
    `cover` VARCHAR(255) DEFAULT NULL COMMENT '封面 URL',
    `description` TEXT DEFAULT NULL COMMENT '内容描述',
    `rating` DECIMAL(2,1) DEFAULT NULL COMMENT '评分：0.0-5.0',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_type` (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='内容表';

-- =============================================
-- 6. 用户偏好表 (user_preference)
-- =============================================
DROP TABLE IF EXISTS `user_preference`;
CREATE TABLE `user_preference` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '偏好 ID',
    `user_id` BIGINT NOT NULL COMMENT '用户 ID',
    `content_type` VARCHAR(20) DEFAULT NULL COMMENT '内容类型',
    `preference_score` INT DEFAULT 0 COMMENT '偏好分数',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户偏好表';

-- =============================================
-- 7. 操作日志表 (operation_log)
-- =============================================
DROP TABLE IF EXISTS `operation_log`;
CREATE TABLE `operation_log` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '日志 ID',
    `user_id` BIGINT DEFAULT NULL COMMENT '用户 ID',
    `operation` VARCHAR(50) DEFAULT NULL COMMENT '操作类型',
    `target_type` VARCHAR(50) DEFAULT NULL COMMENT '目标类型',
    `target_id` BIGINT DEFAULT NULL COMMENT '目标 ID',
    `ip_address` VARCHAR(50) DEFAULT NULL COMMENT 'IP 地址',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_operation` (`operation`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='操作日志表';

-- =============================================
-- 8. 推荐记录表 (recommendation)
-- =============================================
DROP TABLE IF EXISTS `recommendation`;
CREATE TABLE `recommendation` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '推荐 ID',
    `user_id` BIGINT NOT NULL COMMENT '用户 ID',
    `content_id` BIGINT NOT NULL COMMENT '内容 ID',
    `reason` VARCHAR(100) DEFAULT NULL COMMENT '推荐原因',
    `score` INT DEFAULT 50 COMMENT '推荐分数',
    `is_clicked` TINYINT DEFAULT 0 COMMENT '是否已点击',
    `is_liked` TINYINT DEFAULT 0 COMMENT '是否已收藏',
    `is_disliked` TINYINT DEFAULT 0 COMMENT '是否不喜欢',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_content_id` (`content_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='推荐记录表';

-- =============================================
-- 9. 推荐反馈表 (recommendation_feedback)
-- =============================================
DROP TABLE IF EXISTS `recommendation_feedback`;
CREATE TABLE `recommendation_feedback` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '反馈 ID',
    `user_id` BIGINT NOT NULL COMMENT '用户 ID',
    `content_id` BIGINT NOT NULL COMMENT '内容 ID',
    `recommendation_id` BIGINT DEFAULT NULL COMMENT '推荐 ID',
    `feedback_type` VARCHAR(20) NOT NULL COMMENT '反馈类型：LIKE/DISLIKE/CLICK/COLLECT',
    `score` INT DEFAULT NULL COMMENT '反馈分数',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_content_id` (`content_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='推荐反馈表';

-- =============================================
-- 初始化数据
-- =============================================

-- 插入管理员账号（密码：admin123，BCrypt 加密后）
-- BCrypt hash for 'admin123': $2b$10$zx9GL3uyMlOazwcqD0UfneEVkcYKCFOm15O5baLQw6boP2WYZM9iu
INSERT INTO `user` (`username`, `password`, `nickname`, `role`) VALUES
('admin', '$2b$10$zx9GL3uyMlOazwcqD0UfneEVkcYKCFOm15O5baLQw6boP2WYZM9iu', '管理员', 'ADMIN'),
('user', '$2b$10$zx9GL3uyMlOazwcqD0UfneEVkcYKCFOm15O5baLQw6boP2WYZM9iu', '普通用户', 'USER');

-- 插入预设场景
INSERT INTO `scene` (`name`, `description`, `icon`, `is_enabled`, `user_id`) VALUES
('回家模式', '打开灯光和空调，营造温馨氛围', 'home', 1, NULL),
('离家模式', '关闭所有设备，节能省电', 'logout', 1, NULL),
('影院模式', '调暗灯光，打开音响和电视', 'movie', 1, NULL),
('睡眠模式', '关闭所有灯光，开启睡眠场景', 'sleep', 1, NULL),
('阅读模式', '打开阅读灯，调节适宜亮度', 'read', 1, NULL);

-- 插入示例内容
INSERT INTO `content` (`title`, `type`, `cover`, `description`, `rating`) VALUES
('流浪地球 2', 'MOVIE', '/covers/movie1.jpg', '科幻冒险电影', 4.5),
('流浪地球', 'MOVIE', '/covers/movie2.jpg', '科幻冒险电影', 4.3),
('三体', 'MOVIE', '/covers/movie3.jpg', '科幻电视剧', 4.7),
('狂飙', 'MOVIE', '/covers/movie4.jpg', '犯罪剧情电视剧', 4.8),
('周杰伦 - 最伟大的作品', 'MUSIC', '/covers/music1.jpg', '周杰伦专辑', 4.2),
('陈奕迅 - 孤勇者', 'MUSIC', '/covers/music2.jpg', '英雄联盟主题曲', 4.6),
('塞尔达传说：王国之泪', 'GAME', '/covers/game1.jpg', '任天堂 Switch 游戏', 4.9),
('原神', 'GAME', '/covers/game2.jpg', '开放世界 RPG', 4.4);
