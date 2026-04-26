package com.example.iot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.iot.entity.RecommendationFeedback;
import org.apache.ibatis.annotations.Mapper;

/**
 * 推荐反馈 Mapper 接口
 */
@Mapper
public interface RecommendationFeedbackMapper extends BaseMapper<RecommendationFeedback> {
}
