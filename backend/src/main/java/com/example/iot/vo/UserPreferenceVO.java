package com.example.iot.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/**
 * 用户偏好视图对象
 */
@Schema(description = "用户偏好视图对象")
public class UserPreferenceVO {

    @Schema(description = "偏好 ID", example = "1")
    private Long id;

    @Schema(description = "用户 ID", example = "1")
    private Long userId;

    @Schema(description = "内容类型", example = "MOVIE")
    private String contentType;

    @Schema(description = "偏好分数", example = "80")
    private Integer preferenceScore;

    @Schema(description = "点击行为权重", example = "5")
    private Integer clickWeight;

    @Schema(description = "收藏行为权重", example = "10")
    private Integer likeWeight;

    @Schema(description = "不喜欢行为权重", example = "-20")
    private Integer dislikeWeight;

    @Schema(description = "创建时间", example = "2026-03-12 10:00:00")
    private LocalDateTime createTime;

    @Schema(description = "更新时间", example = "2026-03-12 12:00:00")
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
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
}