package com.example.iot.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 内容实体类
 */
@Schema(description = "内容实体")
@TableName("content")
public class Content implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "内容 ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "内容标题")
    private String title;

    @Schema(description = "内容类型：MOVIE/MUSIC/GAME")
    private String type;

    @Schema(description = "内容分类/流派")
    private String genre;

    @Schema(description = "封面 URL")
    private String cover;

    @Schema(description = "内容描述")
    private String description;

    @Schema(description = "评分：0.0-5.0")
    private java.math.BigDecimal rating;

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
    public java.math.BigDecimal getRating() { return rating; }
    public void setRating(java.math.BigDecimal rating) { this.rating = rating; }
    public Integer getDeleted() { return deleted; }
    public void setDeleted(Integer deleted) { this.deleted = deleted; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
}