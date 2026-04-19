package com.example.iot.controller;

import com.example.iot.common.result.Result;
import com.example.iot.dto.UserPreferenceDTO;
import com.example.iot.service.UserPreferenceService;
import com.example.iot.vo.UserPreferenceVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户偏好控制器
 */
@RestController
@RequestMapping("/api/preferences")
@Tag(name = "推荐管理", description = "推荐和偏好设置接口")
public class PreferenceController {

    private final UserPreferenceService userPreferenceService;

    public PreferenceController(UserPreferenceService userPreferenceService) {
        this.userPreferenceService = userPreferenceService;
    }

    /**
     * 设置用户偏好
     */
    @PostMapping
    @Operation(summary = "设置用户偏好")
    public Result<Void> setPreference(
        @Parameter(description = "用户 ID", example = "1")
        @RequestParam Long userId,
        @RequestBody @Valid @Validated UserPreferenceDTO dto
    ) {
        userPreferenceService.setPreference(userId, dto.getContentType(), dto.getPreferenceScore(),
            dto.getClickWeight(), dto.getLikeWeight(), dto.getDislikeWeight());
        return Result.success("设置成功", null);
    }

    /**
     * 获取用户偏好列表
     */
    @GetMapping
    @Operation(summary = "获取用户偏好列表")
    public Result<List<UserPreferenceVO>> getPreferences(
        @Parameter(description = "用户 ID", example = "1")
        @RequestParam Long userId
    ) {
        List<UserPreferenceVO> preferences = userPreferenceService.getPreferences(userId);
        return Result.success(preferences);
    }

    /**
     * 获取用户指定类型的偏好
     */
    @GetMapping("/{contentType}")
    @Operation(summary = "获取用户指定类型的偏好")
    public Result<UserPreferenceVO> getPreference(
        @Parameter(description = "用户 ID", example = "1")
        @RequestParam Long userId,
        @Parameter(description = "内容类型", example = "MOVIE")
        @PathVariable String contentType
    ) {
        UserPreferenceVO preference = userPreferenceService.getPreference(userId, contentType);
        return Result.success(preference);
    }

    /**
     * 删除用户偏好
     */
    @DeleteMapping("/{contentType}")
    @Operation(summary = "删除用户偏好")
    public Result<Void> deletePreference(
        @Parameter(description = "用户 ID", example = "1")
        @RequestParam Long userId,
        @Parameter(description = "内容类型", example = "MOVIE")
        @PathVariable String contentType
    ) {
        userPreferenceService.deletePreference(userId, contentType);
        return Result.success("删除成功", null);
    }
}
