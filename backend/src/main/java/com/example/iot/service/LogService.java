package com.example.iot.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.iot.vo.OperationLogVO;

import java.util.Map;

/**
 * 日志管理服务接口
 */
public interface LogService {

    /**
     * 分页查询操作日志
     *
     * @param page 页码
     * @param size 每页大小
     * @param userId 用户 ID
     * @param operation 操作类型
     * @param targetType 目标类型
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 日志列表
     */
    IPage<OperationLogVO> list(Integer page, Integer size, Long userId, String operation,
                                String targetType, String startDate, String endDate);

    /**
     * 根据 ID 查询操作日志
     *
     * @param id 日志 ID
     * @return 日志详情
     */
    OperationLogVO getById(Long id);

    /**
     * 统计各操作类型的数量
     *
     * @return 统计列表
     */
    java.util.List<Map<String, Object>> countByOperation();

    /**
     * 统计每日日志数量
     *
     * @param days 天数
     * @return 统计列表
     */
    java.util.List<Map<String, Object>> countByDay(Integer days);
}
