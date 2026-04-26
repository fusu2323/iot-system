package com.example.iot.vo;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 设备趋势 VO
 */
@Schema(description = "设备趋势统计")
public class DeviceTrendVO {

    @Schema(description = "日期", example = "2026-03-12")
    private String date;

    @Schema(description = "设备数量", example = "50")
    private Integer deviceCount;

    @Schema(description = "在线设备数", example = "35")
    private Integer onlineCount;

    @Schema(description = "新增设备数", example = "5")
    private Integer newCount;

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public Integer getDeviceCount() { return deviceCount; }
    public void setDeviceCount(Integer deviceCount) { this.deviceCount = deviceCount; }
    public Integer getOnlineCount() { return onlineCount; }
    public void setOnlineCount(Integer onlineCount) { this.onlineCount = onlineCount; }
    public Integer getNewCount() { return newCount; }
    public void setNewCount(Integer newCount) { this.newCount = newCount; }
}
