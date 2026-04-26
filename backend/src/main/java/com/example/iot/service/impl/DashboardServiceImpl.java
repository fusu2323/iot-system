package com.example.iot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.iot.entity.Content;
import com.example.iot.entity.Device;
import com.example.iot.entity.OperationLog;
import com.example.iot.entity.Scene;
import com.example.iot.entity.User;
import com.example.iot.mapper.ContentMapper;
import com.example.iot.mapper.DeviceMapper;
import com.example.iot.mapper.OperationLogMapper;
import com.example.iot.mapper.SceneMapper;
import com.example.iot.mapper.UserMapper;
import com.example.iot.service.DashboardService;
import com.example.iot.vo.DashboardStatsVO;
import com.example.iot.vo.DeviceTrendVO;
import com.example.iot.vo.UserActivityVO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 数据统计服务实现类
 */
@Service
public class DashboardServiceImpl implements DashboardService {

    private final DeviceMapper deviceMapper;
    private final SceneMapper sceneMapper;
    private final ContentMapper contentMapper;
    private final UserMapper userMapper;
    private final OperationLogMapper operationLogMapper;

    public DashboardServiceImpl(DeviceMapper deviceMapper, SceneMapper sceneMapper,
                               ContentMapper contentMapper, UserMapper userMapper,
                               OperationLogMapper operationLogMapper) {
        this.deviceMapper = deviceMapper;
        this.sceneMapper = sceneMapper;
        this.contentMapper = contentMapper;
        this.userMapper = userMapper;
        this.operationLogMapper = operationLogMapper;
    }

    @Override
    public DashboardStatsVO getOverview() {
        DashboardStatsVO stats = new DashboardStatsVO();

        // 设备统计
        int totalDevices = Math.toIntExact(deviceMapper.selectCount(null));
        int onlineDevices = countOnlineDevices();
        stats.setTotalDevices(totalDevices);
        stats.setOnlineDevices(onlineDevices);
        stats.setOfflineDevices(totalDevices - onlineDevices);

        // 场景统计
        int totalScenes = Math.toIntExact(sceneMapper.selectCount(null));
        int enabledScenes = countEnabledScenes();
        stats.setTotalScenes(totalScenes);
        stats.setEnabledScenes(enabledScenes);

        // 内容统计
        stats.setTotalContents(Math.toIntExact(contentMapper.selectCount(null)));

        // 用户统计
        stats.setTotalUsers(Math.toIntExact(userMapper.selectCount(null)));

        // 今日日志统计
        LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LambdaQueryWrapper<OperationLog> logWrapper = new LambdaQueryWrapper<>();
        logWrapper.ge(OperationLog::getCreateTime, startOfDay);
        stats.setTodayLogs(Math.toIntExact(operationLogMapper.selectCount(logWrapper)));

        // 计算在线率
        if (totalDevices > 0) {
            double rate = (double) onlineDevices / totalDevices * 100;
            stats.setOnlineRate(BigDecimal.valueOf(rate).setScale(1, RoundingMode.HALF_UP).doubleValue());
        } else {
            stats.setOnlineRate(0.0);
        }

        return stats;
    }

    @Override
    public List<DeviceTrendVO> getDeviceTrend(Integer days) {
        List<DeviceTrendVO> result = new ArrayList<>();
        int actualDays = days != null ? days : 7;

        for (int i = actualDays - 1; i >= 0; i--) {
            LocalDate date = LocalDate.now().minusDays(i);
            LocalDateTime startOfDay = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endOfDay = LocalDateTime.of(date, LocalTime.MAX);

            // 统计该日期的设备总数
            DeviceTrendVO vo = new DeviceTrendVO();
            vo.setDate(date.format(DateTimeFormatter.ISO_LOCAL_DATE));

            // 由于没有历史快照，这里使用简化逻辑
            // 实际项目中应该有设备状态历史表
            vo.setDeviceCount(Math.toIntExact(deviceMapper.selectCount(null)));
            vo.setOnlineCount(countOnlineDevices());
            vo.setNewCount(0); // 简化处理

            result.add(vo);
        }

        return result;
    }

    @Override
    public List<UserActivityVO> getUserActivity(Integer days) {
        List<UserActivityVO> result = new ArrayList<>();
        int actualDays = days != null ? days : 7;

        for (int i = actualDays - 1; i >= 0; i--) {
            LocalDate date = LocalDate.now().minusDays(i);
            LocalDateTime startOfDay = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endOfDay = LocalDateTime.of(date, LocalTime.MAX);

            UserActivityVO vo = new UserActivityVO();
            vo.setDate(date.format(DateTimeFormatter.ISO_LOCAL_DATE));

            // 统计该日期的操作日志数
            LambdaQueryWrapper<OperationLog> logWrapper = new LambdaQueryWrapper<>();
            logWrapper.between(OperationLog::getCreateTime, startOfDay, endOfDay);
            vo.setOperationCount(Math.toIntExact(operationLogMapper.selectCount(logWrapper)));

            // 统计该日期的活跃用户数（有操作日志的用户）
            vo.setActiveUsers(0); // 简化处理
            vo.setNewUsers(0);

            result.add(vo);
        }

        return result;
    }

    @Override
    public List<Map<String, Object>> getSceneUsage() {
        // 统计各场景的触发次数（基于操作日志）
        List<Map<String, Object>> result = new ArrayList<>();

        // 获取所有场景
        List<Scene> scenes = sceneMapper.selectList(null);
        for (Scene scene : scenes) {
            Map<String, Object> stat = new HashMap<>();
            stat.put("sceneId", scene.getId());
            stat.put("sceneName", scene.getName());
            stat.put("triggerCount", 0); // 简化处理
            result.add(stat);
        }

        return result;
    }

    @Override
    public List<Map<String, Object>> getContentDistribution() {
        List<Map<String, Object>> result = new ArrayList<>();

        // 按类型分组统计内容
        Map<String, Long> typeCount = contentMapper.selectList(null).stream()
            .collect(Collectors.groupingBy(Content::getType, Collectors.counting()));

        for (Map.Entry<String, Long> entry : typeCount.entrySet()) {
            Map<String, Object> stat = new HashMap<>();
            stat.put("type", entry.getKey());
            stat.put("count", entry.getValue());
            result.add(stat);
        }

        return result;
    }

    @Override
    public Map<String, String> getSystemInfo() {
        Map<String, String> info = new LinkedHashMap<>();
        info.put("javaVersion", System.getProperty("java.version"));
        info.put("javaVendor", System.getProperty("java.vendor"));
        info.put("osName", System.getProperty("os.name"));
        info.put("osVersion", System.getProperty("os.version"));
        info.put("availableProcessors", String.valueOf(Runtime.getRuntime().availableProcessors()));
        info.put("maxMemory", String.valueOf(Runtime.getRuntime().maxMemory() / 1024 / 1024) + " MB");
        info.put("freeMemory", String.valueOf(Runtime.getRuntime().freeMemory() / 1024 / 1024) + " MB");
        info.put("totalMemory", String.valueOf(Runtime.getRuntime().totalMemory() / 1024 / 1024) + " MB");
        return info;
    }

    /**
     * 统计在线设备数量
     */
    private int countOnlineDevices() {
        LambdaQueryWrapper<Device> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Device::getIsOnline, 1);
        return Math.toIntExact(deviceMapper.selectCount(wrapper));
    }

    /**
     * 统计启用场景数量
     */
    private int countEnabledScenes() {
        LambdaQueryWrapper<Scene> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Scene::getIsEnabled, 1);
        return Math.toIntExact(sceneMapper.selectCount(wrapper));
    }
}
