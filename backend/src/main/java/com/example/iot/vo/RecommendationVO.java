package com.example.iot.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/**
 * 推荐内容视图对象
 */
@Schema(description = "推荐内容视图对象")
public class RecommendationVO {

    @Schema(description = "推荐 ID", example = "1")
    private Long id;

    @Schema(description = "用户 ID", example = "1")
    private Long userId;

    @Schema(description = "内容 ID", example = "1")
    private Long contentId;

    @Schema(description = "内容标题", example = "流浪地球 2")
    private String contentTitle;

    @Schema(description = "内容类型", example = "MOVIE")
    private String contentType;

    @Schema(description = "内容封面", example = "https://example.com/cover.jpg")
    private String contentCover;

    @Schema(description = "推荐原因", example = "基于您的偏好")
    private String reason;

    @Schema(description = "推荐分数", example = "85")
    private Integer score;

    @Schema(description = "是否已点击", example = "0")
    private Integer isClicked;

    @Schema(description = "是否已收藏", example = "0")
    private Integer isLiked;

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
    public String getContentType() { return contentType; }
    public void setContentType(String contentType) { this.contentType = contentType; }
    public String getContentCover() { return contentCover; }
    public void setContentCover(String contentCover) { this.contentCover = contentCover; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }
    public Integer getIsClicked() { return isClicked; }
    public void setIsClicked(Integer isClicked) { this.isClicked = isClicked; }
    public Integer getIsLiked() { return isLiked; }
    public void setIsLiked(Integer isLiked) { this.isLiked = isLiked; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
}
