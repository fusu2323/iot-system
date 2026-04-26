package com.example.iot.vo;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Dashboard 统计概览 VO
 */
@Schema(description = "Dashboard 统计概览")
public class DashboardStatsVO {

    @Schema(description = "设备总数", example = "50")
    private Integer totalDevices;

    @Schema(description = "在线设备数", example = "35")
    private Integer onlineDevices;

    @Schema(description = "离线设备数", example = "15")
    private Integer offlineDevices;

    @Schema(description = "场景总数", example = "10")
    private Integer totalScenes;

    @Schema(description = "启用场景数", example = "8")
    private Integer enabledScenes;

    @Schema(description = "内容总数", example = "100")
    private Integer totalContents;

    @Schema(description = "用户总数", example = "25")
    private Integer totalUsers;

    @Schema(description = "今日操作日志数", example = "150")
    private Integer todayLogs;

    @Schema(description = "设备在线率", example = "70.0")
    private Double onlineRate;

    public Integer getTotalDevices() { return totalDevices; }
    public void setTotalDevices(Integer totalDevices) { this.totalDevices = totalDevices; }
    public Integer getOnlineDevices() { return onlineDevices; }
    public void setOnlineDevices(Integer onlineDevices) { this.onlineDevices = onlineDevices; }
    public Integer getOfflineDevices() { return offlineDevices; }
    public void setOfflineDevices(Integer offlineDevices) { this.offlineDevices = offlineDevices; }
    public Integer getTotalScenes() { return totalScenes; }
    public void setTotalScenes(Integer totalScenes) { this.totalScenes = totalScenes; }
    public Integer getEnabledScenes() { return enabledScenes; }
    public void setEnabledScenes(Integer enabledScenes) { this.enabledScenes = enabledScenes; }
    public Integer getTotalContents() { return totalContents; }
    public void setTotalContents(Integer totalContents) { this.totalContents = totalContents; }
    public Integer getTotalUsers() { return totalUsers; }
    public void setTotalUsers(Integer totalUsers) { this.totalUsers = totalUsers; }
    public Integer getTodayLogs() { return todayLogs; }
    public void setTodayLogs(Integer todayLogs) { this.todayLogs = todayLogs; }
    public Double getOnlineRate() { return onlineRate; }
    public void setOnlineRate(Double onlineRate) { this.onlineRate = onlineRate; }
}
