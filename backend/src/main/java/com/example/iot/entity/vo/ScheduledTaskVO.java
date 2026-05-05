package com.example.iot.entity.vo;

import com.example.iot.entity.ScheduledTask;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/**
 * 定时任务VO
 */
@Schema(description = "定时任务视图对象")
public class ScheduledTaskVO extends ScheduledTask {

    @Schema(description = "关联场景名称")
    private String sceneName;

    @Schema(description = "调度类型描述")
    private String scheduleTypeDesc;

    public ScheduledTaskVO() {}

    public String getSceneName() { return sceneName; }
    public void setSceneName(String sceneName) { this.sceneName = sceneName; }
    public String getScheduleTypeDesc() { return scheduleTypeDesc; }
    public void setScheduleTypeDesc(String scheduleTypeDesc) { this.scheduleTypeDesc = scheduleTypeDesc; }
}