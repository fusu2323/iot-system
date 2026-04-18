package com.example.iot.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.iot.common.result.Result;
import com.example.iot.service.LogService;
import com.example.iot.vo.OperationLogVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 操作日志控制器
 */
@RestController
@RequestMapping("/api/logs")
@Tag(name = "日志管理", description = "操作日志查询接口")
public class LogController {

    private final LogService logService;

    public LogController(LogService logService) {
        this.logService = logService;
    }

    /**
     * 分页查询操作日志
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "查询操作日志列表")
    public Result<IPage<OperationLogVO>> list(
        @Parameter(description = "页码", example = "1")
        @RequestParam(defaultValue = "1") Integer page,
        @Parameter(description = "每页大小", example = "10")
        @RequestParam(defaultValue = "10") Integer size,
        @Parameter(description = "用户 ID", example = "1")
        @RequestParam(required = false) Long userId,
        @Parameter(description = "操作类型", example = "LOGIN")
        @RequestParam(required = false) String operation,
        @Parameter(description = "目标类型", example = "DEVICE")
        @RequestParam(required = false) String targetType,
        @Parameter(description = "开始日期", example = "2026-03-01")
        @RequestParam(required = false) String startDate,
        @Parameter(description = "结束日期", example = "2026-03-12")
        @RequestParam(required = false) String endDate
    ) {
        IPage<OperationLogVO> logs = logService.list(page, size, userId, operation, targetType, startDate, endDate);
        return Result.success(logs);
    }

    /**
     * 根据 ID 查询操作日志
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "根据 ID 查询操作日志")
    public Result<OperationLogVO> getLogById(
        @Parameter(description = "日志 ID", example = "1")
        @PathVariable Long id
    ) {
        OperationLogVO log = logService.getById(id);
        if (log == null) {
            return Result.error("日志不存在");
        }
        return Result.success(log);
    }

    /**
     * 统计各操作类型的数量
     */
    @GetMapping("/stats/operation")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "统计各操作类型的数量")
    public Result<List<Map<String, Object>>> countByOperation() {
        return Result.success(logService.countByOperation());
    }

    /**
     * 统计每日日志数量
     */
    @GetMapping("/stats/daily")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "统计每日日志数量")
    public Result<List<Map<String, Object>>> countByDay(
        @Parameter(description = "天数", example = "7")
        @RequestParam(defaultValue = "7") Integer days
    ) {
        return Result.success(logService.countByDay(days));
    }
}
