package com.example.iot.dto;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 执行记录查询DTO
 */
@Schema(description = "执行记录查询DTO")
public class ExecutionLogQueryDTO {

    @Parameter(description = "页码", example = "1")
    @Schema(description = "页码")
    private Integer page = 1;

    @Parameter(description = "每页大小", example = "20")
    @Schema(description = "每页大小")
    private Integer size = 20;

    @Parameter(description = "开始时间")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Schema(description = "开始时间")
    private LocalDateTime startTime;

    @Parameter(description = "结束时间")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Schema(description = "结束时间")
    private LocalDateTime endTime;

    public Integer getPage() { return page; }
    public void setPage(Integer page) { this.page = page; }
    public Integer getSize() { return size; }
    public void setSize(Integer size) { this.size = size; }
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
}
