package com.example.iot.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 内容视图对象
 */
@Schema(description = "内容视图对象")
public class ContentVO {

    @Schema(description = "内容 ID", example = "1")
    private Long id;

    @Schema(description = "内容标题", example = "流浪地球 2")
    private String title;

    @Schema(description = "内容类型", example = "MOVIE")
    private String type;

    @Schema(description = "封面 URL", example = "https://example.com/cover.jpg")
    private String cover;

    @Schema(description = "内容描述", example = "科幻冒险电影")
    private String description;

    @Schema(description = "评分", example = "4.5")
    private BigDecimal rating;

    @Schema(description = "创建时间", example = "2026-03-12 10:00:00")
    private LocalDateTime createTime;

    @Schema(description = "更新时间", example = "2026-03-12 12:00:00")
    private LocalDateTime updateTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getCover() { return cover; }
    public void setCover(String cover) { this.cover = cover; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getRating() { return rating; }
    public void setRating(BigDecimal rating) { this.rating = rating; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
}