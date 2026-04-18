package com.example.iot.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.iot.common.result.Result;
import com.example.iot.dto.UserUpdateDTO;
import com.example.iot.security.SecurityContextUtil;
import com.example.iot.service.UserService;
import com.example.iot.vo.UserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户管理控制器
 */
@RestController
@RequestMapping("/api/users")
@Tag(name = "用户管理", description = "用户信息 CRUD 接口")
public class UserController {

    private final UserService userService;
    private final SecurityContextUtil securityContextUtil;

    public UserController(UserService userService, SecurityContextUtil securityContextUtil) {
        this.userService = userService;
        this.securityContextUtil = securityContextUtil;
    }

    /**
     * 获取当前登录用户信息
     */
    @GetMapping("/me")
    @Operation(summary = "获取当前用户信息")
    public Result<UserVO> getCurrentUser() {
        Long currentUserId = securityContextUtil.getCurrentUserId();
        UserVO user = userService.getById(currentUserId);
        return Result.success(user);
    }

    /**
     * 根据 ID 查询用户
     */
    @GetMapping("/{id}")
    @Operation(summary = "根据 ID 查询用户")
    public Result<UserVO> getUserById(
        @Parameter(description = "用户 ID", example = "1")
        @PathVariable Long id
    ) {
        UserVO user = userService.getById(id);
        return Result.success(user);
    }

    /**
     * 分页查询用户列表
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "查询用户列表")
    public Result<IPage<UserVO>> list(
        @Parameter(description = "页码", example = "1")
        @RequestParam(defaultValue = "1") Integer page,
        @Parameter(description = "每页大小", example = "10")
        @RequestParam(defaultValue = "10") Integer size,
        @Parameter(description = "搜索关键词", example = "admin")
        @RequestParam(required = false) String keyword
    ) {
        IPage<UserVO> users = userService.list(page, size, keyword,
            securityContextUtil.getCurrentUserId(),
            securityContextUtil.getCurrentUserRole());
        return Result.success(users);
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新用户信息")
    public Result<UserVO> update(
        @Parameter(description = "用户 ID", example = "1")
        @PathVariable Long id,
        @RequestBody @Valid @Validated UserUpdateDTO dto
    ) {
        UserVO user = userService.update(id, dto);
        return Result.success("更新成功", user);
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "删除用户")
    public Result<Void> delete(
        @Parameter(description = "用户 ID", example = "1")
        @PathVariable Long id
    ) {
        userService.delete(id,
            securityContextUtil.getCurrentUserId(),
            securityContextUtil.getCurrentUserRole());
        return Result.success("删除成功", null);
    }
}
