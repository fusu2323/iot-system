-- =====================================================
-- 数据库变更脚本 - Phase 10: 内容数据扩充
-- =====================================================
-- 变更时间：2026-04-18
-- 变更说明：扩充内容表数据，新增24条内容（电影/音乐/游戏各8条）
-- 前置版本：v1.0 (Phase 4), v1.4 (Phase 9)
-- 当前版本：v1.5 (Phase 10)
-- 需求：CONTENT-01, CONTENT-02, CONTENT-03
-- =====================================================

USE `iot_system`;

-- =============================================
-- 扩充数据 - 新增电影 (8条)
-- =============================================

INSERT INTO `content` (`title`, `type`, `cover`, `description`, `rating`, `create_time`, `update_time`)
SELECT '阿凡达2', 'MOVIE', '/covers/movie5.jpg', '潘多拉星球水下世界科幻巨制', 4.6, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM `content` WHERE `title` = '阿凡达2');

INSERT INTO `content` (`title`, `type`, `cover`, `description`, `rating`, `create_time`, `update_time`)
SELECT '泰坦尼克号', 'MOVIE', '/covers/movie6.jpg', '经典爱情灾难电影', 4.7, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM `content` WHERE `title` = '泰坦尼克号');

INSERT INTO `content` (`title`, `type`, `cover`, `description`, `rating`, `create_time`, `update_time`)
SELECT '复仇者联盟', 'MOVIE', '/covers/movie7.jpg', '漫威超级英雄集结大作', 4.5, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM `content` WHERE `title` = '复仇者联盟');

INSERT INTO `content` (`title`, `type`, `cover`, `description`, `rating`, `create_time`, `update_time`)
SELECT '盗梦空间', 'MOVIE', '/covers/movie8.jpg', '克里斯托弗·诺兰执导的烧脑科幻片', 4.8, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM `content` WHERE `title` = '盗梦空间');

INSERT INTO `content` (`title`, `type`, `cover`, `description`, `rating`, `create_time`, `update_time`)
SELECT '星际穿越', 'MOVIE', '/covers/movie9.jpg', '探索宇宙奥秘的科幻史诗', 4.9, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM `content` WHERE `title` = '星际穿越');

INSERT INTO `content` (`title`, `type`, `cover`, `description`, `rating`, `create_time`, `update_time`)
SELECT '速度与激情', 'MOVIE', '/covers/movie10.jpg', '街头赛车动作系列电影', 4.3, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM `content` WHERE `title` = '速度与激情');

INSERT INTO `content` (`title`, `type`, `cover`, `description`, `rating`, `create_time`, `update_time`)
SELECT '变形金刚', 'MOVIE', '/covers/movie11.jpg', '机器人变形科幻动作片', 4.4, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM `content` WHERE `title` = '变形金刚');

INSERT INTO `content` (`title`, `type`, `cover`, `description`, `rating`, `create_time`, `update_time`)
SELECT '侏罗纪世界', 'MOVIE', '/covers/movie12.jpg', '恐龙主题科幻冒险电影', 4.5, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM `content` WHERE `title` = '侏罗纪世界');

-- =============================================
-- 扩充数据 - 新增音乐 (8条)
-- =============================================

INSERT INTO `content` (`title`, `type`, `cover`, `description`, `rating`, `create_time`, `update_time`)
SELECT '泰勒丝 - Anti-Hero', 'MUSIC', '/covers/music3.jpg', 'Taylor Swift流行单曲', 4.5, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM `content` WHERE `title` = '泰勒丝 - Anti-Hero');

INSERT INTO `content` (`title`, `type`, `cover`, `description`, `rating`, `create_time`, `update_time`)
SELECT '碧昂丝 - Crazy in Love', 'MUSIC', '/covers/music4.jpg', 'Beyonce经典R&B单曲', 4.6, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM `content` WHERE `title` = '碧昂丝 - Crazy in Love');

INSERT INTO `content` (`title`, `type`, `cover`, `description`, `rating`, `create_time`, `update_time`)
SELECT '周杰伦 - 夜曲', 'MUSIC', '/covers/music5.jpg', '周杰伦经典华语流行', 4.8, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM `content` WHERE `title` = '周杰伦 - 夜曲');

