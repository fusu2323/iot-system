-- =====================================================
-- 数据库变更脚本 - Phase 4: 场景与内容模块
-- =====================================================
-- 变更时间：2026-03-12
-- 变更说明：创建场景、场景设备关联、内容表
-- 前置版本：v0.9 (Phase 1-2)
-- 当前版本：v1.0 (Phase 3-4)
-- =====================================================

USE `iot_system`;

-- 场景表已在 init.sql 中创建，此处留痕记录

-- =============================================
-- 说明：Phase 3-4 的表结构变更
-- =============================================

-- 设备表 (device) - Phase 3
-- 场景表 (scene) - Phase 4
-- 场景设备关联表 (scene_device) - Phase 4
-- 内容表 (content) - Phase 4

-- 以上表已在 init.sql 中定义，此处仅做留痕记录

-- =============================================
-- 初始化数据 - 预设场景
-- =============================================

-- 检查是否已存在预设场景，不存在则插入
INSERT INTO `scene` (`name`, `description`, `icon`, `is_enabled`, `create_time`, `update_time`)
SELECT '回家模式', '打开灯光和空调，营造温馨氛围', 'home', 1, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM `scene` WHERE `name` = '回家模式');

INSERT INTO `scene` (`name`, `description`, `icon`, `is_enabled`, `create_time`, `update_time`)
SELECT '离家模式', '关闭所有设备，节能省电', 'logout', 1, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM `scene` WHERE `name` = '离家模式');

INSERT INTO `scene` (`name`, `description`, `icon`, `is_enabled`, `create_time`, `update_time`)
SELECT '影院模式', '调暗灯光，打开音响和电视', 'movie', 1, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM `scene` WHERE `name` = '影院模式');

INSERT INTO `scene` (`name`, `description`, `icon`, `is_enabled`, `create_time`, `update_time`)
SELECT '睡眠模式', '关闭所有灯光，开启睡眠场景', 'sleep', 1, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM `scene` WHERE `name` = '睡眠模式');

INSERT INTO `scene` (`name`, `description`, `icon`, `is_enabled`, `create_time`, `update_time`)
SELECT '阅读模式', '打开阅读灯，调节适宜亮度', 'read', 1, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM `scene` WHERE `name` = '阅读模式');

-- =============================================
-- 初始化数据 - 示例内容
-- =============================================

-- 检查是否已存在示例内容，不存在则插入
INSERT INTO `content` (`title`, `type`, `cover`, `description`, `rating`, `create_time`, `update_time`)
SELECT '流浪地球 2', 'MOVIE', '/covers/movie1.jpg', '科幻冒险电影', 4.5, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM `content` WHERE `title` = '流浪地球 2');

INSERT INTO `content` (`title`, `type`, `cover`, `description`, `rating`, `create_time`, `update_time`)
SELECT '流浪地球', 'MOVIE', '/covers/movie2.jpg', '科幻冒险电影', 4.3, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM `content` WHERE `title` = '流浪地球');

INSERT INTO `content` (`title`, `type`, `cover`, `description`, `rating`, `create_time`, `update_time`)
SELECT '三体', 'MOVIE', '/covers/movie3.jpg', '科幻电视剧', 4.7, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM `content` WHERE `title` = '三体');

INSERT INTO `content` (`title`, `type`, `cover`, `description`, `rating`, `create_time`, `update_time`)
SELECT '狂飙', 'MOVIE', '/covers/movie4.jpg', '犯罪剧情电视剧', 4.8, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM `content` WHERE `title` = '狂飙');

INSERT INTO `content` (`title`, `type`, `cover`, `description`, `rating`, `create_time`, `update_time`)
SELECT '周杰伦 - 最伟大的作品', 'MUSIC', '/covers/music1.jpg', '周杰伦专辑', 4.2, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM `content` WHERE `title` = '周杰伦 - 最伟大的作品');

INSERT INTO `content` (`title`, `type`, `cover`, `description`, `rating`, `create_time`, `update_time`)
SELECT '陈奕迅 - 孤勇者', 'MUSIC', '/covers/music2.jpg', '英雄联盟主题曲', 4.6, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM `content` WHERE `title` = '陈奕迅 - 孤勇者');

INSERT INTO `content` (`title`, `type`, `cover`, `description`, `rating`, `create_time`, `update_time`)
SELECT '塞尔达传说：王国之泪', 'GAME', '/covers/game1.jpg', '任天堂 Switch 游戏', 4.9, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM `content` WHERE `title` = '塞尔达传说：王国之泪');

INSERT INTO `content` (`title`, `type`, `cover`, `description`, `rating`, `create_time`, `update_time`)
SELECT '原神', 'GAME', '/covers/game2.jpg', '开放世界 RPG', 4.4, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM `content` WHERE `title` = '原神');

-- =============================================
-- 变更完成
-- =============================================
