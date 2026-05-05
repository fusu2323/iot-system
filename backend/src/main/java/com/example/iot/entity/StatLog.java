package com.example.iot.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 使用统计日志实体类
 */
@Schema(description = "使用统计日志实体")
@TableName("stat_log")
public class StatLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "日志 ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "用户 ID")
    @TableField("user_id")
    private Long userId;

    @Schema(description = "目标类型：DEVICE/SCENE")
    @TableField("target_type")
    private String targetType;

    @Schema(description = "目标 ID")
    @TableField("target_id")
    private Long targetId;

    @Schema(description = "动作：ACTIVATE/TRIGGER")
    private String action;

    @Schema(description = "统计日期")
    @TableField("stat_date")
    private LocalDate statDate;

    @Schema(description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getTargetType() { return targetType; }
    public void setTargetType(String targetType) { this.targetType = targetType; }
    public Long getTargetId() { return targetId; }
    public void setTargetId(Long targetId) { this.targetId = targetId; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public LocalDate getStatDate() { return statDate; }
    public void setStatDate(LocalDate statDate) { this.statDate = statDate; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
}
