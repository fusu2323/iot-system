package com.example.iot.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.iot.dto.RecommendationFeedbackDTO;
import com.example.iot.vo.GroupedRecommendationResponse;
import com.example.iot.vo.RecommendationFeedbackVO;
import com.example.iot.vo.RecommendationVO;

import java.util.List;

/**
 * 推荐服务接口
 */
public interface RecommendationService {

    /**
     * 获取推荐列表（基于用户偏好，支持按类型筛选和分组返回）
     *
     * @param userId 用户 ID
     * @param type 内容类型筛选（可选，如 MOVIE, MUSIC, GAME）
     * @param page 页码
     * @param size 每页大小
     * @return 分组推荐响应
     */
    GroupedRecommendationResponse getRecommendations(Long userId, String type, Integer page, Integer size);

    /**
     * 记录推荐
     *
     * @param userId 用户 ID
     * @param contentId 内容 ID
     * @param reason 推荐原因
     * @param score 推荐分数
     */
    void recordRecommend(Long userId, Long contentId, String reason, Integer score);

    /**
     * 记录推荐点击
     *
     * @param userId 用户 ID
     * @param contentId 内容 ID
     */
    void recordClick(Long userId, Long contentId);

    /**
     * 记录推荐收藏
     *
     * @param userId 用户 ID
     * @param contentId 内容 ID
     */
    void recordLike(Long userId, Long contentId);

    /**
     * 记录不喜欢
     *
     * @param userId 用户 ID
     * @param contentId 内容 ID
     */
    void recordDislike(Long userId, Long contentId);

    /**
     * 提交推荐反馈
     *
     * @param dto 反馈请求
     */
    void submitFeedback(RecommendationFeedbackDTO dto);

    /**
     * 获取用户反馈历史
     *
     * @param userId 用户 ID
     * @param page 页码
     * @param size 每页大小
     * @return 反馈列表
     */
    IPage<RecommendationFeedbackVO> getUserFeedbackHistory(Long userId, Integer page, Integer size);

    /**
     * 更新用户偏好分数（基于反馈）
     *
     * @param userId 用户 ID
     * @param contentType 内容类型
     * @param deltaScore 分数变化
     */
    void updateUserPreferenceScore(Long userId, String contentType, int deltaScore);
}
