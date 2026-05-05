package com.example.iot.service;

import java.time.LocalDateTime;

/**
 * 定时任务执行日志服务接口
 */
public interface IExecutionLogService {

    /**
     * 保存执行日志
     * @param taskId 任务ID
     * @param sceneId 场景ID
     * @param triggerTime 触发时间
     * @param status 执行状态：0=失败,1=成功
     * @param errorMsg 错误信息（失败时）
     */
    void saveLog(Long taskId, Long sceneId, LocalDateTime triggerTime, Integer status, String errorMsg);
}