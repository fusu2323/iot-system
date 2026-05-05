-- =====================================================
-- 数据库变更脚本 - 添加场景设备目标状态字段
-- =====================================================
-- 变更时间：2026-05-06
-- 变更说明：为 scene_device 表添加 target_status 字段，用于存储设备在对应场景中的目标状态
-- =====================================================

USE `iot_system`;

-- 为 scene_device 表添加 target_status 列
ALTER TABLE `scene_device` 
ADD COLUMN `target_status` INT DEFAULT 1 
COMMENT '目标状态：0-禁用，1-启用' 
AFTER `config`;

-- 更新现有记录，将 target_status 设置为 1（默认启用）
UPDATE `scene_device` SET `target_status` = 1 WHERE `target_status` IS NULL;
