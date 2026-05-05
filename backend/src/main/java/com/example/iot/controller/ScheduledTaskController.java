package com.example.iot.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.iot.common.result.Result;
import com.example.iot.dto.ScheduledTaskCreateDTO;
import com.example.iot.dto.ScheduledTaskUpdateDTO;
import com.example.iot.entity.vo.ScheduledTaskVO;
import com.example.iot.service.IScheduledTaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 定时任务管理控制器
 */
@RestController
@RequestMapping("/api/scheduled-tasks")
@Tag(name = "定时任务管理", description = "定时任务 CRUD 接口")
@PreAuthorize("hasRole('ADMIN')")
public class ScheduledTaskController {

    private final IScheduledTaskService scheduledTaskService;

    public ScheduledTaskController(IScheduledTaskService scheduledTaskService) {
        this.scheduledTaskService = scheduledTaskService;
    }

    /**
     * 查询定时任务列表（分页）
     */
    @GetMapping
    @Operation(summary = "查询定时任务列表")
    public Result<IPage<ScheduledTaskVO>> list(
        @Parameter(description = "页码", example = "1")
        @RequestParam(defaultValue = "1") Integer page,
        @Parameter(description = "每页大小", example = "10")
        @RequestParam(defaultValue = "10") Integer size,
        @Parameter(description = "任务名称", example = "早起")
        @RequestParam(required = false) String name
    ) {
        IPage<ScheduledTaskVO> tasks = scheduledTaskService.listPage(page, size, name);
        return Result.success(tasks);
    }

    /**
     * 查询定时任务详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "查询定时任务详情")
    public Result<ScheduledTaskVO> getById(
        @Parameter(description = "任务 ID", example = "1")
        @PathVariable Long id
    ) {
        ScheduledTaskVO task = scheduledTaskService.getById(id);
        return Result.success(task);
    }

    /**
     * 创建定时任务
     */
    @PostMapping
    @Operation(summary = "创建定时任务")
    public Result<Long> create(
        @RequestBody @Valid @Validated ScheduledTaskCreateDTO dto,
        @Parameter(description = "创建人用户ID", example = "1")
        @RequestParam Long userId
    ) {
        Long taskId = scheduledTaskService.create(dto, userId);
        return Result.success("创建成功", taskId);
    }

    /**
     * 更新定时任务
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新定时任务")
    public Result<ScheduledTaskVO> update(
        @Parameter(description = "任务 ID", example = "1")
        @PathVariable Long id,
        @RequestBody @Valid @Validated ScheduledTaskUpdateDTO dto
    ) {
        ScheduledTaskVO task = scheduledTaskService.update(id, dto);
        return Result.success("更新成功", task);
    }

    /**
     * 删除定时任务
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除定时任务")
    public Result<Void> delete(
        @Parameter(description = "任务 ID", example = "1")
        @PathVariable Long id
    ) {
        scheduledTaskService.delete(id);
        return Result.success("删除成功", null);
    }

    /**
     * 启用/禁用定时任务
     */
    @PostMapping("/{id}/toggle")
    @Operation(summary = "启用/禁用定时任务")
    public Result<ScheduledTaskVO> toggle(
        @Parameter(description = "任务 ID", example = "1")
        @PathVariable Long id
    ) {
        ScheduledTaskVO task = scheduledTaskService.toggle(id);
        return Result.success("操作成功", task);
    }
}