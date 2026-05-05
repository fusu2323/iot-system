package com.example.iot.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.iot.dto.SceneCreateDTO;
import com.example.iot.dto.SceneUpdateDTO;
import com.example.iot.vo.SceneVO;

import java.util.List;

/**
 * 场景服务接口
 */
public interface SceneService {

    /**
     * 分页查询场景列表
     *
     * @param page 页码
     * @param size 每页大小
     * @param userId 用户 ID
     * @return 场景列表
     */
    IPage<SceneVO> list(Integer page, Integer size, Long userId);

    /**
     * 根据 ID 查询场景详情（含关联设备）
     *
     * @param id 场景 ID
     * @return 场景信息
     */
    SceneVO getById(Long id);

    /**
     * 创建场景
     *
     * @param dto 创建请求
     * @param userId 用户 ID
     * @return 场景 ID
     */
    Long create(SceneCreateDTO dto, Long userId);

    /**
     * 更新场景信息
     *
     * @param id 场景 ID
     * @param dto 更新请求
     * @return 场景信息
     */
    SceneVO update(Long id, SceneUpdateDTO dto);

    /**
     * 删除场景
     *
     * @param id 场景 ID
     */
    void delete(Long id);

    /**
     * 启用/禁用场景
     *
     * @param id 场景 ID
     * @return 场景信息
     */
    SceneVO toggle(Long id);

    /**
     * 触发场景
     *
     * @param id 场景 ID
     */
    void trigger(Long id);

    /**
     * 添加场景设备关联
     *
     * @param sceneId 场景 ID
     * @param deviceId 设备 ID
     * @param config 设备配置
     * @param targetStatus 目标状态：0-禁用，1-启用
     */
    void addDevice(Long sceneId, Long deviceId, String config, Integer targetStatus);

    /**
     * 移除场景设备关联
     *
     * @param sceneId 场景 ID
     * @param deviceId 设备 ID
     */
    void removeDevice(Long sceneId, Long deviceId);

    /**
     * 获取场景设备列表
     *
     * @param sceneId 场景 ID
     * @return 设备列表
     */
    List<Long> getDeviceIds(Long sceneId);
}
