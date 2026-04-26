package com.example.iot.service.impl;

import com.example.iot.mapper.DeviceMapper;
import com.example.iot.service.DeviceStatisticsService;
import com.example.iot.vo.DeviceStatisticsVO;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 设备统计服务实现类
 */
@Service
public class DeviceStatisticsServiceImpl implements DeviceStatisticsService {

    private final DeviceMapper deviceMapper;

    public DeviceStatisticsServiceImpl(DeviceMapper deviceMapper) {
        this.deviceMapper = deviceMapper;
    }

    @Override
    public Map<String, Integer> getOverview() {
        Map<String, Integer> overview = new HashMap<>();
        overview.put("total", countTotal());
        overview.put("online", countOnline());
        overview.put("offline", countTotal() - countOnline());
        return overview;
    }

    @Override
    public List<DeviceStatisticsVO> countByType() {
        List<Map<String, Object>> results = deviceMapper.countByType();
        return results.stream().map(this::mapToStatistics).toList();
    }

    @Override
    public List<DeviceStatisticsVO> countByRoom() {
        List<Map<String, Object>> results = deviceMapper.countByRoom();
        return results.stream().map(this::mapToStatistics).toList();
    }

    @Override
    public List<DeviceStatisticsVO> countByStatus() {
        List<Map<String, Object>> results = deviceMapper.countByStatus();
        return results.stream().map(this::mapToStatistics).toList();
    }

    @Override
    public Integer countOnline() {
        Map<String, Object> result = deviceMapper.countOnline();
        return result != null ? ((Number) result.get("count")).intValue() : 0;
    }

    @Override
    public Integer countTotal() {
        return Math.toIntExact(deviceMapper.selectCount(null));
    }

    /**
     * Map 转 StatisticsVO
     */
    private DeviceStatisticsVO mapToStatistics(Map<String, Object> map) {
        DeviceStatisticsVO vo = new DeviceStatisticsVO();
        // 根据不同的统计类型，获取对应的 name 字段
        if (map.containsKey("type")) {
            vo.setName((String) map.get("type"));
        } else if (map.containsKey("room")) {
            vo.setName((String) map.get("room"));
        } else if (map.containsKey("status")) {
            vo.setName(((Number) map.get("status")).intValue() == 1 ? "启用" : "禁用");
        }
        vo.setCount(((Number) map.get("count")).intValue());
        return vo;
    }
}
