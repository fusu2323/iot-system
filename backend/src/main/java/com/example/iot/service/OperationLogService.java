package com.example.iot.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.iot.entity.OperationLog;
import com.example.iot.vo.OperationLogVO;

/**
 * 操作日志服务接口
 */
public interface OperationLogService {

    /**
     * 记录操作日志
     *
     * @param userId 用户 ID
     * @param operation 操作类型
     * @param targetType 目标类型
     * @param targetId 目标 ID
     * @param ipAddress IP 地址
     */
    void log(Long userId, String operation, String targetType, Long targetId, String ipAddress);

    /**
     * 分页查询操作日志
     *
     * @param page 页码
     * @param size 每页大小
     * @param userId 用户 ID（可选）
     * @param operation 操作类型（可选）
     * @return 日志列表
     */
    IPage<OperationLogVO> list(Integer page, Integer size, Long userId, String operation);

    /**
     * 根据 ID 查询操作日志
     *
     * @param id 日志 ID
     * @return 日志信息
     */
    OperationLogVO getById(Long id);
}
