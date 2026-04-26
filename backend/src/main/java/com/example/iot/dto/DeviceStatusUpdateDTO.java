package com.example.iot.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * 设备状态更新请求 DTO
 */
@Schema(description = "设备状态更新请求")
public class DeviceStatusUpdateDTO {

    @Schema(description = "状态：0-禁用，1-启用", example = "1")
    @NotNull(message = "状态不能为空")
    private Integer status;

    @Schema(description = "在线状态：0-离线，1-在线", example = "1")
    private Integer isOnline;

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public Integer getIsOnline() { return isOnline; }
    public void setIsOnline(Integer isOnline) { this.isOnline = isOnline; }
}
