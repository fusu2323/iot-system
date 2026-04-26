-- Phase 6: 日志与统计模块
-- 创建系统配置表
-- 执行时间：2026-03-12

SET NAMES utf8mb4;

-- ----------------------------
-- 系统配置表
-- ----------------------------
DROP TABLE IF EXISTS `system_config`;
CREATE TABLE `system_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '配置 ID',
  `config_key` varchar(100) NOT NULL COMMENT '配置键',
  `config_value` varchar(500) DEFAULT NULL COMMENT '配置值',
  `description` varchar(255) DEFAULT NULL COMMENT '配置描述',
  `config_type` varchar(20) DEFAULT 'SYSTEM' COMMENT '配置类型：SYSTEM-系统配置，BUSINESS-业务配置',
  `deleted` tinyint(1) DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_config_key` (`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统配置表';

-- ----------------------------
-- 初始化系统配置数据
-- ----------------------------
INSERT INTO `system_config` (`config_key`, `config_value`, `description`, `config_type`) VALUES
('app.name', '智能家居娱乐管理系统', '系统名称', 'SYSTEM'),
('app.version', '1.0.0', '系统版本', 'SYSTEM'),
('app.description', '基于 Spring Boot 的智能家居娱乐管理系统', '系统描述', 'SYSTEM'),
('device.max_count', '100', '单个用户最大设备数', 'BUSINESS'),
('scene.max_count', '20', '单个用户最大场景数', 'BUSINESS'),
('log.retention_days', '30', '操作日志保留天数', 'SYSTEM');
