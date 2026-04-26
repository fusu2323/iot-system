package com.example.iot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.iot.entity.Content;
import org.apache.ibatis.annotations.Mapper;

/**
 * 内容 Mapper 接口
 */
@Mapper
public interface ContentMapper extends BaseMapper<Content> {
}
