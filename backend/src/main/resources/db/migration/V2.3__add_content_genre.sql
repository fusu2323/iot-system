-- =====================================================
-- 数据库变更脚本 - 为内容表添加分类字段
-- =====================================================
-- 变更时间：2026-05-06
-- 变更说明：为 content 表添加 genre 字段，用于内容分类
-- =====================================================

USE `iot_system`;

-- 为 content 表添加 genre 列
ALTER TABLE `content` 
ADD COLUMN `genre` VARCHAR(50) DEFAULT NULL 
COMMENT '内容分类/流派' 
AFTER `type`;

-- =============================================
-- 丰富内容数据
-- =============================================

-- 电影类内容
INSERT INTO `content` (`title`, `type`, `genre`, `cover`, `description`, `rating`) 
SELECT '阿凡达：水之道', 'MOVIE', '科幻', '/covers/movie-avatar.jpg', '詹姆斯·卡梅隆执导的科幻史诗巨作，潘多拉星球的全新冒险', 4.8
WHERE NOT EXISTS (SELECT 1 FROM `content` WHERE `title` = '阿凡达：水之道');

INSERT INTO `content` (`title`, `type`, `genre`, `cover`, `description`, `rating`) 
SELECT '星际穿越', 'MOVIE', '科幻', '/covers/movie-interstellar.jpg', '诺兰执导的科幻经典，穿越时空的父爱之旅', 4.9
WHERE NOT EXISTS (SELECT 1 FROM `content` WHERE `title` = '星际穿越');

INSERT INTO `content` (`title`, `type`, `genre`, `cover`, `description`, `rating`) 
SELECT '盗梦空间', 'MOVIE', '科幻', '/covers/movie-inception.jpg', '在梦境中植入想法，诺兰的烧脑神作', 4.8
WHERE NOT EXISTS (SELECT 1 FROM `content` WHERE `title` = '盗梦空间');

INSERT INTO `content` (`title`, `type`, `genre`, `cover`, `description`, `rating`) 
SELECT '速度与激情10', 'MOVIE', '动作', '/covers/movie-fast10.jpg', '家族最终章，狂飙到底', 4.3
WHERE NOT EXISTS (SELECT 1 FROM `content` WHERE `title` = '速度与激情10');

INSERT INTO `content` (`title`, `type`, `genre`, `cover`, `description`, `rating`) 
SELECT '碟中谍7', 'MOVIE', '动作', '/covers/movie-mi7.jpg', '伊森·亨特的不可能任务', 4.5
WHERE NOT EXISTS (SELECT 1 FROM `content` WHERE `title` = '碟中谍7');

INSERT INTO `content` (`title`, `type`, `genre`, `cover`, `description`, `rating`) 
SELECT '肖申克的救赎', 'MOVIE', '剧情', '/covers/movie-shawshank.jpg', '影史第一，希望与自由的赞歌', 4.9
WHERE NOT EXISTS (SELECT 1 FROM `content` WHERE `title` = '肖申克的救赎');

INSERT INTO `content` (`title`, `type`, `genre`, `cover`, `description`, `rating`) 
SELECT '阿甘正传', 'MOVIE', '剧情', '/covers/movie-forrest.jpg', '人生就像一盒巧克力', 4.8
WHERE NOT EXISTS (SELECT 1 FROM `content` WHERE `title` = '阿甘正传');

INSERT INTO `content` (`title`, `type`, `genre`, `cover`, `description`, `rating`) 
SELECT '功夫熊猫', 'MOVIE', '动画', '/covers/movie-kungfu.jpg', '憨态可掬的熊猫大侠', 4.6
WHERE NOT EXISTS (SELECT 1 FROM `content` WHERE `title` = '功夫熊猫');

INSERT INTO `content` (`title`, `type`, `genre`, `cover`, `description`, `rating`) 
SELECT '寻梦环游记', 'MOVIE', '动画', '/covers/movie-coco.jpg', '皮克斯的催泪神作，关于家庭与梦想', 4.9
WHERE NOT EXISTS (SELECT 1 FROM `content` WHERE `title` = '寻梦环游记');

