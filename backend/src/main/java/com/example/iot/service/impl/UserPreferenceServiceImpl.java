package com.example.iot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.iot.entity.UserPreference;
import com.example.iot.mapper.UserPreferenceMapper;
import com.example.iot.service.UserPreferenceService;
import com.example.iot.vo.UserPreferenceVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户偏好服务实现类
 */
@Service
public class UserPreferenceServiceImpl implements UserPreferenceService {

    private static final Logger log = LoggerFactory.getLogger(UserPreferenceServiceImpl.class);

    private final UserPreferenceMapper userPreferenceMapper;

    public UserPreferenceServiceImpl(UserPreferenceMapper userPreferenceMapper) {
        this.userPreferenceMapper = userPreferenceMapper;
    }

    @Override
    @Transactional
    public void setPreference(Long userId, String contentType, Integer score) {
        // 查询是否已存在
        UserPreference existing = userPreferenceMapper.selectByUserIdAndContentType(userId, contentType);

        if (existing != null) {
            // 更新偏好分数
            existing.setPreferenceScore(score);
            userPreferenceMapper.updateById(existing);
            log.info("用户偏好更新成功：userId={}, contentType={}, score={}", userId, contentType, score);
        } else {
            // 创建新偏好
            UserPreference preference = new UserPreference();
            preference.setUserId(userId);
            preference.setContentType(contentType);
            preference.setPreferenceScore(score);
            userPreferenceMapper.insert(preference);
            log.info("用户偏好设置成功：userId={}, contentType={}, score={}", userId, contentType, score);
        }
    }

    @Override
    @Transactional
    public void setPreference(Long userId, String contentType, Integer score, Integer clickWeight, Integer likeWeight, Integer dislikeWeight) {
        // 查询是否已存在
        UserPreference existing = userPreferenceMapper.selectByUserIdAndContentType(userId, contentType);

        // 使用默认值处理 null 权重
        int effectiveClickWeight = (clickWeight != null) ? clickWeight : 5;
        int effectiveLikeWeight = (likeWeight != null) ? likeWeight : 10;
        int effectiveDislikeWeight = (dislikeWeight != null) ? dislikeWeight : -20;

        if (existing != null) {
            // 更新偏好分数和权重
            existing.setPreferenceScore(score);
            existing.setClickWeight(effectiveClickWeight);
            existing.setLikeWeight(effectiveLikeWeight);
            existing.setDislikeWeight(effectiveDislikeWeight);
            userPreferenceMapper.updateById(existing);
            log.info("用户偏好更新成功（带权重）：userId={}, contentType={}, score={}, weights={}/{}/{}",
                userId, contentType, score, effectiveClickWeight, effectiveLikeWeight, effectiveDislikeWeight);
        } else {
            // 创建新偏好
            UserPreference preference = new UserPreference();
            preference.setUserId(userId);
            preference.setContentType(contentType);
            preference.setPreferenceScore(score);
            preference.setClickWeight(effectiveClickWeight);
            preference.setLikeWeight(effectiveLikeWeight);
            preference.setDislikeWeight(effectiveDislikeWeight);
            userPreferenceMapper.insert(preference);
            log.info("用户偏好设置成功（带权重）：userId={}, contentType={}, score={}, weights={}/{}/{}",
                userId, contentType, score, effectiveClickWeight, effectiveLikeWeight, effectiveDislikeWeight);
        }
    }

    @Override
    public List<UserPreferenceVO> getPreferences(Long userId) {
        List<UserPreference> preferences = userPreferenceMapper.selectByUserId(userId);
        return preferences.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    @Override
    public UserPreferenceVO getPreference(Long userId, String contentType) {
        UserPreference preference = userPreferenceMapper.selectByUserIdAndContentType(userId, contentType);
        if (preference == null) {
            return null;
        }
        return convertToVO(preference);
    }

    @Override
    @Transactional
    public void deletePreference(Long userId, String contentType) {
        UserPreference preference = userPreferenceMapper.selectByUserIdAndContentType(userId, contentType);
        if (preference != null) {
            userPreferenceMapper.deleteById(preference.getId());
            log.info("用户偏好删除成功：userId={}, contentType={}", userId, contentType);
        }
    }

    /**
     * 实体转 VO
     */
    private UserPreferenceVO convertToVO(UserPreference preference) {
        UserPreferenceVO vo = new UserPreferenceVO();
        vo.setId(preference.getId());
        vo.setUserId(preference.getUserId());
        vo.setContentType(preference.getContentType());
        vo.setPreferenceScore(preference.getPreferenceScore());
        vo.setClickWeight(preference.getClickWeight());
        vo.setLikeWeight(preference.getLikeWeight());
        vo.setDislikeWeight(preference.getDislikeWeight());
        vo.setCreateTime(preference.getCreateTime());
        vo.setUpdateTime(preference.getUpdateTime());
        return vo;
    }
}
