package com.example.iot.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 设备更新请求 DTO
 */
@Schema(description = "设备更新请求")
public class DeviceUpdateDTO {

    @Schema(description = "设备名称", example = "新客厅电视")
    private String name;

    @Schema(description = "房间", example = "大客厅")
    private String room;

    @Schema(description = "设备类型", example = "TV")
    private String type;

    @Schema(description = "状态：true-启用，false-禁用", example = "true")
    private Boolean status;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getRoom() { return room; }
    public void setRoom(String room) { this.room = room; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Boolean getStatus() { return status; }
    public void setStatus(Boolean status) { this.status = status; }
}