INSERT INTO `content` (`title`, `type`, `genre`, `cover`, `description`, `rating`) 
SELECT '釜山行', 'MOVIE', '恐怖', '/covers/movie-train.jpg', '开往釜山的丧尸列车', 4.5
WHERE NOT EXISTS (SELECT 1 FROM `content` WHERE `title` = '釜山行');

-- 音乐类内容
INSERT INTO `content` (`title`, `type`, `genre`, `cover`, `description`, `rating`) 
SELECT '林俊杰 - JJ20', 'MUSIC', '流行', '/covers/music-jj20.jpg', '林俊杰出道20周年精选专辑', 4.7
WHERE NOT EXISTS (SELECT 1 FROM `content` WHERE `title` = '林俊杰 - JJ20');

INSERT INTO `content` (`title`, `type`, `genre`, `cover`, `description`, `rating`) 
SELECT '邓紫棋 - 启示录', 'MUSIC', '流行', '/covers/music-gem.jpg', '邓紫棋的全新创作专辑', 4.6
WHERE NOT EXISTS (SELECT 1 FROM `content` WHERE `title` = '邓紫棋 - 启示录');

INSERT INTO `content` (`title`, `type`, `genre`, `cover`, `description`, `rating`) 
SELECT '周杰伦 - 范特西', 'MUSIC', '流行', '/covers/music-fantasy.jpg', '华语乐坛的经典神专', 4.9
WHERE NOT EXISTS (SELECT 1 FROM `content` WHERE `title` = '周杰伦 - 范特西');

INSERT INTO `content` (`title`, `type`, `genre`, `cover`, `description`, `rating`) 
SELECT '五月天 - 人生无限公司', 'MUSIC', '摇滚', '/covers/music-mayday.jpg', '五月天演唱会专辑', 4.7
WHERE NOT EXISTS (SELECT 1 FROM `content` WHERE `title` = '五月天 - 人生无限公司');

INSERT INTO `content` (`title`, `type`, `genre`, `cover`, `description`, `rating`) 
SELECT 'Coldplay - Music of the Spheres', 'MUSIC', '摇滚', '/covers/music-coldplay.jpg', '酷玩乐队的太空主题专辑', 4.5
WHERE NOT EXISTS (SELECT 1 FROM `content` WHERE `title` = 'Coldplay - Music of the Spheres');

INSERT INTO `content` (`title`, `type`, `genre`, `cover`, `description`, `rating`) 
SELECT 'Ed Sheeran - =', 'MUSIC', '流行', '/covers/music-ed.jpg', '黄老板的最新专辑', 4.4
WHERE NOT EXISTS (SELECT 1 FROM `content` WHERE `title` = 'Ed Sheeran - =');

INSERT INTO `content` (`title`, `type`, `genre`, `cover`, `description`, `rating`) 
SELECT 'Taylor Swift - Midnights', 'MUSIC', '流行', '/covers/music-taylor.jpg', '泰勒·斯威夫特的午夜专辑', 4.6
WHERE NOT EXISTS (SELECT 1 FROM `content` WHERE `title` = 'Taylor Swift - Midnights');

INSERT INTO `content` (`title`, `type`, `genre`, `cover`, `description`, `rating`) 
SELECT '薛之谦 - 无数', 'MUSIC', '流行', '/covers/music-joker.jpg', '薛之谦的全新专辑', 4.5
WHERE NOT EXISTS (SELECT 1 FROM `content` WHERE `title` = '薛之谦 - 无数');

INSERT INTO `content` (`title`, `type`, `genre`, `cover`, `description`, `rating`) 
SELECT '毛不易 - 平凡的一天', 'MUSIC', '民谣', '/covers/music-mao.jpg', '毛不易的治愈系专辑', 4.7
WHERE NOT EXISTS (SELECT 1 FROM `content` WHERE `title` = '毛不易 - 平凡的一天');

