-- V2.1 Phase 21: 定时任务执行日志表
-- cron调度执行 + 执行记录 功能

CREATE TABLE IF NOT EXISTS `scheduled_task_execution_log` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '记录ID',
    `task_id` BIGINT NOT NULL COMMENT '任务ID',
    `scene_id` BIGINT NOT NULL COMMENT '场景ID',
    `trigger_time` DATETIME NOT NULL COMMENT '触发时间',
    `status` TINYINT NOT NULL COMMENT '执行状态：0=失败,1=成功',
    `error_msg` VARCHAR(1000) DEFAULT NULL COMMENT '错误信息（失败时填写）',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_task_id` (`task_id`),
    KEY `idx_trigger_time` (`trigger_time`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='定时任务执行日志表';