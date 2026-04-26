package com.example.iot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.iot.entity.SceneDevice;
import org.apache.ibatis.annotations.Mapper;

/**
 * 场景设备关联 Mapper 接口
 */
@Mapper
public interface SceneDeviceMapper extends BaseMapper<SceneDevice> {
}
