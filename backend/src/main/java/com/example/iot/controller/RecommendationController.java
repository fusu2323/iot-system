package com.example.iot.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.iot.common.result.Result;
import com.example.iot.dto.RecommendationFeedbackDTO;
import com.example.iot.service.RecommendationService;
import com.example.iot.vo.GroupedRecommendationResponse;
import com.example.iot.vo.RecommendationFeedbackVO;
import com.example.iot.vo.RecommendationVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 推荐管理控制器
 */
@RestController
@RequestMapping("/api/recommendations")
@Tag(name = "推荐管理", description = "推荐和偏好设置接口")
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    /**
     * 获取推荐列表（分组返回）
     */
    @GetMapping
    @Operation(summary = "获取推荐列表（按类型分组）")
    public Result<GroupedRecommendationResponse> getRecommendations(
        @Parameter(description = "用户 ID", example = "1")
        @RequestParam Long userId,
        @Parameter(description = "内容类型筛选", example = "MOVIE")
        @RequestParam(required = false) String type,
        @Parameter(description = "页码", example = "1")
        @RequestParam(defaultValue = "1") Integer page,
        @Parameter(description = "每页大小", example = "10")
        @RequestParam(defaultValue = "10") Integer size
    ) {
        GroupedRecommendationResponse recommendations = recommendationService.getRecommendations(userId, type, page, size);
        return Result.success(recommendations);
    }

    /**
     * 记录推荐点击
     */
    @PostMapping("/click")
    @Operation(summary = "记录推荐点击")
    public Result<Void> recordClick(
        @Parameter(description = "用户 ID", example = "1")
        @RequestParam Long userId,
        @Parameter(description = "内容 ID", example = "1")
        @RequestParam Long contentId
    ) {
        recommendationService.recordClick(userId, contentId);
        return Result.success("已记录点击", null);
    }

    /**
     * 记录推荐收藏/喜欢
     */
    @PostMapping("/like")
    @Operation(summary = "记录推荐收藏/喜欢")
    public Result<Void> recordLike(
        @Parameter(description = "用户 ID", example = "1")
        @RequestParam Long userId,
        @Parameter(description = "内容 ID", example = "1")
        @RequestParam Long contentId
    ) {
        recommendationService.recordLike(userId, contentId);
        return Result.success("已记录喜欢", null);
    }

    /**
     * 记录不喜欢
     */
    @PostMapping("/dislike")
    @Operation(summary = "记录不喜欢")
    public Result<Void> recordDislike(
        @Parameter(description = "用户 ID", example = "1")
        @RequestParam Long userId,
        @Parameter(description = "内容 ID", example = "1")
        @RequestParam Long contentId
    ) {
        recommendationService.recordDislike(userId, contentId);
        return Result.success("已记录不喜欢", null);
    }

    /**
     * 提交推荐反馈
     */
    @PostMapping("/feedback")
    @Operation(summary = "提交推荐反馈")
    public Result<Void> submitFeedback(
        @RequestBody @Valid @Validated RecommendationFeedbackDTO dto
    ) {
        recommendationService.submitFeedback(dto);
        return Result.success("反馈提交成功", null);
    }

    /**
     * 获取用户反馈历史
     */
    @GetMapping("/feedback/history")
    @Operation(summary = "获取用户反馈历史")
    public Result<IPage<RecommendationFeedbackVO>> getFeedbackHistory(
        @Parameter(description = "用户 ID", example = "1")
        @RequestParam Long userId,
        @Parameter(description = "页码", example = "1")
        @RequestParam(defaultValue = "1") Integer page,
        @Parameter(description = "每页大小", example = "10")
        @RequestParam(defaultValue = "10") Integer size
    ) {
        IPage<RecommendationFeedbackVO> feedbacks = recommendationService.getUserFeedbackHistory(userId, page, size);
        return Result.success(feedbacks);
    }
}
