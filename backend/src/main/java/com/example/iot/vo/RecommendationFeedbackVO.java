package com.example.iot.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/**
 * 推荐反馈视图对象
 */
@Schema(description = "推荐反馈视图对象")
public class RecommendationFeedbackVO {

    @Schema(description = "反馈 ID", example = "1")
    private Long id;

    @Schema(description = "用户 ID", example = "1")
    private Long userId;

    @Schema(description = "内容 ID", example = "1")
    private Long contentId;

    @Schema(description = "内容标题", example = "流浪地球 2")
    private String contentTitle;

    @Schema(description = "反馈类型", example = "LIKE")
    private String feedbackType;

    @Schema(description = "反馈分数", example = "5")
    private Integer score;

    @Schema(description = "创建时间", example = "2026-03-12 10:00:00")
    private LocalDateTime createTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getContentId() { return contentId; }
    public void setContentId(Long contentId) { this.contentId = contentId; }
    public String getContentTitle() { return contentTitle; }
    public void setContentTitle(String contentTitle) { this.contentTitle = contentTitle; }
    public String getFeedbackType() { return feedbackType; }
    public void setFeedbackType(String feedbackType) { this.feedbackType = feedbackType; }
    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
}
