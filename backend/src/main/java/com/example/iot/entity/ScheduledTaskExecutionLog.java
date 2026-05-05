package com.example.iot.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 定时任务执行日志实体
 */
@Schema(description = "定时任务执行日志实体")
@TableName("scheduled_task_execution_log")
public class ScheduledTaskExecutionLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "记录 ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "任务 ID")
    private Long taskId;

    @Schema(description = "场景 ID")
    private Long sceneId;

    @Schema(description = "触发时间")
    private LocalDateTime triggerTime;

    @Schema(description = "执行状态：0=失败,1=成功")
    private Integer status;

    @Schema(description = "错误信息（失败时填写）")
    private String errorMsg;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getTaskId() { return taskId; }
    public void setTaskId(Long taskId) { this.taskId = taskId; }
    public Long getSceneId() { return sceneId; }
    public void setSceneId(Long sceneId) { this.sceneId = sceneId; }
    public LocalDateTime getTriggerTime() { return triggerTime; }
    public void setTriggerTime(LocalDateTime triggerTime) { this.triggerTime = triggerTime; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public String getErrorMsg() { return errorMsg; }
    public void setErrorMsg(String errorMsg) { this.errorMsg = errorMsg; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
}