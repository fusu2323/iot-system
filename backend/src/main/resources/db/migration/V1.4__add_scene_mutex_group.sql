-- Phase 8: Add mutex_group column to scene table
-- Supports SCENE-01, SCENE-02, SCENE-04

ALTER TABLE scene ADD COLUMN mutex_group VARCHAR(50) DEFAULT NULL COMMENT '互斥组：同一组内只能有一个场景启用';
