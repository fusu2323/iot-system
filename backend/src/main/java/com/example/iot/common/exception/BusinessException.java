package com.example.iot.common.exception;

import com.example.iot.common.result.ResultCode;

/**
 * 业务异常类
 */
public class BusinessException extends RuntimeException {

    private Integer code;

    public BusinessException(String message) {
        super(message);
        this.code = 500;
    }

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(com.example.iot.common.result.ResultCode resultCode) {
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.code = 500;
    }

    public BusinessException(ResultCode resultCode, String 只能管理员才能删除用户) {
    }

    public Integer getCode() {
        return code;
    }
}
