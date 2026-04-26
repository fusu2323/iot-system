package com.example.iot.service;

import com.example.iot.vo.DeviceStatisticsVO;

import java.util.Map;

/**
 * 设备统计服务接口
 */
public interface DeviceStatisticsService {

    /**
     * 获取设备统计概览
     *
     * @return 统计信息
     */
    Map<String, Integer> getOverview();

    /**
     * 按类型统计设备数量
     *
     * @return 统计列表
     */
    java.util.List<DeviceStatisticsVO> countByType();

    /**
     * 按房间统计设备数量
     *
     * @return 统计列表
     */
    java.util.List<DeviceStatisticsVO> countByRoom();

    /**
     * 按状态统计设备数量
     *
     * @return 统计列表
     */
    java.util.List<DeviceStatisticsVO> countByStatus();

    /**
     * 统计在线设备数量
     *
     * @return 在线数量
     */
    Integer countOnline();

    /**
     * 统计设备总数
     *
     * @return 设备总数
     */
    Integer countTotal();
}
