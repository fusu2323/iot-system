package com.example.iot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.iot.entity.OperationLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 操作日志 Mapper 接口
 */
@Mapper
public interface OperationLogMapper extends BaseMapper<OperationLog> {

    /**
     * 分页查询用户操作日志
     */
    @Select("SELECT * FROM operation_log ${ew.customSqlSegment} ORDER BY create_time DESC")
    IPage<OperationLog> selectPageWithWrapper(IPage<OperationLog> page,
                                               @Param("ew") com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<OperationLog> wrapper);

    /**
     * 分页查询操作日志（使用 LambdaQueryWrapper）
     */
    @Select("SELECT * FROM operation_log WHERE deleted = 0 ORDER BY create_time DESC")
    IPage<OperationLog> selectPage(IPage<OperationLog> page);
}
