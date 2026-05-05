-- Phase 22/23: Add stat_log table for usage statistics
-- Supports STATS-01, STATS-02, STATS-03, STATS-04, STATS-05
use `iot_system`;
DROP TABLE IF EXISTS `stat_log`;
CREATE TABLE `stat_log` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '日志 ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户 ID',
    `target_type` VARCHAR(20) NOT NULL COMMENT '目标类型：DEVICE/SCENE',
    `target_id` BIGINT(20) NOT NULL COMMENT '目标 ID',
    `action` VARCHAR(20) NOT NULL COMMENT '动作：ACTIVATE/TRIGGER',
    `stat_date` DATE NOT NULL COMMENT '统计日期（用于日/周/月聚合）',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_target` (`target_type`, `target_id`),
    KEY `idx_stat_date` (`stat_date`),
    KEY `idx_user_date` (`user_id`, `stat_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='使用统计日志表';
INSERT INTO iot_system.stat_log (user_id, target_type, target_id, action, stat_date) VALUES
                                                                                         (1, 'DEVICE', 1, 'ACTIVATE', CURDATE()),
                                                                                         (1, 'SCENE', 1, 'TRIGGER', CURDATE()),
                                                                                         (1, 'DEVICE', 2, 'ACTIVATE', CURDATE()),
                                                                                         (1, 'SCENE', 2, 'TRIGGER', CURDATE());
