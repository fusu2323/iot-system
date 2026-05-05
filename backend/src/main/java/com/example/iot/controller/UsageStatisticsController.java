package com.example.iot.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.iot.common.result.Result;
import com.example.iot.security.SecurityContextUtil;
import com.example.iot.service.StatLogService;
import com.example.iot.vo.UsageRankingVO;
import com.example.iot.vo.UsageStatsVO;
import com.example.iot.vo.UsageTimelineVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 使用统计控制器
 */
@RestController
@RequestMapping("/api/usage")
@Tag(name = "使用统计", description = "设备/场景使用统计查询接口")
public class UsageStatisticsController {

    private final StatLogService statLogService;

    public UsageStatisticsController(StatLogService statLogService) {
        this.statLogService = statLogService;
    }

    /**
     * STATS-03: 普通用户查询自己设备和场景的使用统计（按日/周/月维度）
     */
    @GetMapping("/stats/my")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "查询我的使用统计")
    public Result<UsageStatsVO> getMyStatistics(
            @Parameter(description = "统计维度：DAY/WEEK/MONTH", example = "DAY")
            @RequestParam(defaultValue = "DAY") String dimension,
            @Parameter(description = "目标类型：DEVICE/SCENE（可选）")
            @RequestParam(required = false) String targetType,
            @Parameter(description = "开始日期 YYYY-MM-DD")
            @RequestParam(required = false) String startDate,
            @Parameter(description = "结束日期 YYYY-MM-DD")
            @RequestParam(required = false) String endDate
    ) {
        Long userId = SecurityContextUtil.getCurrentUserId();
        UsageStatsVO stats = statLogService.getMyStatistics(
                userId, dimension, targetType, startDate, endDate);
        return Result.success(stats);
    }

    /**
     * STATS-04: 管理员查询全局设备和场景使用次数排行（前10名）
     */
    @GetMapping("/stats/ranking")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "查询全局使用排行（仅管理员）")
    public Result<UsageRankingVO> getGlobalRanking(
            @Parameter(description = "目标类型：DEVICE/SCENE（可选）")
            @RequestParam(required = false) String targetType,
            @Parameter(description = "返回条数", example = "10")
            @RequestParam(defaultValue = "10") Integer limit
    ) {
        UsageRankingVO ranking = statLogService.getGlobalRanking(targetType, limit);
        return Result.success(ranking);
    }

    /**
     * STATS-05: 用户查看单条设备或场景的历史激活时间线
     */
    @GetMapping("/timeline/{targetType}/{targetId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "查询单条设备/场景的历史时间线")
    public Result<UsageTimelineVO> getTimeline(
            @Parameter(description = "目标类型：DEVICE/SCENE", example = "DEVICE")
            @PathVariable String targetType,
            @Parameter(description = "目标 ID", example = "1")
            @PathVariable Long targetId,
            @Parameter(description = "页码", example = "1")
            @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页条数", example = "10")
            @RequestParam(defaultValue = "10") Integer size
    ) {
        Long userId = SecurityContextUtil.getCurrentUserId();
        UsageTimelineVO timeline = statLogService.getTimeline(userId, targetType, targetId, page, size);
        return Result.success(timeline);
    }
}
