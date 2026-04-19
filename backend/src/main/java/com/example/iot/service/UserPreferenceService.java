package com.example.iot.service;

import com.example.iot.dto.UserPreferenceDTO;
import com.example.iot.vo.UserPreferenceVO;

import java.util.List;

/**
 * 用户偏好服务接口
 */
public interface UserPreferenceService {

    /**
     * 设置用户偏好
     *
     * @param userId 用户 ID
     * @param contentType 内容类型
     * @param score 偏好分数
     */
    void setPreference(Long userId, String contentType, Integer score);

    /**
     * 设置用户偏好（包含权重配置）
     *
     * @param userId 用户 ID
     * @param contentType 内容类型
     * @param score 偏好分数
     * @param clickWeight 点击行为权重 (nullable, defaults to 5)
     * @param likeWeight 收藏行为权重 (nullable, defaults to 10)
     * @param dislikeWeight 不喜欢行为权重 (nullable, defaults to -20)
     */
    void setPreference(Long userId, String contentType, Integer score, Integer clickWeight, Integer likeWeight, Integer dislikeWeight);

    /**
     * 获取用户偏好列表
     *
     * @param userId 用户 ID
     * @return 偏好列表
     */
    List<UserPreferenceVO> getPreferences(Long userId);

    /**
     * 获取用户指定类型的偏好
     *
     * @param userId 用户 ID
     * @param contentType 内容类型
     * @return 偏好信息
     */
    UserPreferenceVO getPreference(Long userId, String contentType);

    /**
     * 删除用户偏好
     *
     * @param userId 用户 ID
     * @param contentType 内容类型
     */
    void deletePreference(Long userId, String contentType);
}
