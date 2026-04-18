package com.example.iot.common.result;

/**
 * 统一错误码枚举
 */
public enum ResultCode {

    // 通用错误码 (200-999)
    SUCCESS(200, "操作成功"),
    ERROR(500, "操作失败"),
    INVALID_PARAM(400, "参数错误"),
    UNAUTHORIZED(401, "未授权，请先登录"),
    FORBIDDEN(403, "拒绝访问"),
    NOT_FOUND(404, "请求地址不存在"),
    METHOD_NOT_ALLOWED(405, "请求方法错误"),
    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),

    // 认证相关错误码 (1000-1999)
    TOKEN_EMPTY(1001, "Token 为空"),
    TOKEN_INVALID(1002, "Token 无效"),
    TOKEN_EXPIRED(1003, "Token 已过期"),
    LOGIN_FAILED(1004, "登录失败"),
    USERNAME_OR_PASSWORD_ERROR(1005, "用户名或密码错误"),
    USER_NOT_FOUND(1006, "用户不存在"),
    USER_ALREADY_EXISTS(1007, "用户已存在"),
    PASSWORD_ERROR(1008, "密码错误"),
    REGISTER_FAILED(1009, "注册失败"),
    EMAIL_ALREADY_EXISTS(1010, "该邮箱已被注册"),

    // 设备相关错误码 (2000-2999)
    DEVICE_NOT_FOUND(2001, "设备不存在"),
    DEVICE_ALREADY_EXISTS(2002, "设备已存在"),
    DEVICE_STATUS_ERROR(2003, "设备状态错误"),

    // 场景相关错误码 (3000-3999)
    SCENE_NOT_FOUND(3001, "场景不存在"),
    SCENE_ALREADY_EXISTS(3002, "场景已存在"),
    SCENE_DEVICE_NOT_FOUND(3003, "场景设备关联不存在"),

    // 内容相关错误码 (4000-4999)
    CONTENT_NOT_FOUND(4001, "内容不存在"),

    // 推荐相关错误码 (5000-5999)
    PREFERENCE_NOT_FOUND(5001, "用户偏好不存在"),

    // 日志相关错误码 (6000-6999)
    LOG_NOT_FOUND(6001, "日志不存在");

    private final Integer code;
    private final String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() { return code; }
    public String getMessage() { return message; }
}
