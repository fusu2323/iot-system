package com.example.iot.controller;

import com.example.iot.common.result.Result;
import com.example.iot.entity.SystemConfig;
import com.example.iot.service.SystemConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 系统配置控制器
 */
@RestController
@RequestMapping("/api/system/configs")
@Tag(name = "系统配置", description = "系统配置管理接口")
public class SystemConfigController {

    private final SystemConfigService systemConfigService;

    public SystemConfigController(SystemConfigService systemConfigService) {
        this.systemConfigService = systemConfigService;
    }

    /**
     * 根据键获取配置值
     */
    @GetMapping("/{configKey}")
    @Operation(summary = "根据键获取配置值")
    public Result<String> getValueByKey(
        @Parameter(description = "配置键", example = "app.name")
        @PathVariable String configKey
    ) {
        String value = systemConfigService.getValueByKey(configKey);
        if (value == null) {
            return Result.error("配置不存在");
        }
        return Result.success(value);
    }

    /**
     * 设置配置值
     */
    @PostMapping
    @Operation(summary = "设置配置值")
    public Result<Void> setValue(
        @Parameter(description = "配置键", example = "app.name", required = true)
        @RequestParam String configKey,
        @Parameter(description = "配置值", example = "智能家居系统", required = true)
        @RequestParam String configValue,
        @Parameter(description = "描述", example = "系统名称")
        @RequestParam(required = false) String description
    ) {
        systemConfigService.setValue(configKey, configValue, description);
        return Result.success();
    }

    /**
     * 获取所有系统配置
     */
    @GetMapping
    @Operation(summary = "获取所有系统配置")
    public Result<List<SystemConfig>> getAllConfigs() {
        return Result.success(systemConfigService.getAllConfigs());
    }

    /**
     * 删除配置
     */
    @DeleteMapping("/{configKey}")
    @Operation(summary = "删除配置")
    public Result<Void> deleteByKey(
        @Parameter(description = "配置键", example = "app.name")
        @PathVariable String configKey
    ) {
        systemConfigService.deleteByKey(configKey);
        return Result.success();
    }
}
