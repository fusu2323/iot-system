package com.example.iot.controller;

import com.example.iot.common.result.Result;
import com.example.iot.service.DashboardService;
import com.example.iot.vo.DashboardStatsVO;
import com.example.iot.vo.DeviceTrendVO;
import com.example.iot.vo.UserActivityVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 数据统计仪表盘控制器
 */
@RestController
@RequestMapping("/api/dashboard")
@Tag(name = "数据统计", description = "Dashboard 统计接口")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    /**
     * 获取概览统计数据
     */
    @GetMapping("/overview")
    @Operation(summary = "获取概览统计数据")
    public Result<DashboardStatsVO> getOverview() {
        return Result.success(dashboardService.getOverview());
    }

    /**
     * 获取设备趋势数据
     */
    @GetMapping("/device-trend")
    @Operation(summary = "获取设备趋势数据")
    public Result<List<DeviceTrendVO>> getDeviceTrend(
        @Parameter(description = "天数", example = "7")
        @RequestParam(defaultValue = "7") Integer days
    ) {
        return Result.success(dashboardService.getDeviceTrend(days));
    }

    /**
     * 获取用户活跃度数据
     */
    @GetMapping("/user-activity")
    @Operation(summary = "获取用户活跃度数据")
    public Result<List<UserActivityVO>> getUserActivity(
        @Parameter(description = "天数", example = "7")
        @RequestParam(defaultValue = "7") Integer days
    ) {
        return Result.success(dashboardService.getUserActivity(days));
    }

    /**
     * 获取场景使用统计
     */
    @GetMapping("/scene-usage")
    @Operation(summary = "获取场景使用统计")
    public Result<List<Map<String, Object>>> getSceneUsage() {
        return Result.success(dashboardService.getSceneUsage());
    }

    /**
     * 获取内容分布统计
     */
    @GetMapping("/content-distribution")
    @Operation(summary = "获取内容分布统计")
    public Result<List<Map<String, Object>>> getContentDistribution() {
        return Result.success(dashboardService.getContentDistribution());
    }

    /**
     * 获取系统信息
     */
    @GetMapping("/system-info")
    @Operation(summary = "获取系统信息")
    public Result<Map<String, String>> getSystemInfo() {
        return Result.success(dashboardService.getSystemInfo());
    }
}
