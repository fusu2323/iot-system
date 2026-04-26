package com.example.iot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.iot.entity.Device;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 设备 Mapper 接口
 */
@Mapper
public interface DeviceMapper extends BaseMapper<Device> {

    /**
     * 分页查询设备列表（支持多条件筛选）
     */
    @Select("SELECT * FROM device ${ew.customSqlSegment} ORDER BY create_time DESC")
    IPage<Device> selectPageWithWrapper(IPage<Device> page,
                                         @Param("ew") com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<Device> wrapper);

    /**
     * 分页查询设备列表（简单查询）
     */
    @Select("SELECT * FROM device WHERE deleted = 0 ORDER BY create_time DESC")
    IPage<Device> selectPage(IPage<Device> page);

    /**
     * 统计各类型设备数量
     */
    @Select("SELECT type, COUNT(*) as count FROM device WHERE deleted = 0 GROUP BY type")
    List<Map<String, Object>> countByType();

    /**
     * 统计各房间设备数量
     */
    @Select("SELECT room, COUNT(*) as count FROM device WHERE deleted = 0 GROUP BY room")
    List<Map<String, Object>> countByRoom();

    /**
     * 统计设备状态
     */
    @Select("SELECT status, COUNT(*) as count FROM device WHERE deleted = 0 GROUP BY status")
    List<Map<String, Object>> countByStatus();

    /**
     * 统计在线设备数量
     */
    @Select("SELECT COUNT(*) as count FROM device WHERE deleted = 0 AND is_online = 1")
    Map<String, Object> countOnline();
}
