package com.example.iot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.iot.common.exception.BusinessException;
import com.example.iot.common.result.ResultCode;
import com.example.iot.dto.SceneCreateDTO;
import com.example.iot.dto.SceneUpdateDTO;
import com.example.iot.entity.Device;
import com.example.iot.entity.Scene;
import com.example.iot.entity.SceneDevice;
import com.example.iot.mapper.DeviceMapper;
import com.example.iot.mapper.SceneDeviceMapper;
import com.example.iot.mapper.SceneMapper;
import com.example.iot.service.OperationLogService;
import com.example.iot.service.SceneService;
import com.example.iot.vo.SceneDeviceVO;
import com.example.iot.vo.SceneVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 场景服务实现类
 */
@Service
public class SceneServiceImpl implements SceneService {

    private static final Logger log = LoggerFactory.getLogger(SceneServiceImpl.class);

    private final SceneMapper sceneMapper;
    private final SceneDeviceMapper sceneDeviceMapper;
    private final DeviceMapper deviceMapper;
    private final OperationLogService operationLogService;

    public SceneServiceImpl(SceneMapper sceneMapper, SceneDeviceMapper sceneDeviceMapper,
                           DeviceMapper deviceMapper, OperationLogService operationLogService) {
        this.sceneMapper = sceneMapper;
        this.sceneDeviceMapper = sceneDeviceMapper;
        this.deviceMapper = deviceMapper;
        this.operationLogService = operationLogService;
    }

    @Override
    public IPage<SceneVO> list(Integer page, Integer size, Long userId) {
        Page<Scene> scenePage = new Page<>(page, size);
        LambdaQueryWrapper<Scene> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Scene::getDeleted, 0);
        if (userId != null) {
            wrapper.eq(Scene::getUserId, userId);
        }
        wrapper.orderByDesc(Scene::getCreateTime);

