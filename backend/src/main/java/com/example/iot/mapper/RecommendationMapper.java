package com.example.iot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.iot.entity.Recommendation;
import org.apache.ibatis.annotations.Mapper;

/**
 * 推荐记录 Mapper 接口
 */
@Mapper
public interface RecommendationMapper extends BaseMapper<Recommendation> {
}
