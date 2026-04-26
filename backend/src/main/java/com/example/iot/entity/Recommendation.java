package com.example.iot.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 推荐记录实体类
 */
@Schema(description = "推荐记录实体")
@TableName("recommendation")
public class Recommendation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "推荐 ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "用户 ID")
    private Long userId;

    @Schema(description = "内容 ID")
    private Long contentId;

    @Schema(description = "推荐原因", example = "基于您的偏好")
    private String reason;

    @Schema(description = "推荐分数", example = "85")
    private Integer score;

    @Schema(description = "是否已点击", example = "0")
    private Integer isClicked;

    @Schema(description = "是否已收藏", example = "0")
    private Integer isLiked;

    @Schema(description = "是否不喜欢", example = "0")
    private Integer isDisliked;

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
    public Long getContentId() { return contentId; }
    public void setContentId(Long contentId) { this.contentId = contentId; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }
    public Integer getIsClicked() { return isClicked; }
    public void setIsClicked(Integer isClicked) { this.isClicked = isClicked; }
    public Integer getIsLiked() { return isLiked; }
    public void setIsLiked(Integer isLiked) { this.isLiked = isLiked; }
    public Integer getIsDisliked() { return isDisliked; }
    public void setIsDisliked(Integer isDisliked) { this.isDisliked = isDisliked; }
    public Integer getDeleted() { return deleted; }
    public void setDeleted(Integer deleted) { this.deleted = deleted; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
}