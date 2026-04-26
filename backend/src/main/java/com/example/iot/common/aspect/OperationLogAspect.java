package com.example.iot.common.aspect;

import com.example.iot.service.OperationLogService;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;

/**
 * 操作日志切面
 */
@Aspect
@Component
public class OperationLogAspect {

    private static final Logger log = LoggerFactory.getLogger(OperationLogAspect.class);

    private final OperationLogService operationLogService;

    public OperationLogAspect(OperationLogService operationLogService) {
        this.operationLogService = operationLogService;
    }

    /**
     * 切点：标记有@OperationLog 注解的方法
     */
    @Pointcut("@annotation(com.example.iot.common.aspect.OperationLog)")
    public void logPointcut() {
    }

    /**
     * 后置通知：方法执行后记录日志
     */
    @After("logPointcut()")
    public void afterReturning(JoinPoint joinPoint) {
        try {
            // 获取注解信息
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            OperationLog operationLog = method.getAnnotation(OperationLog.class);

            // 获取请求信息
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .currentRequestAttributes()).getRequest();

            // 获取 IP 地址
            String ipAddress = getIpAddress(request);

            // TODO: 获取当前用户 ID（需要从 SecurityContext 或 Token 中解析）
            // 这里暂时记录为 null
            Long currentUserId = null;

            // 记录操作日志
            operationLogService.log(
                currentUserId,
                operationLog.operation(),
                operationLog.targetType(),
                null,
                ipAddress
            );

            log.info("操作日志记录：{} - {}", operationLog.operation(), operationLog.targetType());
        } catch (Exception e) {
            log.error("记录操作日志失败：{}", e.getMessage(), e);
        }
    }

    /**
     * 获取请求 IP 地址
     */
    private String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 多个代理时，取第一个 IP
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0];
        }
        return ip;
    }
}