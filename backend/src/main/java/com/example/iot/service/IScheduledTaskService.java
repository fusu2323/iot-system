package com.example.iot.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.iot.dto.ScheduledTaskCreateDTO;
import com.example.iot.dto.ScheduledTaskUpdateDTO;
import com.example.iot.entity.vo.ScheduledTaskVO;

/**
 * 定时任务服务接口
 */
public interface IScheduledTaskService {

    /**
     * 创建定时任务
     * @param dto 创建DTO
     * @param userId 创建人ID
     * @return 任务ID
     */
    Long create(ScheduledTaskCreateDTO dto, Long userId);

    /**
     * 更新定时任务
     * @param id 任务ID
     * @param dto 更新DTO
     * @return 更新后的任务VO
     */
    ScheduledTaskVO update(Long id, ScheduledTaskUpdateDTO dto);

    /**
     * 删除定时任务（软删除）
     * @param id 任务ID
     */
    void delete(Long id);

    /**
     * 分页查询定时任务
     * @param page 页码
     * @param size 每页大小
     * @param name 任务名称（模糊查询）
     * @return 分页结果
     */
    IPage<ScheduledTaskVO> listPage(Integer page, Integer size, String name);

    /**
     * 获取任务详情
     * @param id 任务ID
     * @return 任务VO
     */
    ScheduledTaskVO getById(Long id);

    /**
     * 启用/禁用任务
     * @param id 任务ID
     * @return 更新后的任务VO
     */
    ScheduledTaskVO toggle(Long id);
}