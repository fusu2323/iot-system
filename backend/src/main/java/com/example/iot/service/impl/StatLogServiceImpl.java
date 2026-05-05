package com.example.iot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.iot.entity.Device;
import com.example.iot.entity.Scene;
import com.example.iot.entity.StatLog;
import com.example.iot.mapper.DeviceMapper;
import com.example.iot.mapper.SceneMapper;
import com.example.iot.mapper.StatLogMapper;
import com.example.iot.service.StatLogService;
import com.example.iot.vo.UsageRankingVO;
import com.example.iot.vo.UsageStatsVO;
import com.example.iot.vo.UsageTimelineVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 统计日志服务实现类
 */
@Service
public class StatLogServiceImpl implements StatLogService {

    private static final Logger log = LoggerFactory.getLogger(StatLogServiceImpl.class);

    private final StatLogMapper statLogMapper;
    private final DeviceMapper deviceMapper;
    private final SceneMapper sceneMapper;

    public StatLogServiceImpl(StatLogMapper statLogMapper,
                               DeviceMapper deviceMapper,
                               SceneMapper sceneMapper) {
        this.statLogMapper = statLogMapper;
        this.deviceMapper = deviceMapper;
        this.sceneMapper = sceneMapper;
    }

    @Override
    public void log(Long userId, String targetType, Long targetId, String action) {
        StatLog record = new StatLog();
        record.setUserId(userId);
        record.setTargetType(targetType);
        record.setTargetId(targetId);
        record.setAction(action);
        record.setStatDate(LocalDate.now());
        statLogMapper.insert(record);
        log.info("stat logged: userId={}, targetType={}, targetId={}, action={}",
                 userId, targetType, targetId, action);
    }

    @Override
    public UsageStatsVO getMyStatistics(Long userId, String dimension, String targetType,
                                        String startDate, String endDate) {
        List<Map<String, Object>> rawStats = statLogMapper.selectStatsByUserAndDimension(
                userId, dimension, targetType, startDate, endDate);

        List<UsageStatsVO.StatItem> items = new ArrayList<>();
        for (Map<String, Object> row : rawStats) {
            Object key = row.get("stat_key");
            Object count = row.get("count");
            items.add(new UsageStatsVO.StatItem(
                    key != null ? key.toString() : "",
                    count != null ? ((Number) count).longValue() : 0L
            ));
        }

        UsageStatsVO vo = new UsageStatsVO();
        vo.setTargetType(targetType != null ? targetType : "ALL");
        vo.setDimension(dimension != null ? dimension : "DAY");
        vo.setStatistics(items);
        return vo;
    }

    @Override
    public UsageRankingVO getGlobalRanking(String targetType, Integer limit) {
        List<Map<String, Object>> raw = statLogMapper.selectGlobalRanking(targetType, limit);

        List<UsageRankingVO.RankItem> ranks = new ArrayList<>();
        for (Map<String, Object> row : raw) {
            Long targetId = ((Number) row.get("target_id")).longValue();
            String type = (String) row.get("target_type");
            Long count = ((Number) row.get("count")).longValue();
            String name = resolveTargetName(type, targetId);
            ranks.add(new UsageRankingVO.RankItem(targetId, name, type, count));
        }

        UsageRankingVO vo = new UsageRankingVO();
        vo.setRankings(ranks);
        return vo;
    }

    @Override
    public UsageTimelineVO getTimeline(Long userId, String targetType, Long targetId,
                                        Integer page, Integer size) {
        Page<StatLog> pageParam = new Page<>(page != null ? page : 1, size != null ? size : 10);
        IPage<StatLog> result = statLogMapper.selectTimeline(pageParam, targetType, targetId);

        List<UsageTimelineVO.TimelineItem> items = new ArrayList<>();
        for (StatLog record : result.getRecords()) {
            items.add(new UsageTimelineVO.TimelineItem(record.getCreateTime(), record.getAction()));
        }

        UsageTimelineVO vo = new UsageTimelineVO();
        vo.setTargetType(targetType);
        vo.setTargetId(targetId);
        vo.setTargetName(resolveTargetName(targetType, targetId));
        vo.setTimeline(items);
        return vo;
    }

    private String resolveTargetName(String targetType, Long targetId) {
        if ("DEVICE".equals(targetType)) {
            Device device = deviceMapper.selectById(targetId);
            return device != null ? device.getName() : "未知设备";
        } else if ("SCENE".equals(targetType)) {
            Scene scene = sceneMapper.selectById(targetId);
            return scene != null ? scene.getName() : "未知场景";
        }
        return "未知";
    }
}
