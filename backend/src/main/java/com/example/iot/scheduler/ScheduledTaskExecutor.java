package com.example.iot.scheduler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.iot.entity.ScheduledTask;
import com.example.iot.mapper.ScheduledTaskMapper;
import com.example.iot.service.SceneService;
import com.example.iot.service.impl.ExecutionLogServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.SimpleTriggerContext;
import org.springframework.scheduling.TriggerContext;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 定时任务调度执行器
 * 扫描enabled任务，按cron表达式触发场景，记录执行结果
 */
@Component
public class ScheduledTaskExecutor {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTaskExecutor.class);

    private final ScheduledTaskMapper taskMapper;
    private final SceneService sceneService;
    private final ExecutionLogServiceImpl executionLogService;
    private final ObjectMapper objectMapper;

    public ScheduledTaskExecutor(ScheduledTaskMapper taskMapper,
                                  SceneService sceneService,
                                  ExecutionLogServiceImpl executionLogService,
                                  ObjectMapper objectMapper) {
        this.taskMapper = taskMapper;
        this.sceneService = sceneService;
        this.executionLogService = executionLogService;
        this.objectMapper = objectMapper;
    }

    /**
     * 每分钟扫描一次所有enabled任务，评估cron表达式判断是否触发
     */
    @Scheduled(fixedRate = 60000)
    public void scanAndTrigger() {
        LocalDateTime now = LocalDateTime.now();
        List<ScheduledTask> enabledTasks = taskMapper.selectList(
            new LambdaQueryWrapper<ScheduledTask>()
                .eq(ScheduledTask::getIsEnabled, 1)
                .eq(ScheduledTask::getDeleted, 0)
        );

        for (ScheduledTask task : enabledTasks) {
            // Re-check is_enabled immediately before trigger (Race condition mitigation from Pitfall 4)
            task = taskMapper.selectById(task.getId());
            if (task == null || task.getIsEnabled() == 0) {
                continue;
            }
            if (shouldFireNow(task.getCronExpression(), now)) {
                executeAndLog(task, now);
            }
        }
    }

    /**
     * 使用CronTrigger判断当前时间是否应该在触发窗口内
     */
    private boolean shouldFireNow(String cronExpression, LocalDateTime now) {
        try {
            CronTrigger trigger = new CronTrigger(cronExpression, ZoneId.of("Asia/Shanghai"));
            Instant nowInstant = now.toInstant(ZoneId.of("Asia/Shanghai").getRules().getOffset(now));
            TriggerContext context = new SimpleTriggerContext(Clock.system(ZoneId.of("Asia/Shanghai")));
            Instant nextFire = trigger.nextExecution(context);
            if (nextFire == null) return false;
            // Fire if next fire time is within the current minute window
            long diffSeconds = java.time.Duration.between(nowInstant, nextFire).getSeconds();
            return diffSeconds >= 0 && diffSeconds < 60;
        } catch (Exception e) {
            log.error("Invalid cron expression: {}", cronExpression, e);
            return false;
        }
    }

    /**
     * 执行场景触发并记录日志
     */
    private void executeAndLog(ScheduledTask task, LocalDateTime triggerTime) {
        try {
            sceneService.trigger(task.getSceneId());
            executionLogService.saveLog(task.getId(), task.getSceneId(), triggerTime, 1, null);
            log.info("scheduled task triggered: taskId={}, sceneId={}", task.getId(), task.getSceneId());
        } catch (Exception e) {
            // D-03: No retry on failure — record error and continue
            String errorMsg = buildErrorMsg(e);
            executionLogService.saveLog(task.getId(), task.getSceneId(), triggerTime, 0, errorMsg);
            log.error("scheduled task failed: taskId={}, sceneId={}, error={}", task.getId(), task.getSceneId(), e.getMessage());
        }
        updateNextFireTime(task);
    }

    /**
     * 构建错误信息JSON，包含异常类型和消息
     */
    private String buildErrorMsg(Exception e) {
        try {
            Map<String, Object> errorDetail = new LinkedHashMap<>();
            errorDetail.put("type", e.getClass().getSimpleName());
            errorDetail.put("msg", e.getMessage());
            String json = objectMapper.writeValueAsString(errorDetail);
            return json.length() > 1000 ? json.substring(0, 1000) : json;
        } catch (Exception ex) {
            return e.getMessage() != null ? e.getMessage().substring(0, Math.min(e.getMessage().length(), 1000)) : e.getClass().getSimpleName();
        }
    }

    /**
     * 计算并更新任务的下次触发时间
     */
    private void updateNextFireTime(ScheduledTask task) {
        try {
            CronTrigger trigger = new CronTrigger(task.getCronExpression(), ZoneId.of("Asia/Shanghai"));
            TriggerContext context = new SimpleTriggerContext(Clock.system(ZoneId.of("Asia/Shanghai")));
            Instant nextFire = trigger.nextExecution(context);
            if (nextFire != null) {
                task.setNextFireTime(nextFire.atZone(ZoneId.of("Asia/Shanghai")).toLocalDateTime());
                taskMapper.updateById(task);
            }
        } catch (Exception e) {
            log.error("Failed to update next fire time for task: {}", task.getId(), e);
        }
    }
}
