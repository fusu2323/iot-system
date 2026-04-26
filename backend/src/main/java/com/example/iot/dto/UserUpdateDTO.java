package com.example.iot.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

/**
 * 用户更新请求 DTO
 */
@Schema(description = "用户更新请求")
public class UserUpdateDTO {

    @Schema(description = "昵称", example = "张三")
    @Size(max = 50, message = "昵称长度不能超过 50")
    private String nickname;

    @Schema(description = "头像 URL", example = "https://example.com/avatar.jpg")
    private String avatar;

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
}