        IPage<Scene> resultPage = sceneMapper.selectPage(scenePage, wrapper);
        return resultPage.convert(scene -> {
            SceneVO vo = convertToVO(scene);
            vo.setDevices(getDeviceVOs(scene.getId()));
            return vo;
        });
    }

    @Override
    public SceneVO getById(Long id) {
        Scene scene = sceneMapper.selectById(id);
        if (scene == null || scene.getDeleted() == 1) {
            throw new BusinessException(ResultCode.SCENE_NOT_FOUND);
        }
        SceneVO vo = convertToVO(scene);
        vo.setDevices(getDeviceVOs(id));
        return vo;
    }

    @Override
    @Transactional
    public Long create(SceneCreateDTO dto, Long userId) {
        Scene scene = new Scene();
        scene.setName(dto.getName());
        scene.setDescription(dto.getDescription());
        scene.setIcon(dto.getIcon());
        scene.setMutexGroup(dto.getMutexGroup());
        scene.setIsEnabled(1);
        scene.setUserId(userId);

        sceneMapper.insert(scene);

        log.info("场景创建成功：{}, userId={}", dto.getName(), userId);

        // 记录操作日志
        operationLogService.log(userId, "CREATE", "SCENE", scene.getId(), null);

        return scene.getId();
    }

    @Override
    @Transactional
    public SceneVO update(Long id, SceneUpdateDTO dto) {
        Scene scene = sceneMapper.selectById(id);
        if (scene == null || scene.getDeleted() == 1) {
            throw new BusinessException(ResultCode.SCENE_NOT_FOUND);
        }

        if (StringUtils.hasText(dto.getName())) {
            scene.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            scene.setDescription(dto.getDescription());
        }
        if (dto.getIcon() != null) {
            scene.setIcon(dto.getIcon());
        }
        if (dto.getMutexGroup() != null) {
            scene.setMutexGroup(dto.getMutexGroup());
        }

        sceneMapper.updateById(scene);

        log.info("场景信息更新成功：{}", id);

        // 记录操作日志
        operationLogService.log(scene.getUserId(), "UPDATE", "SCENE", id, null);

        return convertToVO(scene);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Scene scene = sceneMapper.selectById(id);
        if (scene == null || scene.getDeleted() == 1) {
            throw new BusinessException(ResultCode.SCENE_NOT_FOUND);
        }

        // 删除场景设备关联
        LambdaQueryWrapper<SceneDevice> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SceneDevice::getSceneId, id);
        sceneDeviceMapper.delete(wrapper);

        // 删除场景
        sceneMapper.deleteById(id);

        log.info("场景删除成功：{}", id);

        // 记录操作日志
        operationLogService.log(scene.getUserId(), "DELETE", "SCENE", id, null);
    }

    @Override
    @Transactional
    public SceneVO toggle(Long id) {
        Scene scene = sceneMapper.selectById(id);
        if (scene == null || scene.getDeleted() == 1) {
            throw new BusinessException(ResultCode.SCENE_NOT_FOUND);
        }

        int currentEnabled = scene.getIsEnabled() != null ? scene.getIsEnabled() : 0;
        int newEnabled = (currentEnabled == 1) ? 0 : 1;
        scene.setIsEnabled(newEnabled);

        // If enabling and has a mutex group, auto-disable other scenes in same group
        if (newEnabled == 1 && scene.getMutexGroup() != null && scene.getUserId() != null) {
            LambdaQueryWrapper<Scene> mutexWrapper = new LambdaQueryWrapper<>();
            mutexWrapper.eq(Scene::getUserId, scene.getUserId())
                   .eq(Scene::getMutexGroup, scene.getMutexGroup())
                   .eq(Scene::getIsEnabled, 1)
                   .ne(Scene::getId, id)
                   .eq(Scene::getDeleted, 0);
            List<Scene> sameGroupEnabled = sceneMapper.selectList(mutexWrapper);
            for (Scene s : sameGroupEnabled) {
                s.setIsEnabled(0);
                sceneMapper.updateById(s);
                log.info("mutex disabled: sceneId={}, mutexGroup={}", s.getId(), scene.getMutexGroup());
            }
        }

        sceneMapper.updateById(scene);

        // Sync linked devices to match scene state
        syncDevices(id, newEnabled);

        log.info("scene toggled: {}, isEnabled={}", id, scene.getIsEnabled());

        // 记录操作日志
        operationLogService.log(scene.getUserId(), "TOGGLE", "SCENE", id, null);

        return convertToVO(scene);
    }

    @Override
    @Transactional
    public void trigger(Long id) {
        Scene scene = sceneMapper.selectById(id);
        if (scene == null || scene.getDeleted() == 1) {
            throw new BusinessException(ResultCode.SCENE_NOT_FOUND);
        }

        if (scene.getIsEnabled() == 0) {
            throw new BusinessException(ResultCode.SCENE_DEVICE_NOT_FOUND.getCode(), "场景已禁用，无法触发");
        }

        // Sync all linked devices to enabled status
        syncDevices(id, 1);

        log.info("场景触发成功：{}", id);

        // 记录操作日志
        operationLogService.log(scene.getUserId(), "TRIGGER", "SCENE", id, null);
    }

    @Override
    @Transactional
    public void addDevice(Long sceneId, Long deviceId, String config) {
        Scene scene = sceneMapper.selectById(sceneId);
        if (scene == null || scene.getDeleted() == 1) {
            throw new BusinessException(ResultCode.SCENE_NOT_FOUND);
        }

        Device device = deviceMapper.selectById(deviceId);
        if (device == null || device.getDeleted() == 1) {
            throw new BusinessException(ResultCode.DEVICE_NOT_FOUND);
        }

        // 检查是否已关联
        LambdaQueryWrapper<SceneDevice> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SceneDevice::getSceneId, sceneId)
               .eq(SceneDevice::getDeviceId, deviceId);
        long count = sceneDeviceMapper.selectCount(wrapper);
        if (count > 0) {
            throw new BusinessException("设备已关联到该场景");
        }

        SceneDevice sceneDevice = new SceneDevice();
        sceneDevice.setSceneId(sceneId);
        sceneDevice.setDeviceId(deviceId);
        sceneDevice.setConfig(config);

        sceneDeviceMapper.insert(sceneDevice);

        log.info("场景设备关联添加成功：sceneId={}, deviceId={}", sceneId, deviceId);
    }

    @Override
    @Transactional
    public void removeDevice(Long sceneId, Long deviceId) {
        LambdaQueryWrapper<SceneDevice> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SceneDevice::getSceneId, sceneId)
               .eq(SceneDevice::getDeviceId, deviceId);
        sceneDeviceMapper.delete(wrapper);

        log.info("场景设备关联删除成功：sceneId={}, deviceId={}", sceneId, deviceId);
    }

    @Override
    public List<Long> getDeviceIds(Long sceneId) {
        LambdaQueryWrapper<SceneDevice> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SceneDevice::getSceneId, sceneId);
        List<SceneDevice> sceneDevices = sceneDeviceMapper.selectList(wrapper);
        return sceneDevices.stream().map(SceneDevice::getDeviceId).collect(Collectors.toList());
    }

    /**
     * Sync all linked devices to target status when scene is enabled/disabled.
     * @param sceneId the scene ID
     * @param targetStatus 1=enabled, 0=disabled
     */
    private void syncDevices(Long sceneId, Integer targetStatus) {
        LambdaQueryWrapper<SceneDevice> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SceneDevice::getSceneId, sceneId);
        List<SceneDevice> sceneDevices = sceneDeviceMapper.selectList(wrapper);
        for (SceneDevice sd : sceneDevices) {
            Device device = deviceMapper.selectById(sd.getDeviceId());
            if (device != null && device.getDeleted() == 0) {
                device.setStatus(targetStatus);
                deviceMapper.updateById(device);
                log.info("device sync: deviceId={}, sceneId={}, status={}", device.getId(), sceneId, targetStatus);
            }
        }
    }

    /**
     * 实体转 VO
     */
    private SceneVO convertToVO(Scene scene) {
        SceneVO vo = new SceneVO();
        vo.setId(scene.getId());
        vo.setName(scene.getName());
        vo.setDescription(scene.getDescription());
        vo.setIcon(scene.getIcon());
        vo.setMutexGroup(scene.getMutexGroup());
        vo.setIsEnabled(scene.getIsEnabled());
        vo.setUserId(scene.getUserId());
        vo.setCreateTime(scene.getCreateTime());
        vo.setUpdateTime(scene.getUpdateTime());

        // populate activeGroupSceneId for mutex group
        if (scene.getMutexGroup() != null && scene.getUserId() != null) {
            LambdaQueryWrapper<Scene> activeWrapper = new LambdaQueryWrapper<>();
            activeWrapper.eq(Scene::getUserId, scene.getUserId())
                         .eq(Scene::getMutexGroup, scene.getMutexGroup())
                         .eq(Scene::getIsEnabled, 1)
                         .eq(Scene::getDeleted, 0);
            activeWrapper.last("LIMIT 1");
            List<Scene> activeScenes = sceneMapper.selectList(activeWrapper);
            if (!activeScenes.isEmpty()) {
                vo.setActiveGroupSceneId(activeScenes.get(0).getId());
            }
        }

        return vo;
    }

    /**
     * 获取场景设备列表
     */
    private List<SceneDeviceVO> getDeviceVOs(Long sceneId) {
        LambdaQueryWrapper<SceneDevice> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SceneDevice::getSceneId, sceneId);
        List<SceneDevice> sceneDevices = sceneDeviceMapper.selectList(wrapper);

        List<SceneDeviceVO> result = new ArrayList<>();
        for (SceneDevice sd : sceneDevices) {
            Device device = deviceMapper.selectById(sd.getDeviceId());
            SceneDeviceVO vo = new SceneDeviceVO();
            vo.setId(sd.getId());
            vo.setSceneId(sd.getSceneId());
            vo.setDeviceId(sd.getDeviceId());
            vo.setDeviceName(device != null ? device.getName() : null);
            vo.setConfig(sd.getConfig());
            vo.setCreateTime(sd.getCreateTime());
            vo.setUpdateTime(sd.getUpdateTime());
            result.add(vo);
        }
        return result;
    }
}