INSERT INTO `content` (`title`, `type`, `genre`, `cover`, `description`, `rating`) 
SELECT '华晨宇 - 新世界', 'MUSIC', '摇滚', '/covers/music-hua.jpg', '华晨宇的音乐新世界', 4.6
WHERE NOT EXISTS (SELECT 1 FROM `content` WHERE `title` = '华晨宇 - 新世界');

-- 游戏类内容
INSERT INTO `content` (`title`, `type`, `genre`, `cover`, `description`, `rating`) 
SELECT '艾尔登法环', 'GAME', 'RPG', '/covers/game-elden.jpg', 'FromSoftware的开放世界魂系大作', 4.9
WHERE NOT EXISTS (SELECT 1 FROM `content` WHERE `title` = '艾尔登法环');

INSERT INTO `content` (`title`, `type`, `genre`, `cover`, `description`, `rating`) 
SELECT '霍格沃茨之遗', 'GAME', 'RPG', '/covers/game-hogwarts.jpg', '哈利波特世界观开放世界游戏', 4.7
WHERE NOT EXISTS (SELECT 1 FROM `content` WHERE `title` = '霍格沃茨之遗');

INSERT INTO `content` (`title`, `type`, `genre`, `cover`, `description`, `rating`) 
SELECT '战神：诸神黄昏', 'GAME', '动作', '/covers/game-god.jpg', '奎托斯父子的北欧终章', 4.9
WHERE NOT EXISTS (SELECT 1 FROM `content` WHERE `title` = '战神：诸神黄昏');

INSERT INTO `content` (`title`, `type`, `genre`, `cover`, `description`, `rating`) 
SELECT '最后生还者2', 'GAME', '动作', '/covers/game-tlou.jpg', '末日世界的人性挣扎', 4.8
WHERE NOT EXISTS (SELECT 1 FROM `content` WHERE `title` = '最后生还者2');

INSERT INTO `content` (`title`, `type`, `genre`, `cover`, `description`, `rating`) 
SELECT '星穹铁道', 'GAME', 'RPG', '/covers/game-starrail.jpg', '米哈游的银河冒险RPG', 4.6
WHERE NOT EXISTS (SELECT 1 FROM `content` WHERE `title` = '星穹铁道');

INSERT INTO `content` (`title`, `type`, `genre`, `cover`, `description`, `rating`) 
SELECT '王者荣耀', 'GAME', 'MOBA', '/covers/game-honor.jpg', '国民级MOBA手游', 4.5
WHERE NOT EXISTS (SELECT 1 FROM `content` WHERE `title` = '王者荣耀');

INSERT INTO `content` (`title`, `type`, `genre`, `cover`, `description`, `rating`) 
SELECT '和平精英', 'GAME', '射击', '/covers/game-peace.jpg', '战术竞技手游', 4.4
WHERE NOT EXISTS (SELECT 1 FROM `content` WHERE `title` = '和平精英');

INSERT INTO `content` (`title`, `type`, `genre`, `cover`, `description`, `rating`) 
SELECT '我的世界', 'GAME', '沙盒', '/covers/game-minecraft.jpg', '无限可能的沙盒世界', 4.8
WHERE NOT EXISTS (SELECT 1 FROM `content` WHERE `title` = '我的世界');

INSERT INTO `content` (`title`, `type`, `genre`, `cover`, `description`, `rating`) 
SELECT '动物森友会', 'GAME', '模拟', '/covers/game-animal.jpg', '悠闲的海岛生活', 4.7
WHERE NOT EXISTS (SELECT 1 FROM `content` WHERE `title` = '动物森友会');

INSERT INTO `content` (`title`, `type`, `genre`, `cover`, `description`, `rating`) 
SELECT '赛博朋克2077', 'GAME', 'RPG', '/covers/game-cyberpunk.jpg', '夜之城的开放世界RPG', 4.6
WHERE NOT EXISTS (SELECT 1 FROM `content` WHERE `title` = '赛博朋克2077');
