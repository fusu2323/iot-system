package com.example.iot.common.aspect;

import java.lang.annotation.*;

/**
 * 操作日志注解
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperationLog {

    /**
     * 操作类型
     */
    String operation() default "";

    /**
     * 目标类型
     */
    String targetType() default "";
}
