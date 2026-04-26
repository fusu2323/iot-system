package com.example.iot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.iot.entity.OperationLog;
import com.example.iot.entity.User;
import com.example.iot.mapper.OperationLogMapper;
import com.example.iot.mapper.UserMapper;
import com.example.iot.service.LogService;
import com.example.iot.vo.OperationLogVO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 日志管理服务实现类
 */
@Service
public class LogServiceImpl implements LogService {

    private final OperationLogMapper operationLogMapper;
    private final UserMapper userMapper;

    public LogServiceImpl(OperationLogMapper operationLogMapper, UserMapper userMapper) {
        this.operationLogMapper = operationLogMapper;
        this.userMapper = userMapper;
    }

    @Override
    public IPage<OperationLogVO> list(Integer page, Integer size, Long userId, String operation,
                                       String targetType, String startDate, String endDate) {
        int pageNum = page != null ? page : 1;
        int pageSize = size != null ? size : 10;

        Page<OperationLog> pager = new Page<>(pageNum, pageSize);

        LambdaQueryWrapper<OperationLog> wrapper = new LambdaQueryWrapper<>();

        // 用户 ID 筛选
        if (userId != null) {
            wrapper.eq(OperationLog::getUserId, userId);
        }

        // 操作类型筛选
        if (operation != null && !operation.isEmpty()) {
            wrapper.eq(OperationLog::getOperation, operation);
        }

        // 目标类型筛选
        if (targetType != null && !targetType.isEmpty()) {
            wrapper.eq(OperationLog::getTargetType, targetType);
        }

        // 日期范围筛选
        if (startDate != null && !startDate.isEmpty()) {
            LocalDateTime startDateTime = LocalDate.parse(startDate, DateTimeFormatter.ISO_LOCAL_DATE)
                    .atTime(LocalTime.MIN);
            wrapper.ge(OperationLog::getCreateTime, startDateTime);
        }
        if (endDate != null && !endDate.isEmpty()) {
            LocalDateTime endDateTime = LocalDate.parse(endDate, DateTimeFormatter.ISO_LOCAL_DATE)
                    .atTime(LocalTime.MAX);
            wrapper.le(OperationLog::getCreateTime, endDateTime);
        }

        // 按创建时间降序
        wrapper.orderByDesc(OperationLog::getCreateTime);

        IPage<OperationLog> pageResult = operationLogMapper.selectPage(pager, wrapper);

        // 转换为 VO
        List<OperationLogVO> voList = pageResult.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        IPage<OperationLogVO> voPage = new Page<>(pageNum, pageSize, pageResult.getTotal());
        voPage.setRecords(voList);
        return voPage;
    }

    @Override
    public OperationLogVO getById(Long id) {
        OperationLog log = operationLogMapper.selectById(id);
        if (log == null) {
            return null;
        }
        return convertToVO(log);
    }

    @Override
    public List<Map<String, Object>> countByOperation() {
        List<OperationLog> allLogs = operationLogMapper.selectList(null);

        // 按操作类型分组统计
        Map<String, Long> countMap = allLogs.stream()
                .collect(Collectors.groupingBy(OperationLog::getOperation, Collectors.counting()));

        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<String, Long> entry : countMap.entrySet()) {
            Map<String, Object> stat = new HashMap<>();
            stat.put("operation", entry.getKey());
            stat.put("count", entry.getValue());
            result.add(stat);
        }

        // 按数量降序排序
        result.sort((a, b) -> Long.compare((Long) b.get("count"), (Long) a.get("count")));
        return result;
    }

    @Override
    public List<Map<String, Object>> countByDay(Integer days) {
        List<Map<String, Object>> result = new ArrayList<>();
        int actualDays = days != null ? days : 7;

        for (int i = actualDays - 1; i >= 0; i--) {
            LocalDate date = LocalDate.now().minusDays(i);
            LocalDateTime startOfDay = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endOfDay = LocalDateTime.of(date, LocalTime.MAX);

            LambdaQueryWrapper<OperationLog> wrapper = new LambdaQueryWrapper<>();
            wrapper.between(OperationLog::getCreateTime, startOfDay, endOfDay);
            Long count = operationLogMapper.selectCount(wrapper);

            Map<String, Object> stat = new HashMap<>();
            stat.put("date", date.format(DateTimeFormatter.ISO_LOCAL_DATE));
            stat.put("count", count);
            result.add(stat);
        }

        return result;
    }

    private OperationLogVO convertToVO(OperationLog log) {
        OperationLogVO vo = new OperationLogVO();
        vo.setId(log.getId());
        vo.setUserId(log.getUserId());
        vo.setOperation(log.getOperation());
        vo.setTargetType(log.getTargetType());
        vo.setTargetId(log.getTargetId());
        vo.setIpAddress(log.getIpAddress());
        vo.setCreateTime(log.getCreateTime());

        // 查询用户名
        if (log.getUserId() != null) {
            User user = userMapper.selectById(log.getUserId());
            if (user != null) {
                vo.setUsername(user.getUsername());
            }
        }

        return vo;
    }
}
