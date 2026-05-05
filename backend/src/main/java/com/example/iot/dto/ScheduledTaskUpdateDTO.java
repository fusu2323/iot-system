package com.example.iot.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 更新定时任务DTO
 */
@Schema(description = "更新定时任务请求")
public class ScheduledTaskUpdateDTO {

    @Schema(description = "任务名称")
    private String name;

    @Schema(description = "关联场景ID")
    private Long sceneId;

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