INSERT INTO `content` (`title`, `type`, `cover`, `description`, `rating`, `create_time`, `update_time`)
SELECT '林俊杰 - 可惜没如果', 'MUSIC', '/covers/music6.jpg', '林俊杰抒情华语金曲', 4.5, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM `content` WHERE `title` = '林俊杰 - 可惜没如果');

INSERT INTO `content` (`title`, `type`, `cover`, `description`, `rating`, `create_time`, `update_time`)
SELECT '五月天 - 倔强', 'MUSIC', '/covers/music7.jpg', '五月天摇滚代表作', 4.7, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM `content` WHERE `title` = '五月天 - 倔强');

INSERT INTO `content` (`title`, `type`, `cover`, `description`, `rating`, `create_time`, `update_time`)
SELECT '迈克尔杰克逊 - Billie Jean', 'MUSIC', '/covers/music8.jpg', 'MJ经典流行舞曲', 4.9, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM `content` WHERE `title` = '迈克尔杰克逊 - Billie Jean');

INSERT INTO `content` (`title`, `type`, `cover`, `description`, `rating`, `create_time`, `update_time`)
SELECT '艾德希兰 - Shape of You', 'MUSIC', '/covers/music9.jpg', 'Ed Sheeran全球热单', 4.4, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM `content` WHERE `title` = '艾德希兰 - Shape of You');

INSERT INTO `content` (`title`, `type`, `cover`, `description`, `rating`, `create_time`, `update_time`)
SELECT '林肯公园 - Numb', 'MUSIC', '/covers/music10.jpg', 'Linkin Park摇滚经典', 4.8, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM `content` WHERE `title` = '林肯公园 - Numb');

-- =============================================
-- 扩充数据 - 新增游戏 (8条)
-- =============================================

INSERT INTO `content` (`title`, `type`, `cover`, `description`, `rating`, `create_time`, `update_time`)
SELECT '使命召唤', 'GAME', '/covers/game3.jpg', '第一人称射击游戏系列', 4.5, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM `content` WHERE `title` = '使命召唤');

INSERT INTO `content` (`title`, `type`, `cover`, `description`, `rating`, `create_time`, `update_time`)
SELECT '守望先锋', 'GAME', '/covers/game4.jpg', '暴雪团队射击游戏', 4.3, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM `content` WHERE `title` = '守望先锋');

INSERT INTO `content` (`title`, `type`, `cover`, `description`, `rating`, `create_time`, `update_time`)
SELECT '我的世界', 'GAME', '/covers/game5.jpg', '沙盒建造游戏', 4.9, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM `content` WHERE `title` = '我的世界');

INSERT INTO `content` (`title`, `type`, `cover`, `description`, `rating`, `create_time`, `update_time`)
SELECT 'GTA5', 'GAME', '/covers/game6.jpg', '开放世界动作冒险游戏', 4.7, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM `content` WHERE `title` = 'GTA5');

INSERT INTO `content` (`title`, `type`, `cover`, `description`, `rating`, `create_time`, `update_time`)
SELECT '巫师3', 'GAME', '/covers/game7.jpg', '开放世界RPG角色扮演游戏', 4.8, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM `content` WHERE `title` = '巫师3');

INSERT INTO `content` (`title`, `type`, `cover`, `description`, `rating`, `create_time`, `update_time`)
SELECT '黑暗之魂', 'GAME', '/covers/game8.jpg', '高难度动作角色扮演游戏', 4.6, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM `content` WHERE `title` = '黑暗之魂');

INSERT INTO `content` (`title`, `type`, `cover`, `description`, `rating`, `create_time`, `update_time`)
SELECT '艾尔登法环', 'GAME', '/covers/game9.jpg', '宫崎英高动作RPG', 4.9, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM `content` WHERE `title` = '艾尔登法环');

INSERT INTO `content` (`title`, `type`, `cover`, `description`, `rating`, `create_time`, `update_time`)
SELECT '超级马里奥奥德赛', 'GAME', '/covers/game10.jpg', '任天堂Switch平台3D马里奥游戏', 4.9, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM `content` WHERE `title` = '超级马里奥奥德赛');

-- =============================================
-- 变更完成
-- =============================================
