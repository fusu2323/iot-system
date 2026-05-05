package com.example.iot.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.iot.dto.ExecutionLogQueryDTO;
import com.example.iot.entity.Scene;
import com.example.iot.entity.ScheduledTask;
import com.example.iot.entity.ScheduledTaskExecutionLog;
import com.example.iot.entity.vo.ExecutionLogVO;
import com.example.iot.mapper.SceneMapper;
import com.example.iot.mapper.ScheduledTaskExecutionLogMapper;
import com.example.iot.mapper.ScheduledTaskMapper;
import com.example.iot.service.IExecutionLogService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 定时任务执行日志服务实现类
 */
@Service
public class ExecutionLogServiceImpl implements IExecutionLogService {

    private final ScheduledTaskExecutionLogMapper executionLogMapper;
    private final SceneMapper sceneMapper;
    private final ScheduledTaskMapper taskMapper;

    public ExecutionLogServiceImpl(ScheduledTaskExecutionLogMapper executionLogMapper,
                                  SceneMapper sceneMapper,
                                  ScheduledTaskMapper taskMapper) {
        this.executionLogMapper = executionLogMapper;
        this.sceneMapper = sceneMapper;
        this.taskMapper = taskMapper;
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

    @Override
    public IPage<ExecutionLogVO> listByTaskId(Long taskId, ExecutionLogQueryDTO queryDTO) {
        Page<ScheduledTaskExecutionLog> page = new Page<>(queryDTO.getPage(), queryDTO.getSize());

        LambdaQueryWrapper<ScheduledTaskExecutionLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ScheduledTaskExecutionLog::getTaskId, taskId)
               .ge(queryDTO.getStartTime() != null, ScheduledTaskExecutionLog::getTriggerTime, queryDTO.getStartTime())
               .le(queryDTO.getEndTime() != null, ScheduledTaskExecutionLog::getTriggerTime, queryDTO.getEndTime())
               .orderByDesc(ScheduledTaskExecutionLog::getTriggerTime);

        IPage<ScheduledTaskExecutionLog> resultPage = executionLogMapper.selectPage(page, wrapper);
        return resultPage.convert(this::convertToVO);
    }

    private ExecutionLogVO convertToVO(ScheduledTaskExecutionLog log) {
        ExecutionLogVO vo = new ExecutionLogVO();
        vo.setId(log.getId());
        vo.setTaskId(log.getTaskId());
        vo.setSceneId(log.getSceneId());
        vo.setTriggerTime(log.getTriggerTime());
        vo.setStatus(log.getStatus());
        vo.setErrorMsg(log.getErrorMsg());
        vo.setCreateTime(log.getCreateTime());

        // Fetch task name
        ScheduledTask task = taskMapper.selectById(log.getTaskId());
        if (task != null) {
            vo.setTaskName(task.getName());
        }

        // Fetch scene name
        Scene scene = sceneMapper.selectById(log.getSceneId());
        if (scene != null) {
            vo.setSceneName(scene.getName());
        }

        return vo;
    }
}