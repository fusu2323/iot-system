package com.example.iot.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 定时任务实体
 */
@Schema(description = "定时任务实体")
@TableName("scheduled_task")
public class ScheduledTask implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "任务 ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "任务名称")
    private String name;

    @Schema(description = "关联场景 ID")
    private Long sceneId;

    @Schema(description = "调度类型：0=每日,1=工作日,2=周末,3=自定义")
    private Integer scheduleType;

    @Schema(description = "CRON表达式")
    private String cronExpression;

    @Schema(description = "时间配置JSON")
    private String timeConfig;

    @Schema(description = "是否启用：0=禁用,1=启用")
    private Integer isEnabled;

    @Schema(description = "下次触发时间")
    private LocalDateTime nextFireTime;

    @Schema(description = "创建人")
    private Long createdBy;

    @Schema(description = "逻辑删除：0-未删除,1-已删除")
    @TableLogic
    private Integer deleted;

    @Schema(description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Long getSceneId() { return sceneId; }
    public void setSceneId(Long sceneId) { this.sceneId = sceneId; }
    public Integer getScheduleType() { return scheduleType; }
    public void setScheduleType(Integer scheduleType) { this.scheduleType = scheduleType; }
    public String getCronExpression() { return cronExpression; }
    public void setCronExpression(String cronExpression) { this.cronExpression = cronExpression; }
    public String getTimeConfig() { return timeConfig; }
    public void setTimeConfig(String timeConfig) { this.timeConfig = timeConfig; }
    public Integer getIsEnabled() { return isEnabled; }
    public void setIsEnabled(Integer isEnabled) { this.isEnabled = isEnabled; }
    public LocalDateTime getNextFireTime() { return nextFireTime; }
    public void setNextFireTime(LocalDateTime nextFireTime) { this.nextFireTime = nextFireTime; }
    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }
    public Integer getDeleted() { return deleted; }
    public void setDeleted(Integer deleted) { this.deleted = deleted; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
}