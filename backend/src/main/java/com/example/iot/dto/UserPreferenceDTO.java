package com.example.iot.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 用户偏好设置 DTO
 */
@Schema(description = "用户偏好设置请求")
public class UserPreferenceDTO {

    @Schema(description = "内容类型", example = "MOVIE", requiredMode = io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "内容类型不能为空")
    private String contentType;

    @Schema(description = "偏好分数", example = "80", requiredMode = io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED)
    @NotNull(message = "偏好分数不能为空")
    private Integer preferenceScore;

    @Schema(description = "点击行为权重", example = "5")
    private Integer clickWeight;

    @Schema(description = "收藏行为权重", example = "10")
    private Integer likeWeight;

    @Schema(description = "不喜欢行为权重", example = "-20")
    private Integer dislikeWeight;

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
}
