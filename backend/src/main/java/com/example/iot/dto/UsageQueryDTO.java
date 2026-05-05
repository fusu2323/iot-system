package com.example.iot.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;

/**
 * 使用统计查询 DTO
 */
@Schema(description = "使用统计查询参数")
public class UsageQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "统计维度：DAY/WEEK/MONTH", example = "DAY")
    private String dimension = "DAY";

    @Schema(description = "目标类型：DEVICE/SCENE（可选，空表示全部）")
    private String targetType;

    @Schema(description = "开始日期（YYYY-MM-DD）")
    private String startDate;

    @Schema(description = "结束日期（YYYY-MM-DD）")
    private String endDate;

    @Schema(description = "每页条数")
    private Integer size = 10;

    public String getDimension() { return dimension; }
    public void setDimension(String dimension) { this.dimension = dimension; }
    public String getTargetType() { return targetType; }
    public void setTargetType(String targetType) { this.targetType = targetType; }
    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }
    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }
    public Integer getSize() { return size; }
    public void setSize(Integer size) { this.size = size; }
}
