package com.example.iot.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.iot.common.exception.BusinessException;
import com.example.iot.common.result.Result;
import com.example.iot.common.result.ResultCode;
import com.example.iot.dto.DeviceCreateDTO;
import com.example.iot.dto.DeviceStatusUpdateDTO;
import com.example.iot.dto.DeviceUpdateDTO;
import com.example.iot.security.SecurityContextUtil;
import com.example.iot.service.DeviceService;
import com.example.iot.vo.DeviceStatisticsVO;
import com.example.iot.vo.DeviceVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 设备管理控制器
 */
@RestController
@RequestMapping("/api/devices")
@Tag(name = "设备管理", description = "设备信息 CRUD 接口")
public class DeviceController {

    private final DeviceService deviceService;
    private final SecurityContextUtil securityContextUtil;

    public DeviceController(DeviceService deviceService, SecurityContextUtil securityContextUtil) {
        this.deviceService = deviceService;
        this.securityContextUtil = securityContextUtil;
    }

    /**
     * 查询设备列表
     */
    @GetMapping
    @Operation(summary = "查询设备列表")
    public Result<IPage<DeviceVO>> list(
        @Parameter(description = "页码", example = "1")
        @RequestParam(defaultValue = "1") Integer page,
        @Parameter(description = "每页大小", example = "10")
        @RequestParam(defaultValue = "10") Integer size,
        @Parameter(description = "搜索关键词", example = "电视")
        @RequestParam(required = false) String keyword,
        @Parameter(description = "设备类型", example = "TV")
        @RequestParam(required = false) String type,
        @Parameter(description = "房间", example = "客厅")
        @RequestParam(required = false) String room,
        @Parameter(description = "状态", example = "1")
        @RequestParam(required = false) Integer status,
        @Parameter(description = "在线状态", example = "1")
        @RequestParam(required = false) Integer isOnline,
        @Parameter(description = "用户 ID", example = "1")
        @RequestParam(required = false) Long userId
    ) {
        Long currentUserId = securityContextUtil.getCurrentUserId();
        String currentRole = securityContextUtil.getCurrentUserRole();
        Long effectiveUserId = userId;
        if (!"ADMIN".equals(currentRole)) {
            effectiveUserId = currentUserId;
        }
        IPage<DeviceVO> devices = deviceService.list(page, size, keyword, type, room, status, isOnline, effectiveUserId);
        return Result.success(devices);
    }

    /**
     * 查询设备详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "查询设备详情")
    public Result<DeviceVO> getDeviceById(
        @Parameter(description = "设备 ID", example = "1")
        @PathVariable Long id
    ) {
        DeviceVO device = deviceService.getById(id);
        Long currentUserId = securityContextUtil.getCurrentUserId();
        String currentRole = securityContextUtil.getCurrentUserRole();
        if (!"ADMIN".equals(currentRole) && (device.getUserId() == null || !currentUserId.equals(device.getUserId()))) {
            throw new BusinessException(ResultCode.FORBIDDEN.getCode(), "无权限查看其他用户设备");
        }
        return Result.success(device);
    }

    /**
     * 创建设备
     */
    @PostMapping
    @Operation(summary = "创建设备")
    public Result<Long> create(
        @RequestBody @Valid @Validated DeviceCreateDTO dto,
        @Parameter(description = "用户 ID", example = "1")
        @RequestParam(required = false) Long userId
    ) {
        Long currentUserId = securityContextUtil.getCurrentUserId();
        String currentRole = securityContextUtil.getCurrentUserRole();
        Long effectiveUserId = userId;
        if (!"ADMIN".equals(currentRole)) {
            effectiveUserId = currentUserId;
        } else if (effectiveUserId == null) {
            effectiveUserId = currentUserId;
        }
        Long deviceId = deviceService.create(dto, effectiveUserId);
        return Result.success("创建成功", deviceId);
    }

    /**
     * 更新设备信息
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新设备信息")
    public Result<DeviceVO> update(
        @Parameter(description = "设备 ID", example = "1")
        @PathVariable Long id,
        @RequestBody @Valid @Validated DeviceUpdateDTO dto
    ) {
        DeviceVO existing = deviceService.getById(id);
        Long currentUserId = securityContextUtil.getCurrentUserId();
        String currentRole = securityContextUtil.getCurrentUserRole();
        if (!"ADMIN".equals(currentRole) && (existing.getUserId() == null || !currentUserId.equals(existing.getUserId()))) {
            throw new BusinessException(ResultCode.FORBIDDEN.getCode(), "无权限修改其他用户设备");
        }
        DeviceVO device = deviceService.update(id, dto);
        return Result.success("更新成功", device);
    }

    /**
     * 删除设备
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除设备")
    public Result<Void> delete(
        @Parameter(description = "设备 ID", example = "1")
        @PathVariable Long id
    ) {
        DeviceVO existing = deviceService.getById(id);
        Long currentUserId = securityContextUtil.getCurrentUserId();
        String currentRole = securityContextUtil.getCurrentUserRole();
        if (!"ADMIN".equals(currentRole) && (existing.getUserId() == null || !currentUserId.equals(existing.getUserId()))) {
            throw new BusinessException(ResultCode.FORBIDDEN.getCode(), "无权限删除其他用户设备");
        }
        deviceService.delete(id);
        return Result.success("删除成功", null);
    }

    /**
     * 更新设备状态
     */
    @PutMapping("/{id}/status")
    @Operation(summary = "更新设备状态")
    public Result<DeviceVO> updateStatus(
        @Parameter(description = "设备 ID", example = "1")
        @PathVariable Long id,
        @RequestBody @Valid @Validated DeviceStatusUpdateDTO dto
    ) {
        DeviceVO existing = deviceService.getById(id);
        Long currentUserId = securityContextUtil.getCurrentUserId();
        String currentRole = securityContextUtil.getCurrentUserRole();
        if (!"ADMIN".equals(currentRole) && (existing.getUserId() == null || !currentUserId.equals(existing.getUserId()))) {
            throw new BusinessException(ResultCode.FORBIDDEN.getCode(), "无权限修改其他用户设备");
        }
        DeviceVO device = deviceService.updateStatus(id, dto);
        return Result.success("状态更新成功", device);
    }

    /**
     * 统计各类型设备数量
     */
    @GetMapping("/statistics/types")
    @Operation(summary = "统计各类型设备数量")
    public Result<List<DeviceStatisticsVO>> countByType() {
        List<DeviceStatisticsVO> statistics = deviceService.countByType();
        return Result.success(statistics);
    }

    /**
     * 统计各房间设备数量
     */
    @GetMapping("/statistics/rooms")
    @Operation(summary = "统计各房间设备数量")
    public Result<List<DeviceStatisticsVO>> countByRoom() {
        List<DeviceStatisticsVO> statistics = deviceService.countByRoom();
        return Result.success(statistics);
    }
}
