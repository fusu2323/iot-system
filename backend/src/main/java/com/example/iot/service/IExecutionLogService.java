package com.example.iot.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.iot.dto.ExecutionLogQueryDTO;
import com.example.iot.entity.vo.ExecutionLogVO;

import java.time.LocalDateTime;

/**
 * 定时任务执行日志服务接口
 */
public interface IExecutionLogService {

    /**
     * 保存执行日志
     * @param taskId 任务ID
     * @param sceneId 场景ID
     * @param triggerTime 触发时间
     * @param status 执行状态：0=失败,1=成功
     * @param errorMsg 错误信息（失败时）
     */
    void saveLog(Long taskId, Long sceneId, LocalDateTime triggerTime, Integer status, String errorMsg);

    /**
     * 按任务ID查询执行记录（分页）
     * @param taskId 任务ID
     * @param queryDTO 查询参数（page, size, startTime, endTime）
     * @return 分页执行记录结果
     */
    IPage<ExecutionLogVO> listByTaskId(Long taskId, ExecutionLogQueryDTO queryDTO);
}