package com.example.iot.controller;

import com.example.iot.common.result.Result;
import com.example.iot.dto.LoginRequest;
import com.example.iot.dto.LoginResponse;
import com.example.iot.dto.UserRegisterDTO;
import com.example.iot.entity.User;
import com.example.iot.service.UserService;
import com.example.iot.vo.UserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户认证控制器
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "用户认证", description = "用户注册、登录相关接口")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    @Operation(summary = "用户注册")
    public Result<Long> register(@RequestBody @Valid @Validated UserRegisterDTO dto) {
        Long userId = userService.register(dto);
        return Result.success("注册成功", userId);
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录")
    public Result<LoginResponse> login(@RequestBody @Valid @Validated LoginRequest request) {
        String token = userService.login(request);

        // 查询用户信息
        User user = userService.getUserByUsername(request.getUsername());

        LoginResponse response = LoginResponse.of(
            token,
            user.getId(),
            user.getUsername(),
            user.getNickname(),
            user.getAvatar(),
            user.getRole()
        );

        return Result.success("登录成功", response);
    }
}
