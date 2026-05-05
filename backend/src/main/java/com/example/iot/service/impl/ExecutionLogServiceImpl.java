package com.example.iot.service.impl;

import com.example.iot.entity.ScheduledTaskExecutionLog;
import com.example.iot.mapper.ScheduledTaskExecutionLogMapper;
import com.example.iot.service.IExecutionLogService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 定时任务执行日志服务实现类
 */
@Service
public class ExecutionLogServiceImpl implements IExecutionLogService {

    private final ScheduledTaskExecutionLogMapper executionLogMapper;

    public ExecutionLogServiceImpl(ScheduledTaskExecutionLogMapper executionLogMapper) {
        this.executionLogMapper = executionLogMapper;
    }

    @Override
    public void saveLog(Long taskId, Long sceneId, LocalDateTime triggerTime, Integer status, String errorMsg) {
        ScheduledTaskExecutionLog logEntry = new ScheduledTaskExecutionLog();
        logEntry.setTaskId(taskId);
        logEntry.setSceneId(sceneId);
        logEntry.setTriggerTime(triggerTime);
        logEntry.setStatus(status);

        // Truncate error_msg to 1000 chars per Pitfall 5 in 21-RESEARCH.md
        if (errorMsg != null && errorMsg.length() > 1000) {
            errorMsg = errorMsg.substring(0, 1000);
        }
        logEntry.setErrorMsg(errorMsg);

        logEntry.setCreateTime(LocalDateTime.now());
        executionLogMapper.insert(logEntry);
    }
}