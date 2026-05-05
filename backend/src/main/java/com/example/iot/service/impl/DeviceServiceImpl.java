package com.example.iot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.iot.common.exception.BusinessException;
import com.example.iot.common.result.ResultCode;
import com.example.iot.dto.DeviceCreateDTO;
import com.example.iot.dto.DeviceStatusUpdateDTO;
import com.example.iot.dto.DeviceUpdateDTO;
import com.example.iot.entity.Device;
import com.example.iot.mapper.DeviceMapper;
import com.example.iot.service.DeviceService;
import com.example.iot.service.OperationLogService;
import com.example.iot.service.StatLogService;
import com.example.iot.vo.DeviceStatisticsVO;
import com.example.iot.vo.DeviceVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 设备服务实现类
 */
@Service
public class DeviceServiceImpl implements DeviceService {

    private static final Logger log = LoggerFactory.getLogger(DeviceServiceImpl.class);

    private final DeviceMapper deviceMapper;
    private final OperationLogService operationLogService;
    private final StatLogService statLogService;

    public DeviceServiceImpl(DeviceMapper deviceMapper, OperationLogService operationLogService,
                             StatLogService statLogService) {
        this.deviceMapper = deviceMapper;
        this.operationLogService = operationLogService;
        this.statLogService = statLogService;
    }

    @Override
    public IPage<DeviceVO> list(Integer page, Integer size, String keyword, String type, String room,
                                 Integer status, Integer isOnline, Long userId) {
        Page<Device> devicePage = new Page<>(page, size);
        QueryWrapper<Device> wrapper = new QueryWrapper<>();

        // 基础条件：未删除
        wrapper.eq("deleted", 0);

        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like("name", keyword);
        }
        if (type != null && !type.isEmpty()) {
            wrapper.eq("type", type);
        }
        if (room != null && !room.isEmpty()) {
            wrapper.eq("room", room);
        }
        if (status != null) {
            wrapper.eq("status", status);
        }
        if (isOnline != null) {
            wrapper.eq("is_online", isOnline);
        }
        if (userId != null) {
            wrapper.eq("user_id", userId);
        }

        IPage<Device> resultPage = deviceMapper.selectPageWithWrapper(devicePage, wrapper);
        return resultPage.convert(this::convertToVO);
    }

    @Override
    public DeviceVO getById(Long id) {
        Device device = deviceMapper.selectById(id);
        if (device == null || device.getDeleted() == 1) {
            throw new BusinessException(ResultCode.DEVICE_NOT_FOUND);
        }
        return convertToVO(device);
    }

    @Override
    @Transactional
    public Long create(DeviceCreateDTO dto, Long userId) {
        Device device = new Device();
        device.setName(dto.getName());
        device.setType(dto.getType());
        device.setRoom(dto.getRoom());
        device.setStatus(dto.getStatus() ? 1 : 0);
        device.setIsOnline(0);
        device.setUserId(userId);

        deviceMapper.insert(device);

        log.info("设备创建成功：{}, userId={}", dto.getName(), userId);

        // 记录操作日志
        operationLogService.log(userId, "CREATE", "DEVICE", device.getId(), null);

        return device.getId();
    }

    @Override
    @Transactional
    public DeviceVO update(Long id, DeviceUpdateDTO dto) {
        Device device = deviceMapper.selectById(id);
        if (device == null || device.getDeleted() == 1) {
            throw new BusinessException(ResultCode.DEVICE_NOT_FOUND);
        }

        if (StringUtils.hasText(dto.getName())) {
            device.setName(dto.getName());
        }
        if (dto.getRoom() != null) {
            device.setRoom(dto.getRoom());
        }
        if (dto.getType() != null) {
            device.setType(dto.getType());
        }
        if (dto.getStatus() != null) {
            device.setStatus(dto.getStatus() ? 1 : 0);
        }

        deviceMapper.updateById(device);

        log.info("设备信息更新成功：{}", id);

        // 记录操作日志
        operationLogService.log(device.getUserId(), "UPDATE", "DEVICE", id, null);

        return convertToVO(device);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Device device = deviceMapper.selectById(id);
        if (device == null || device.getDeleted() == 1) {
            throw new BusinessException(ResultCode.DEVICE_NOT_FOUND);
        }

        deviceMapper.deleteById(id);

        log.info("设备删除成功：{}", id);

        // 记录操作日志
        operationLogService.log(device.getUserId(), "DELETE", "DEVICE", id, null);
    }

    @Override
    @Transactional
    public DeviceVO updateStatus(Long id, DeviceStatusUpdateDTO dto) {
        Device device = deviceMapper.selectById(id);
        if (device == null || device.getDeleted() == 1) {
            throw new BusinessException(ResultCode.DEVICE_NOT_FOUND);
        }

        if (dto.getStatus() != null) {
            device.setStatus(dto.getStatus());
        }
        if (dto.getIsOnline() != null) {
            device.setIsOnline(dto.getIsOnline());
        }

        deviceMapper.updateById(device);

        log.info("设备状态更新成功：{}, status={}, isOnline={}", id, device.getStatus(), device.getIsOnline());

        // 记录操作日志
        operationLogService.log(device.getUserId(), "UPDATE_STATUS", "DEVICE", id, null);

        // STATS-01: 设备status变为1时，写入激活记录
        if (dto.getStatus() != null && dto.getStatus() == 1 && device.getUserId() != null) {
            statLogService.log(device.getUserId(), "DEVICE", id, "ACTIVATE");
        }

        return convertToVO(device);
    }

    @Override
    public List<DeviceStatisticsVO> countByType() {
        List<Map<String, Object>> results = deviceMapper.countByType();
        return results.stream().map(this::mapToStatistics).collect(Collectors.toList());
    }

    @Override
    public List<DeviceStatisticsVO> countByRoom() {
        List<Map<String, Object>> results = deviceMapper.countByRoom();
        return results.stream().map(this::mapToStatistics).collect(Collectors.toList());
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
     * 实体转 VO
     */
    private DeviceVO convertToVO(Device device) {
        DeviceVO vo = new DeviceVO();
        vo.setId(device.getId());
        vo.setName(device.getName());
        vo.setType(device.getType());
        vo.setRoom(device.getRoom());
        vo.setStatus(device.getStatus());
        vo.setIsOnline(device.getIsOnline());
        vo.setUserId(device.getUserId());
        vo.setCreateTime(device.getCreateTime());
        vo.setUpdateTime(device.getUpdateTime());
        return vo;
    }

    /**
     * Map 转 StatisticsVO
     */
    private DeviceStatisticsVO mapToStatistics(Map<String, Object> map) {
        DeviceStatisticsVO vo = new DeviceStatisticsVO();
        vo.setName((String) map.get("name"));
        vo.setCount(((Number) map.get("count")).intValue());
        return vo;
    }
}
