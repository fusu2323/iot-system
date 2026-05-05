package com.example.iot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.iot.common.exception.BusinessException;
import com.example.iot.common.result.ResultCode;
import com.example.iot.dto.ScheduledTaskCreateDTO;
import com.example.iot.dto.ScheduledTaskUpdateDTO;
import com.example.iot.entity.Scene;
import com.example.iot.entity.ScheduledTask;
import com.example.iot.entity.ScheduledTaskType;
import com.example.iot.entity.vo.ScheduledTaskVO;
import com.example.iot.mapper.SceneMapper;
import com.example.iot.mapper.ScheduledTaskMapper;
import com.example.iot.service.SceneService;
import com.example.iot.service.IScheduledTaskService;
import com.example.iot.service.OperationLogService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

/**
 * 定时任务服务实现类
 */
@Service
public class ScheduledTaskServiceImpl implements IScheduledTaskService {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTaskServiceImpl.class);

    private final ScheduledTaskMapper scheduledTaskMapper;
    private final SceneMapper sceneMapper;
    private final SceneService sceneService;
    private final OperationLogService operationLogService;
    private final ObjectMapper objectMapper;

    public ScheduledTaskServiceImpl(ScheduledTaskMapper scheduledTaskMapper,
                                     SceneMapper sceneMapper,
                                     SceneService sceneService,
                                     OperationLogService operationLogService,
                                     ObjectMapper objectMapper) {
        this.scheduledTaskMapper = scheduledTaskMapper;
        this.sceneMapper = sceneMapper;
        this.sceneService = sceneService;
        this.operationLogService = operationLogService;
        this.objectMapper = objectMapper;
    }

    @Override
    @Transactional
    public Long create(ScheduledTaskCreateDTO dto, Long userId) {
        // Validate scene exists
        Scene scene = sceneMapper.selectById(dto.getSceneId());
        if (scene == null || scene.getDeleted() == 1) {
            throw new BusinessException(ResultCode.SCENE_NOT_FOUND);
        }

        // Validate schedule type
        ScheduledTaskType scheduleType = ScheduledTaskType.fromCode(dto.getScheduleType());

        // Generate cron expression from time_config
        String cronExpression = generateCronExpression(dto.getScheduleType(), dto.getTimeConfig());

        // Create entity
        ScheduledTask task = new ScheduledTask();
        task.setName(dto.getName());
        task.setSceneId(dto.getSceneId());
        task.setScheduleType(dto.getScheduleType());
        task.setCronExpression(cronExpression);
        task.setTimeConfig(dto.getTimeConfig());
        task.setIsEnabled(1); // D-05: Default enabled
        task.setCreatedBy(userId);

        // Calculate next fire time
        task.setNextFireTime(calculateNextFireTime(cronExpression));

        scheduledTaskMapper.insert(task);

        log.info("定时任务创建成功: {}, userId={}", dto.getName(), userId);
        operationLogService.log(userId, "CREATE", "SCHEDULED_TASK", task.getId(), null);

        return task.getId();
    }

    @Override
    @Transactional
    public ScheduledTaskVO update(Long id, ScheduledTaskUpdateDTO dto) {
        ScheduledTask task = scheduledTaskMapper.selectById(id);
        if (task == null || task.getDeleted() == 1) {
            throw new BusinessException(ResultCode.TASK_NOT_FOUND);
        }

        if (StringUtils.hasText(dto.getName())) {
            task.setName(dto.getName());
        }
        if (dto.getSceneId() != null) {
            Scene scene = sceneMapper.selectById(dto.getSceneId());
            if (scene == null || scene.getDeleted() == 1) {
                throw new BusinessException(ResultCode.SCENE_NOT_FOUND);
            }
            task.setSceneId(dto.getSceneId());
        }
        if (dto.getScheduleType() != null) {
            task.setScheduleType(dto.getScheduleType());
        }
        if (dto.getTimeConfig() != null) {
            task.setTimeConfig(dto.getTimeConfig());
            // Regenerate cron expression if time_config changed
            String cronExpression = generateCronExpression(task.getScheduleType(), task.getTimeConfig());
            task.setCronExpression(cronExpression);
            task.setNextFireTime(calculateNextFireTime(cronExpression));
        }

        scheduledTaskMapper.updateById(task);

        log.info("定时任务更新成功: {}", id);
        operationLogService.log(task.getCreatedBy(), "UPDATE", "SCHEDULED_TASK", id, null);

        return convertToVO(task);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        ScheduledTask task = scheduledTaskMapper.selectById(id);
        if (task == null || task.getDeleted() == 1) {
            throw new BusinessException(ResultCode.TASK_NOT_FOUND);
        }

        // Soft delete via @TableLogic
        scheduledTaskMapper.deleteById(id);

        log.info("定时任务删除成功: {}", id);
        operationLogService.log(task.getCreatedBy(), "DELETE", "SCHEDULED_TASK", id, null);
    }

    @Override
    public IPage<ScheduledTaskVO> listPage(Integer page, Integer size, String name) {
        Page<ScheduledTask> taskPage = new Page<>(page, size);
        LambdaQueryWrapper<ScheduledTask> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ScheduledTask::getDeleted, 0);
        if (StringUtils.hasText(name)) {
            wrapper.like(ScheduledTask::getName, name);
        }
        wrapper.orderByDesc(ScheduledTask::getCreateTime);

        IPage<ScheduledTask> resultPage = scheduledTaskMapper.selectPage(taskPage, wrapper);
        return resultPage.convert(this::convertToVO);
    }

    @Override
    public ScheduledTaskVO getById(Long id) {
        ScheduledTask task = scheduledTaskMapper.selectById(id);
        if (task == null || task.getDeleted() == 1) {
            throw new BusinessException(ResultCode.TASK_NOT_FOUND);
        }
        return convertToVO(task);
    }

    @Override
    @Transactional
    public ScheduledTaskVO toggle(Long id) {
        ScheduledTask task = scheduledTaskMapper.selectById(id);
        if (task == null || task.getDeleted() == 1) {
            throw new BusinessException(ResultCode.TASK_NOT_FOUND);
        }

        int currentEnabled = task.getIsEnabled() != null ? task.getIsEnabled() : 0;
        int newEnabled = (currentEnabled == 1) ? 0 : 1;
        task.setIsEnabled(newEnabled);

        scheduledTaskMapper.updateById(task);

        log.info("定时任务状态切换: id={}, isEnabled={}", id, newEnabled);
        operationLogService.log(task.getCreatedBy(), "TOGGLE", "SCHEDULED_TASK", id, null);

        return convertToVO(task);
    }

    /**
     * Generate cron expression from schedule type and time config JSON.
     * Mapping per 20-RESEARCH.md:
     * - DAILY(0): "0 {minute} {hour} * * ?"
     * - WORKDAY(1): "0 {minute} {hour} * * MON-FRI"
     * - WEEKEND(2): "0 {minute} {hour} * * SAT,SUN"
     * - CUSTOM(3): use timeConfig.cron directly
     */
    private String generateCronExpression(Integer scheduleType, String timeConfig) {
        try {
            JsonNode config = objectMapper.readTree(timeConfig);

            switch (scheduleType) {
                case 0: // DAILY
                    int hourDaily = config.get("hour").asInt();
                    int minuteDaily = config.get("minute").asInt();
                    return String.format("0 %d %d * * ?", minuteDaily, hourDaily);

                case 1: // WORKDAY
                    int hourWorkday = config.get("hour").asInt();
                    int minuteWorkday = config.get("minute").asInt();
                    return String.format("0 %d %d * * MON-FRI", minuteWorkday, hourWorkday);

                case 2: // WEEKEND
                    int hourWeekend = config.get("hour").asInt();
                    int minuteWeekend = config.get("minute").asInt();
                    return String.format("0 %d %d * * SAT,SUN", minuteWeekend, hourWeekend);

                case 3: // CUSTOM
                    return config.get("cron").asText();

                default:
                    throw new BusinessException(ResultCode.INVALID_PARAM);
            }
        } catch (Exception e) {
            log.error("Failed to parse timeConfig: {}", timeConfig, e);
            throw new BusinessException(ResultCode.INVALID_PARAM.getCode(), "时间配置格式错误");
        }
    }

    /**
     * Calculate next fire time from cron expression.
     * Simplified: stores null and relies on scheduler to compute at execution time.
     * Phase 21 will implement actual nextFireTime calculation using the scheduler.
     */
    private LocalDateTime calculateNextFireTime(String cronExpression) {
        // nextFireTime will be calculated by the scheduler at execution time
        // This avoids complexity of cron parsing here
        return null;
    }

    /**
     * Convert entity to VO with scene name lookup.
     */
    private ScheduledTaskVO convertToVO(ScheduledTask task) {
        ScheduledTaskVO vo = new ScheduledTaskVO();
        vo.setId(task.getId());
        vo.setName(task.getName());
        vo.setSceneId(task.getSceneId());
        vo.setScheduleType(task.getScheduleType());
        vo.setCronExpression(task.getCronExpression());
        vo.setTimeConfig(task.getTimeConfig());
        vo.setIsEnabled(task.getIsEnabled());
        vo.setNextFireTime(task.getNextFireTime());
        vo.setCreatedBy(task.getCreatedBy());
        vo.setCreateTime(task.getCreateTime());
        vo.setUpdateTime(task.getUpdateTime());

        // Lookup scene name
        if (task.getSceneId() != null) {
            Scene scene = sceneMapper.selectById(task.getSceneId());
            if (scene != null && scene.getDeleted() == 0) {
                vo.setSceneName(scene.getName());
            }
        }

        // Set schedule type description
        if (task.getScheduleType() != null) {
            try {
                ScheduledTaskType type = ScheduledTaskType.fromCode(task.getScheduleType());
                vo.setScheduleTypeDesc(type.getDescription());
            } catch (Exception e) {
                vo.setScheduleTypeDesc("未知");
            }
        }

        return vo;
    }
}