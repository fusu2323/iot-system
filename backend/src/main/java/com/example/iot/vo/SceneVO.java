package com.example.iot.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 场景视图对象
 */
@Schema(description = "场景视图对象")
public class SceneVO {

    @Schema(description = "场景 ID", example = "1")
    private Long id;

    @Schema(description = "场景名称", example = "影院模式")
    private String name;

    @Schema(description = "场景描述", example = "调暗灯光，打开音响和电视")
    private String description;

    @Schema(description = "场景图标", example = "movie")
    private String icon;

    @Schema(description = "是否启用", example = "1")
    private Integer isEnabled;

    @Schema(description = "所属用户 ID", example = "1")
    private Long userId;

    @Schema(description = "互斥组", example = "居家模式")
    private String mutexGroup;

    @Schema(description = "创建时间", example = "2026-03-12 10:00:00")
    private LocalDateTime createTime;

    @Schema(description = "更新时间", example = "2026-03-12 12:00:00")
    private LocalDateTime updateTime;

    @Schema(description = "关联设备列表")
    private List<SceneDeviceVO> devices;

    @Schema(description = "当前同互斥组中已启用的场景ID，NULL表示无其他启用场景或当前场景无互斥组")
    private Long activeGroupSceneId;

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
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
    public List<SceneDeviceVO> getDevices() { return devices; }
    public void setDevices(List<SceneDeviceVO> devices) { this.devices = devices; }
    public Long getActiveGroupSceneId() { return activeGroupSceneId; }
    public void setActiveGroupSceneId(Long activeGroupSceneId) { this.activeGroupSceneId = activeGroupSceneId; }
}
