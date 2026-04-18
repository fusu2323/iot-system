package com.example.iot.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 场景更新请求 DTO
 */
@Schema(description = "场景更新请求")
public class SceneUpdateDTO {

    @Schema(description = "场景名称", example = "新影院模式")
    private String name;

    @Schema(description = "场景描述", example = "全新的影院场景")
    private String description;

    @Schema(description = "场景图标", example = "cinema")
    private String icon;

    @Schema(description = "互斥组：同一组内只能有一个场景启用，NULL表示非互斥", example = "居家模式")
    private String mutexGroup;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
    public String getMutexGroup() { return mutexGroup; }
    public void setMutexGroup(String mutexGroup) { this.mutexGroup = mutexGroup; }
}
