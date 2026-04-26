package com.example.iot.vo;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 设备统计 VO
 */
@Schema(description = "设备统计 VO")
public class DeviceStatisticsVO {

    @Schema(description = "类型/房间名称", example = "TV")
    private String name;

    @Schema(description = "数量", example = "5")
    private Integer count;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Integer getCount() { return count; }
    public void setCount(Integer count) { this.count = count; }
}
