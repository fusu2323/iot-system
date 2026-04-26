package com.example.iot.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.iot.vo.DashboardStatsVO;
import com.example.iot.vo.DeviceTrendVO;
import com.example.iot.vo.UserActivityVO;

import java.util.List;
import java.util.Map;

/**
 * 数据统计服务接口
 */
public interface DashboardService {

    /**
     * 获取 Dashboard 统计概览
     *
     * @return 统计信息
     */
    DashboardStatsVO getOverview();

    /**
     * 获取设备趋势统计
     *
     * @param days 天数
     * @return 趋势列表
     */
    List<DeviceTrendVO> getDeviceTrend(Integer days);

    /**
     * 获取用户活跃度统计
     *
     * @param days 天数
     * @return 活跃度列表
     */
    List<UserActivityVO> getUserActivity(Integer days);

    /**
     * 获取场景使用统计
     *
     * @return 统计列表
     */
    List<Map<String, Object>> getSceneUsage();

    /**
     * 获取内容类型分布
     *
     * @return 分布统计
     */
    List<Map<String, Object>> getContentDistribution();

    /**
     * 获取系统信息
     *
     * @return 系统信息
     */
    Map<String, String> getSystemInfo();
}
