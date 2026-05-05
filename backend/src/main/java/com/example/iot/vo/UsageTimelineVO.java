package com.example.iot.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 使用时间线 VO
 */
@Schema(description = "使用时间线响应")
public class UsageTimelineVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "目标类型：DEVICE/SCENE")
    private String targetType;

    @Schema(description = "目标 ID")
    private Long targetId;

    @Schema(description = "目标名称")
    private String targetName;

    @Schema(description = "时间线记录")
    private List<TimelineItem> timeline;

    @Schema(description = "时间线条目")
    public static class TimelineItem implements Serializable {
        private static final long serialVersionUID = 1L;

        @Schema(description = "发生时间")
        private LocalDateTime activateTime;

        @Schema(description = "动作：ACTIVATE/TRIGGER")
        private String action;

        public TimelineItem() {}
        public TimelineItem(LocalDateTime activateTime, String action) {
            this.activateTime = activateTime;
            this.action = action;
        }

        public LocalDateTime getActivateTime() { return activateTime; }
        public void setActivateTime(LocalDateTime activateTime) { this.activateTime = activateTime; }
        public String getAction() { return action; }
        public void setAction(String action) { this.action = action; }
    }

    public String getTargetType() { return targetType; }
    public void setTargetType(String targetType) { this.targetType = targetType; }
    public Long getTargetId() { return targetId; }
    public void setTargetId(Long targetId) { this.targetId = targetId; }
    public String getTargetName() { return targetName; }
    public void setTargetName(String targetName) { this.targetName = targetName; }
    public List<TimelineItem> getTimeline() { return timeline; }
    public void setTimeline(List<TimelineItem> timeline) { this.timeline = timeline; }
}
