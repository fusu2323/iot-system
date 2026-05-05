package com.example.iot.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.iot.vo.UsageRankingVO;
import com.example.iot.vo.UsageStatsVO;
import com.example.iot.vo.UsageTimelineVO;

/**
 * 统计日志服务接口
 */
public interface StatLogService {

    /**
     * 记录使用日志
     * @param userId 用户 ID
     * @param targetType 目标类型：DEVICE / SCENE
     * @param targetId 目标 ID
     * @param action 动作：ACTIVATE / TRIGGER
     */
    void log(Long userId, String targetType, Long targetId, String action);

    /**
     * STATS-03: 用户查询自己设备和场景的使用统计（按日/周/月维度）
     */
    UsageStatsVO getMyStatistics(Long userId, String dimension, String targetType,
                                  String startDate, String endDate);

    /**
     * STATS-04: 管理员查询全局使用排行（前 N 名）
     */
    UsageRankingVO getGlobalRanking(String targetType, Integer limit);

    /**
     * STATS-05: 用户查看单条设备或场景的历史激活时间线
     */
    UsageTimelineVO getTimeline(Long userId, String targetType, Long targetId,
                                 Integer page, Integer size);
}
