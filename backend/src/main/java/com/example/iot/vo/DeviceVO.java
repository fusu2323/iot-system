package com.example.iot.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/**
 * 设备视图对象
 */
@Schema(description = "设备视图对象")
public class DeviceVO {

    @Schema(description = "设备 ID", example = "1")
    private Long id;

    @Schema(description = "设备名称", example = "客厅电视")
    private String name;

    @Schema(description = "设备类型", example = "TV")
    private String type;

    @Schema(description = "房间", example = "客厅")
    private String room;

    @Schema(description = "状态：0-禁用，1-启用", example = "1")
    private Integer status;

    @Schema(description = "在线状态：0-离线，1-在线", example = "1")
    private Integer isOnline;

    @Schema(description = "所属用户 ID", example = "1")
    private Long userId;

    @Schema(description = "创建时间", example = "2026-03-12 10:00:00")
    private LocalDateTime createTime;

    @Schema(description = "更新时间", example = "2026-03-12 12:00:00")
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
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
}