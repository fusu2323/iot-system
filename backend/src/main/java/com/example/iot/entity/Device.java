package com.example.iot.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 设备实体类
 */
@Schema(description = "设备实体")
@TableName("device")
public class Device implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "设备 ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "设备名称")
    private String name;

    @Schema(description = "设备类型：TV/SPEAKER/LIGHT 等")
    private String type;

    @Schema(description = "房间")
    private String room;

    @Schema(description = "状态：0-禁用，1-启用")
    private Integer status;

    @Schema(description = "在线状态：0-离线，1-在线")
    private Integer isOnline;

    @Schema(description = "所属用户 ID")
    private Long userId;

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
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getRoom() { return room; }
    public void setRoom(String room) { this.room = room; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public Integer getIsOnline() { return isOnline; }
    public void setIsOnline(Integer isOnline) { this.isOnline = isOnline; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Integer getDeleted() { return deleted; }
    public void setDeleted(Integer deleted) { this.deleted = deleted; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
}