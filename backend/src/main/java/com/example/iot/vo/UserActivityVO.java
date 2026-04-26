package com.example.iot.vo;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 用户活跃度 VO
 */
@Schema(description = "用户活跃度统计")
public class UserActivityVO {

    @Schema(description = "日期", example = "2026-03-12")
    private String date;

    @Schema(description = "活跃用户数", example = "20")
    private Integer activeUsers;

    @Schema(description = "操作次数", example = "150")
    private Integer operationCount;

    @Schema(description = "新增用户数", example = "3")
    private Integer newUsers;

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public Integer getActiveUsers() { return activeUsers; }
    public void setActiveUsers(Integer activeUsers) { this.activeUsers = activeUsers; }
    public Integer getOperationCount() { return operationCount; }
    public void setOperationCount(Integer operationCount) { this.operationCount = operationCount; }
    public Integer getNewUsers() { return newUsers; }
    public void setNewUsers(Integer newUsers) { this.newUsers = newUsers; }
}
