package com.example.iot.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 设备创建请求 DTO
 */
@Schema(description = "设备创建请求")
public class DeviceCreateDTO {

    @Schema(description = "设备名称", example = "客厅电视", requiredMode = io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "设备名称不能为空")
    private String name;

    @Schema(description = "设备类型", example = "TV", requiredMode = io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "设备类型不能为空")
    private String type;

    @Schema(description = "房间", example = "客厅")
    private String room;

    @Schema(description = "状态：true-启用，false-禁用", example = "true", defaultValue = "true")
    @NotNull(message = "状态不能为空")
    private Boolean status;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getRoom() { return room; }
    public void setRoom(String room) { this.room = room; }
    public Boolean getStatus() { return status; }
    public void setStatus(Boolean status) { this.status = status; }
}
