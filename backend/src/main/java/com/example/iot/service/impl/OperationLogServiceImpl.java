package com.example.iot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.iot.common.exception.BusinessException;
import com.example.iot.common.result.ResultCode;
import com.example.iot.entity.OperationLog;
import com.example.iot.entity.User;
import com.example.iot.mapper.OperationLogMapper;
import com.example.iot.mapper.UserMapper;
import com.example.iot.service.OperationLogService;
import com.example.iot.vo.OperationLogVO;
import org.springframework.stereotype.Service;

/**
 * 操作日志服务实现类
 */
@Service
public class OperationLogServiceImpl implements OperationLogService {

    private final OperationLogMapper operationLogMapper;
    private final UserMapper userMapper;

    public OperationLogServiceImpl(OperationLogMapper operationLogMapper, UserMapper userMapper) {
        this.operationLogMapper = operationLogMapper;
        this.userMapper = userMapper;
    }

    @Override
    public void log(Long userId, String operation, String targetType, Long targetId, String ipAddress) {
        OperationLog logEntity = new OperationLog();
        logEntity.setUserId(userId);
        logEntity.setOperation(operation);
        logEntity.setTargetType(targetType);
        logEntity.setTargetId(targetId);
        logEntity.setIpAddress(ipAddress);

        operationLogMapper.insert(logEntity);
    }

    @Override
    public IPage<OperationLogVO> list(Integer page, Integer size, Long userId, String operation) {
        Page<OperationLog> logPage = new Page<>(page, size);
        QueryWrapper<OperationLog> wrapper = new QueryWrapper<>();

        // 基础条件：未删除
        wrapper.eq("deleted", 0);

        if (userId != null) {
            wrapper.eq("user_id", userId);
        }
        if (operation != null && !operation.isEmpty()) {
            wrapper.eq("operation", operation);
        }

        IPage<OperationLog> resultPage = operationLogMapper.selectPageWithWrapper(logPage, wrapper);
        return resultPage.convert(this::convertToVO);
    }

    @Override
    public OperationLogVO getById(Long id) {
        OperationLog log = operationLogMapper.selectById(id);
        if (log == null || log.getDeleted() == 1) {
            throw new BusinessException(ResultCode.LOG_NOT_FOUND);
        }
        return convertToVO(log);
    }

    /**
     * 实体转 VO
     */
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
