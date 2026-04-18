package com.example.iot.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 场景实体类
 */
@Schema(description = "场景实体")
@TableName("scene")
public class Scene implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "场景 ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "场景名称")
    private String name;

    @Schema(description = "场景描述")
    private String description;

    @Schema(description = "场景图标")
    private String icon;

    @Schema(description = "是否启用：0-禁用，1-启用")
    private Integer isEnabled;

    @Schema(description = "所属用户 ID")
    private Long userId;

    @Schema(description = "互斥组：同一组内只能有一个场景启用，NULL表示非互斥")
    private String mutexGroup;

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
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
    public Integer getIsEnabled() { return isEnabled; }
    public void setIsEnabled(Integer isEnabled) { this.isEnabled = isEnabled; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getMutexGroup() { return mutexGroup; }
    public void setMutexGroup(String mutexGroup) { this.mutexGroup = mutexGroup; }
    public Integer getDeleted() { return deleted; }
    public void setDeleted(Integer deleted) { this.deleted = deleted; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
}