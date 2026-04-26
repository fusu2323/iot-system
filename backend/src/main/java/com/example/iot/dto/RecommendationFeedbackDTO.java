package com.example.iot.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 推荐反馈请求 DTO
 */
@Schema(description = "推荐反馈请求")
public class RecommendationFeedbackDTO {

    @Schema(description = "用户 ID", example = "1", requiredMode = io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED)
    @NotNull(message = "用户 ID 不能为空")
    private Long userId;

    @Schema(description = "内容 ID", example = "1", requiredMode = io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED)
    @NotNull(message = "内容 ID 不能为空")
    private Long contentId;

    @Schema(description = "反馈类型", example = "LIKE", requiredMode = io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "反馈类型不能为空")
    private String feedbackType;

    @Schema(description = "反馈分数", example = "5")
    private Integer score;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getContentId() { return contentId; }
    public void setContentId(Long contentId) { this.contentId = contentId; }
    public String getFeedbackType() { return feedbackType; }
    public void setFeedbackType(String feedbackType) { this.feedbackType = feedbackType; }
    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }
}
