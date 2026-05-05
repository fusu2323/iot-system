-- V2.0 Phase 20: 定时任务表
-- 定时任务CRUD + 启用/禁用 功能

CREATE TABLE IF NOT EXISTS `scheduled_task` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '任务ID',
    `name` VARCHAR(100) NOT NULL COMMENT '任务名称',
    `scene_id` BIGINT NOT NULL COMMENT '关联场景ID',
    `schedule_type` TINYINT NOT NULL COMMENT '调度类型：0=每日,1=工作日,2=周末,3=自定义',
    `cron_expression` VARCHAR(100) NOT NULL COMMENT 'CRON表达式',
    `time_config` JSON DEFAULT NULL COMMENT '时间配置JSON',
    `is_enabled` TINYINT DEFAULT 1 COMMENT '是否启用：0=禁用,1=启用',
    `next_fire_time` DATETIME DEFAULT NULL COMMENT '下次触发时间',
    `created_by` BIGINT DEFAULT NULL COMMENT '创建人',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除,1-已删除',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_scene_id` (`scene_id`),
    KEY `idx_is_enabled` (`is_enabled`),
    KEY `idx_deleted` (`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='定时任务表';