package com.example.iot.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.iot.common.result.Result;
import com.example.iot.dto.ContentCreateDTO;
import com.example.iot.service.ContentService;
import com.example.iot.vo.ContentVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 内容管理控制器
 */
@RestController
@RequestMapping("/api/contents")
@Tag(name = "内容管理", description = "内容信息 CRUD 接口")
public class ContentController {

    private final ContentService contentService;

    public ContentController(ContentService contentService) {
        this.contentService = contentService;
    }

    /**
     * 查询内容列表
     */
    @GetMapping
    @Operation(summary = "查询内容列表")
    public Result<IPage<ContentVO>> list(
        @Parameter(description = "页码", example = "1")
        @RequestParam(defaultValue = "1") Integer page,
        @Parameter(description = "每页大小", example = "10")
        @RequestParam(defaultValue = "10") Integer size,
        @Parameter(description = "搜索关键词", example = "流浪地球")
        @RequestParam(required = false) String keyword,
        @Parameter(description = "内容类型", example = "MOVIE")
        @RequestParam(required = false) String type
    ) {
        IPage<ContentVO> contents = contentService.list(page, size, keyword, type);
        return Result.success(contents);
    }

    /**
     * 查询内容详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "查询内容详情")
    public Result<ContentVO> getContentById(
        @Parameter(description = "内容 ID", example = "1")
        @PathVariable Long id
    ) {
        ContentVO content = contentService.getById(id);
        return Result.success(content);
    }

    /**
     * 创建内容
     */
    @PostMapping
    @Operation(summary = "创建内容")
    public Result<Long> create(
        @RequestBody @Valid @Validated ContentCreateDTO dto
    ) {
        Long contentId = contentService.create(dto);
        return Result.success("创建成功", contentId);
    }

    /**
     * 更新内容信息
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新内容信息")
    public Result<ContentVO> update(
        @Parameter(description = "内容 ID", example = "1")
        @PathVariable Long id,
        @RequestBody @Valid @Validated ContentCreateDTO dto
    ) {
        ContentVO content = contentService.update(id, dto);
        return Result.success("更新成功", content);
    }

    /**
     * 删除内容
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除内容")
    public Result<Void> delete(
        @Parameter(description = "内容 ID", example = "1")
        @PathVariable Long id
    ) {
        contentService.delete(id);
        return Result.success("删除成功", null);
    }
}
