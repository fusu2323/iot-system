package com.example.iot.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户偏好实体类
 */
@Schema(description = "用户偏好实体")
@TableName("user_preference")
public class UserPreference implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "偏好 ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "用户 ID")
    private Long userId;

    @Schema(description = "内容类型")
    private String contentType;

    @Schema(description = "偏好分数")
    private Integer preferenceScore;

    @Schema(description = "点击行为权重")
    private Integer clickWeight;

    @Schema(description = "收藏行为权重")
    private Integer likeWeight;

    @Schema(description = "不喜欢行为权重")
    private Integer dislikeWeight;

    @Schema(description = "逻辑删除：0-未删除，1-已删除")
    @TableLogic
    private Integer deleted;

    @Schema(description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getContentType() { return contentType; }
    public void setContentType(String contentType) { this.contentType = contentType; }
    public Integer getPreferenceScore() { return preferenceScore; }
    public void setPreferenceScore(Integer preferenceScore) { this.preferenceScore = preferenceScore; }
    public Integer getClickWeight() { return clickWeight; }
    public void setClickWeight(Integer clickWeight) { this.clickWeight = clickWeight; }
    public Integer getLikeWeight() { return likeWeight; }
    public void setLikeWeight(Integer likeWeight) { this.likeWeight = likeWeight; }
    public Integer getDislikeWeight() { return dislikeWeight; }
    public void setDislikeWeight(Integer dislikeWeight) { this.dislikeWeight = dislikeWeight; }
    public Integer getDeleted() { return deleted; }
    public void setDeleted(Integer deleted) { this.deleted = deleted; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
}