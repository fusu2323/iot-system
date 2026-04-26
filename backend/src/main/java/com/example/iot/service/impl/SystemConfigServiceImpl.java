package com.example.iot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.iot.entity.SystemConfig;
import com.example.iot.mapper.SystemConfigMapper;
import com.example.iot.service.SystemConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 系统配置服务实现类
 */
@Service
public class SystemConfigServiceImpl implements SystemConfigService {

    private static final Logger log = LoggerFactory.getLogger(SystemConfigServiceImpl.class);

    private final SystemConfigMapper systemConfigMapper;

    public SystemConfigServiceImpl(SystemConfigMapper systemConfigMapper) {
        this.systemConfigMapper = systemConfigMapper;
    }

    @Override
    public String getValueByKey(String configKey) {
        return getValueByKey(configKey, null);
    }

    @Override
    public String getValueByKey(String configKey, String defaultValue) {
        LambdaQueryWrapper<SystemConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SystemConfig::getConfigKey, configKey);
        SystemConfig config = systemConfigMapper.selectOne(wrapper);

        if (config == null) {
            return defaultValue;
        }
        return config.getConfigValue();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setValue(String configKey, String configValue, String description) {
        LambdaQueryWrapper<SystemConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SystemConfig::getConfigKey, configKey);
        SystemConfig existingConfig = systemConfigMapper.selectOne(wrapper);

        if (existingConfig != null) {
            // 更新现有配置
            existingConfig.setConfigValue(configValue);
            existingConfig.setDescription(description);
            systemConfigMapper.updateById(existingConfig);
            log.info("更新系统配置：{} = {}", configKey, configValue);
        } else {
            // 插入新配置
            SystemConfig config = new SystemConfig();
            config.setConfigKey(configKey);
            config.setConfigValue(configValue);
            config.setDescription(description);
            config.setConfigType("SYSTEM");
            systemConfigMapper.insert(config);
            log.info("新增系统配置：{} = {}", configKey, configValue);
        }
    }

    @Override
    public List<SystemConfig> getAllConfigs() {
        LambdaQueryWrapper<SystemConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(SystemConfig::getConfigKey);
        return systemConfigMapper.selectList(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByKey(String configKey) {
        LambdaQueryWrapper<SystemConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SystemConfig::getConfigKey, configKey);
        systemConfigMapper.delete(wrapper);
        log.info("删除系统配置：{}", configKey);
    }
}
