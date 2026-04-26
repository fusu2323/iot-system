package com.example.iot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.iot.entity.Scene;
import org.apache.ibatis.annotations.Mapper;

/**
 * 场景 Mapper 接口
 */
@Mapper
public interface SceneMapper extends BaseMapper<Scene> {
}
