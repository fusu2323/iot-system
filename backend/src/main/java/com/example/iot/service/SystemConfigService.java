package com.example.iot.service;

/**
 * 系统配置服务接口
 */
public interface SystemConfigService {

    /**
     * 根据键获取配置值
     *
     * @param configKey 配置键
     * @return 配置值
     */
    String getValueByKey(String configKey);

    /**
     * 根据键获取配置值（带默认值）
     *
     * @param configKey 配置键
     * @param defaultValue 默认值
     * @return 配置值
     */
    String getValueByKey(String configKey, String defaultValue);

    /**
     * 设置配置值
     *
     * @param configKey 配置键
     * @param configValue 配置值
     * @param description 描述
     */
    void setValue(String configKey, String configValue, String description);

    /**
     * 获取所有系统配置
     *
     * @return 配置列表
     */
    java.util.List<com.example.iot.entity.SystemConfig> getAllConfigs();

    /**
     * 删除配置
     *
     * @param configKey 配置键
     */
    void deleteByKey(String configKey);
}
