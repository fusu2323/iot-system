package com.example.iot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.iot.entity.ScheduledTaskExecutionLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 定时任务执行日志Mapper
 */
@Mapper
public interface ScheduledTaskExecutionLogMapper extends BaseMapper<ScheduledTaskExecutionLog> {
}