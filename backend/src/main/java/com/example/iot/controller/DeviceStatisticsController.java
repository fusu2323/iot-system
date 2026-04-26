package com.example.iot.controller;

import com.example.iot.common.result.Result;
import com.example.iot.service.DeviceStatisticsService;
import com.example.iot.vo.DeviceStatisticsVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 设备统计控制器
 */
@RestController
@RequestMapping("/api/device-statistics")
@Tag(name = "数据统计", description = "dashboard 统计接口")
public class DeviceStatisticsController {

    private final DeviceStatisticsService deviceStatisticsService;

    public DeviceStatisticsController(DeviceStatisticsService deviceStatisticsService) {
        this.deviceStatisticsService = deviceStatisticsService;
    }

    /**
     * 获取设备统计概览
     */
    @GetMapping("/overview")
    @Operation(summary = "获取设备统计概览")
    public Result<Map<String, Integer>> getOverview() {
        Map<String, Integer> overview = deviceStatisticsService.getOverview();
        return Result.success(overview);
    }

    /**
     * 按类型统计设备数量
     */
    @GetMapping("/types")
    @Operation(summary = "按类型统计设备数量")
    public Result<List<DeviceStatisticsVO>> countByType() {
        List<DeviceStatisticsVO> statistics = deviceStatisticsService.countByType();
        return Result.success(statistics);
    }

    /**
     * 按房间统计设备数量
     */
    @GetMapping("/rooms")
    @Operation(summary = "按房间统计设备数量")
    public Result<List<DeviceStatisticsVO>> countByRoom() {
        List<DeviceStatisticsVO> statistics = deviceStatisticsService.countByRoom();
        return Result.success(statistics);
    }

    /**
     * 按状态统计设备数量
     */
    @GetMapping("/status")
    @Operation(summary = "按状态统计设备数量")
    public Result<List<DeviceStatisticsVO>> countByStatus() {
        List<DeviceStatisticsVO> statistics = deviceStatisticsService.countByStatus();
        return Result.success(statistics);
    }
}
