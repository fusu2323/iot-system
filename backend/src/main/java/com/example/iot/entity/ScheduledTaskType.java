package com.example.iot.entity;

/**
 * 定时任务调度类型枚举
 */
public enum ScheduledTaskType {
    DAILY(0, "每日"),
    WORKDAY(1, "工作日"),
    WEEKEND(2, "周末"),
    CUSTOM(3, "自定义");

    private final int code;
    private final String description;

    ScheduledTaskType(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() { return code; }
    public String getDescription() { return description; }

    public static ScheduledTaskType fromCode(int code) {
        for (ScheduledTaskType type : values()) {
            if (type.code == code) return type;
        }
        throw new IllegalArgumentException("Invalid schedule type: " + code);
    }
}