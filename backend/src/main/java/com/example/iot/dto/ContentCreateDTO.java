package com.example.iot.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;

/**
 * 内容创建请求 DTO
 */
@Schema(description = "内容创建请求")
public class ContentCreateDTO {

    @Schema(description = "内容标题", example = "流浪地球 2", requiredMode = io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "内容标题不能为空")
    private String title;

    @Schema(description = "内容类型", example = "MOVIE", requiredMode = io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "内容类型不能为空")
    private String type;

    @Schema(description = "内容分类/流派", example = "科幻")
    private String genre;

    @Schema(description = "封面 URL", example = "https://example.com/cover.jpg")
    private String cover;

    @Schema(description = "内容描述", example = "科幻冒险电影")
    private String description;

    @Schema(description = "评分", example = "4.5")
    private BigDecimal rating;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }
    public String getCover() { return cover; }
    public void setCover(String cover) { this.cover = cover; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getRating() { return rating; }
    public void setRating(BigDecimal rating) { this.rating = rating; }
}
