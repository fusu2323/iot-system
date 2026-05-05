package com.example.iot.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 创建定时任务DTO
 */
@Schema(description = "创建定时任务请求")
public class ScheduledTaskCreateDTO {

    @NotBlank(message = "任务名称不能为空")
    @Schema(description = "任务名称")
    private String name;

    @NotNull(message = "关联场景ID不能为空")
    @Schema(description = "关联场景ID")
    private Long sceneId;

    @NotNull(message = "调度类型不能为空")
    @Schema(description = "调度类型：0=每日,1=工作日,2=周末,3=自定义")
    private Integer scheduleType;

    @Schema(description = "时间配置JSON")
    private String timeConfig;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Long getSceneId() { return sceneId; }
    public void setSceneId(Long sceneId) { this.sceneId = sceneId; }
    public Integer getScheduleType() { return scheduleType; }
    public void setScheduleType(Integer scheduleType) { this.scheduleType = scheduleType; }
    public String getTimeConfig() { return timeConfig; }
    public void setTimeConfig(String timeConfig) { this.timeConfig = timeConfig; }
}