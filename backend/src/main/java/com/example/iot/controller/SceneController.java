package com.example.iot.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.iot.common.result.Result;
import com.example.iot.dto.SceneCreateDTO;
import com.example.iot.dto.SceneUpdateDTO;
import com.example.iot.service.SceneService;
import com.example.iot.vo.SceneVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 场景管理控制器
 */
@RestController
@RequestMapping("/api/scenes")
@Tag(name = "场景管理", description = "场景配置 CRUD 接口")
public class SceneController {

    private final SceneService sceneService;

    public SceneController(SceneService sceneService) {
        this.sceneService = sceneService;
    }

    /**
     * 查询场景列表
     */
    @GetMapping
    @Operation(summary = "查询场景列表")
    public Result<IPage<SceneVO>> list(
        @Parameter(description = "页码", example = "1")
        @RequestParam(defaultValue = "1") Integer page,
        @Parameter(description = "每页大小", example = "10")
        @RequestParam(defaultValue = "10") Integer size,
        @Parameter(description = "用户 ID", example = "1")
        @RequestParam(required = false) Long userId
    ) {
        IPage<SceneVO> scenes = sceneService.list(page, size, userId);
        return Result.success(scenes);
    }

    /**
     * 查询场景详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "查询场景详情")
    public Result<SceneVO> getSceneById(
        @Parameter(description = "场景 ID", example = "1")
        @PathVariable Long id
    ) {
        SceneVO scene = sceneService.getById(id);
        return Result.success(scene);
    }

    /**
     * 创建场景
     */
    @PostMapping
    @Operation(summary = "创建场景")
    public Result<Long> create(
        @RequestBody @Valid @Validated SceneCreateDTO dto,
        @Parameter(description = "用户 ID", example = "1")
        @RequestParam Long userId
    ) {
        Long sceneId = sceneService.create(dto, userId);
        return Result.success("创建成功", sceneId);
    }

    /**
     * 更新场景信息
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新场景信息")
    public Result<SceneVO> update(
        @Parameter(description = "场景 ID", example = "1")
        @PathVariable Long id,
        @RequestBody @Valid @Validated SceneUpdateDTO dto
    ) {
        SceneVO scene = sceneService.update(id, dto);
        return Result.success("更新成功", scene);
    }

    /**
     * 删除场景
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除场景")
    public Result<Void> delete(
        @Parameter(description = "场景 ID", example = "1")
        @PathVariable Long id
    ) {
        sceneService.delete(id);
        return Result.success("删除成功", null);
    }

    /**
     * 启用/禁用场景
     */
    @PostMapping("/{id}/toggle")
    @Operation(summary = "启用/禁用场景")
    public Result<SceneVO> toggle(
        @Parameter(description = "场景 ID", example = "1")
        @PathVariable Long id
    ) {
        SceneVO scene = sceneService.toggle(id);
        return Result.success("操作成功", scene);
    }

    /**
     * 触发场景
     */
    @PostMapping("/{id}/trigger")
    @Operation(summary = "触发场景")
    public Result<Void> trigger(
        @Parameter(description = "场景 ID", example = "1")
        @PathVariable Long id
    ) {
        sceneService.trigger(id);
        return Result.success("场景触发成功", null);
    }

    /**
     * 添加场景设备关联
     */
    @PostMapping("/{id}/devices")
    @Operation(summary = "添加场景设备关联")
    public Result<Void> addDevice(
        @Parameter(description = "场景 ID", example = "1")
        @PathVariable Long id,
        @Parameter(description = "设备 ID", example = "1")
        @RequestParam Long deviceId,
        @Parameter(description = "设备配置", example = "{\"brightness\": 50}")
        @RequestParam(required = false) String config,
        @Parameter(description = "目标状态：0-禁用，1-启用", example = "1")
        @RequestParam(required = false, defaultValue = "1") Integer targetStatus
    ) {
        sceneService.addDevice(id, deviceId, config, targetStatus);
        return Result.success("关联成功", null);
    }

    /**
     * 移除场景设备关联
     */
    @DeleteMapping("/{id}/devices")
    @Operation(summary = "移除场景设备关联")
    public Result<Void> removeDevice(
        @Parameter(description = "场景 ID", example = "1")
        @PathVariable Long id,
        @Parameter(description = "设备 ID", example = "1")
        @RequestParam Long deviceId
    ) {
        sceneService.removeDevice(id, deviceId);
        return Result.success("移除成功", null);
    }
}
