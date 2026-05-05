package com.example.iot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.iot.dto.RecommendationFeedbackDTO;
import com.example.iot.entity.Content;
import com.example.iot.entity.Recommendation;
import com.example.iot.entity.RecommendationFeedback;
import com.example.iot.entity.UserPreference;
import com.example.iot.mapper.ContentMapper;
import com.example.iot.mapper.RecommendationFeedbackMapper;
import com.example.iot.mapper.RecommendationMapper;
import com.example.iot.mapper.UserPreferenceMapper;
import com.example.iot.service.RecommendationService;
import com.example.iot.vo.GroupedRecommendationResponse;
import com.example.iot.vo.GroupedRecommendationVO;
import com.example.iot.vo.RecommendationFeedbackVO;
import com.example.iot.vo.RecommendationVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 推荐服务实现类
 */
@Service
public class RecommendationServiceImpl implements RecommendationService {

    private static final Logger log = LoggerFactory.getLogger(RecommendationServiceImpl.class);

    private final RecommendationMapper recommendationMapper;
    private final RecommendationFeedbackMapper feedbackMapper;
    private final UserPreferenceMapper userPreferenceMapper;
    private final ContentMapper contentMapper;

    public RecommendationServiceImpl(RecommendationMapper recommendationMapper,
                                     RecommendationFeedbackMapper feedbackMapper,
                                     UserPreferenceMapper userPreferenceMapper,
                                     ContentMapper contentMapper) {
        this.recommendationMapper = recommendationMapper;
        this.feedbackMapper = feedbackMapper;
        this.userPreferenceMapper = userPreferenceMapper;
        this.contentMapper = contentMapper;
    }

    @Override
    public GroupedRecommendationResponse getRecommendations(Long userId, String type, Integer page, Integer size) {
        log.info("为用户 {} 生成推荐列表，type={}, page={}, size={}", userId, type, page, size);

        // 1. 获取用户偏好
        List<UserPreference> preferences = userPreferenceMapper.selectByUserId(userId);
        Map<String, Integer> preferenceMap = new HashMap<>();
        if (preferences != null && !preferences.isEmpty()) {
            preferenceMap = preferences.stream()
                .collect(Collectors.toMap(
                    UserPreference::getContentType,
                    UserPreference::getPreferenceScore,
                    (existing, replacement) -> existing
                ));
        }

        // 2. 获取用户已反馈的内容 ID 列表（避免重复推荐）
        List<Long> excludedContentIds = getExcludedContentIds(userId);

        // 3. 获取所有内容（支持类型筛选）
        List<Content> allContents;
        if (StringUtils.hasText(type)) {
            LambdaQueryWrapper<Content> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Content::getType, type);
            allContents = contentMapper.selectList(wrapper);
        } else {
            allContents = contentMapper.selectList(null);
        }
        if (allContents == null || allContents.isEmpty()) {
            return buildEmptyResponse();
        }

        // 4. 计算每个内容的推荐分数
        List<ContentScore> contentScores = new ArrayList<>();
        for (Content content : allContents) {
            if (excludedContentIds.contains(content.getId())) {
                continue;
            }

            int score = calculateRecommendationScore(content, preferenceMap, userId);
            contentScores.add(new ContentScore(content, score));
        }

        // 5. 按推荐分数排序
        contentScores.sort((a, b) -> b.score - a.score);

        // 6. 按类型分组
        Map<String, List<ContentScore>> groupedByType = contentScores.stream()
            .collect(Collectors.groupingBy(cs -> cs.content.getType(), LinkedHashMap::new, Collectors.toList()));

        // 确保所有三种类型都存在
        String[] contentTypes = {"MOVIE", "MUSIC", "GAME"};
        for (String contentType : contentTypes) {
            groupedByType.putIfAbsent(contentType, new ArrayList<>());
        }

        // 7. 构建分组响应
        Map<String, GroupedRecommendationVO> groups = new LinkedHashMap<>();
        int totalCount = 0;

        for (Map.Entry<String, List<ContentScore>> entry : groupedByType.entrySet()) {
            String typeKey = entry.getKey();
            List<ContentScore> typeScores = entry.getValue();

            // 该类型的总数
            int typeTotal = typeScores.size();
            totalCount += typeTotal;

            // 该类型的分页
            int fromIndex = (page - 1) * size;
            int toIndex = Math.min(fromIndex + size, typeTotal);

            List<RecommendationVO> items = new ArrayList<>();
            if (fromIndex < typeTotal) {
                List<ContentScore> pageScores = typeScores.subList(fromIndex, toIndex);
                for (ContentScore cs : pageScores) {
                    RecommendationVO vo = convertToVO(cs.content, cs.score, userId);
                    items.add(vo);
                }
            }

            GroupedRecommendationVO groupVO = new GroupedRecommendationVO();
            groupVO.setType(typeKey);
            groupVO.setItems(items);
            groupVO.setTotal(typeTotal);
            groups.put(typeKey, groupVO);
        }

        // 8. 构建最终响应
        GroupedRecommendationResponse response = new GroupedRecommendationResponse();
        response.setGroups(groups);
        response.setTotal(totalCount);

        log.info("为用户 {} 生成推荐列表，共 {} 条记录", userId, totalCount);
        return response;
    }

    /**
     * 构建空响应
     */
    private GroupedRecommendationResponse buildEmptyResponse() {
        Map<String, GroupedRecommendationVO> groups = new LinkedHashMap<>();
        for (String type : new String[]{"MOVIE", "MUSIC", "GAME"}) {
            GroupedRecommendationVO vo = new GroupedRecommendationVO();
            vo.setType(type);
            vo.setItems(new ArrayList<>());
            vo.setTotal(0);
            groups.put(type, vo);
        }
        GroupedRecommendationResponse response = new GroupedRecommendationResponse();
        response.setGroups(groups);
        response.setTotal(0);
        return response;
    }

    @Override
    @Transactional
    public void recordRecommend(Long userId, Long contentId, String reason, Integer score) {
        // 检查是否已存在
        LambdaQueryWrapper<Recommendation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Recommendation::getUserId, userId)
               .eq(Recommendation::getContentId, contentId);
        Recommendation existing = recommendationMapper.selectOne(wrapper);

        if (existing != null) {
            return; // 已存在，不重复记录
        }

        Recommendation recommendation = new Recommendation();
        recommendation.setUserId(userId);
        recommendation.setContentId(contentId);
        recommendation.setReason(reason);
        recommendation.setScore(score);
        recommendation.setIsClicked(0);
        recommendation.setIsLiked(0);
        recommendation.setIsDisliked(0);

        recommendationMapper.insert(recommendation);
    }

    @Override
    @Transactional
    public void recordClick(Long userId, Long contentId) {
        // 更新推荐记录
        updateRecommendation(userId, contentId, "isClicked", 1);

        // 更新用户偏好
        Content content = contentMapper.selectById(contentId);
        if (content != null) {
            int weight = getConfigurableWeight(userId, content.getType(), "CLICK");
            updateUserPreferenceScore(userId, content.getType(), weight);
        }

        // 记录反馈
        saveFeedback(userId, contentId, null, "CLICK", null);
    }

    @Override
    @Transactional
    public void recordLike(Long userId, Long contentId) {
        // 更新推荐记录
        updateRecommendation(userId, contentId, "isLiked", 1);

        // 更新用户偏好
        Content content = contentMapper.selectById(contentId);
        if (content != null) {
            int weight = getConfigurableWeight(userId, content.getType(), "LIKE");
            updateUserPreferenceScore(userId, content.getType(), weight);
        }

        // 记录反馈
        saveFeedback(userId, contentId, null, "LIKE", null);
    }

    @Override
    @Transactional
    public void recordDislike(Long userId, Long contentId) {
        // 更新推荐记录
        updateRecommendation(userId, contentId, "isDisliked", 1);

        // 更新用户偏好
        Content content = contentMapper.selectById(contentId);
        if (content != null) {
            int weight = getConfigurableWeight(userId, content.getType(), "DISLIKE");
            updateUserPreferenceScore(userId, content.getType(), weight);
        }

        // 记录反馈
        saveFeedback(userId, contentId, null, "DISLIKE", null);
    }

    @Override
    @Transactional
    public void submitFeedback(RecommendationFeedbackDTO dto) {
        saveFeedback(dto.getUserId(), dto.getContentId(), null, dto.getFeedbackType(), dto.getScore());

        // 根据反馈类型更新用户偏好
        Content content = contentMapper.selectById(dto.getContentId());
        if (content != null) {
            int weight = getConfigurableWeight(dto.getUserId(), content.getType(), dto.getFeedbackType());
            updateUserPreferenceScore(dto.getUserId(), content.getType(), weight);
        }
    }

    @Override
    public IPage<RecommendationFeedbackVO> getUserFeedbackHistory(Long userId, Integer page, Integer size) {
        Page<RecommendationFeedback> feedbackPage = new Page<>(page, size);
        LambdaQueryWrapper<RecommendationFeedback> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RecommendationFeedback::getUserId, userId)
               .orderByDesc(RecommendationFeedback::getCreateTime);

        IPage<RecommendationFeedback> resultPage = feedbackMapper.selectPage(feedbackPage, wrapper);
        return resultPage.convert(this::convertFeedbackToVO);
    }

    @Override
    @Transactional
    public void updateUserPreferenceScore(Long userId, String contentType, int deltaScore) {
        // 查询是否已存在偏好
        UserPreference existing = userPreferenceMapper.selectByUserIdAndContentType(userId, contentType);

        if (existing != null) {
            // 更新偏好分数
            int newScore = Math.max(0, Math.min(100, existing.getPreferenceScore() + deltaScore));
            existing.setPreferenceScore(newScore);
            userPreferenceMapper.updateById(existing);
        } else {
            // 创建新偏好，设置默认权重
            UserPreference preference = new UserPreference();
            preference.setUserId(userId);
            preference.setContentType(contentType);
            preference.setPreferenceScore(Math.max(0, deltaScore));
            preference.setClickWeight(5);
            preference.setLikeWeight(10);
            preference.setDislikeWeight(-20);
            userPreferenceMapper.insert(preference);
        }
    }

    // ==================== 内部方法 ====================

    /**
     * 计算内容推荐分数
     */
    private int calculateRecommendationScore(Content content, Map<String, Integer> preferenceMap, Long userId) {
        int score = 50; // 基础分数

        // 1. 基于用户类型偏好的分数
        if (preferenceMap.containsKey(content.getType())) {
            int preferenceScore = preferenceMap.get(content.getType());
            score += preferenceScore / 2; // 类型偏好分数贡献 50%
        }

        // 2. 基于内容分类（genre）的偏好分数
        if (content.getGenre() != null && preferenceMap.containsKey(content.getGenre())) {
            int genrePreferenceScore = preferenceMap.get(content.getGenre());
            score += genrePreferenceScore / 3; // 分类偏好分数贡献
        }

        // 3. 基于内容评分的分数
        if (content.getRating() != null) {
            score += (int) (content.getRating().doubleValue() * 10); // 评分贡献
        }

        // 4. 基于用户历史行为的调整
        score += getUserBehaviorBonus(userId, content.getType());

        return Math.min(100, Math.max(0, score));
    }

    /**
     * 获取用户行为加分
     */
    private int getUserBehaviorBonus(Long userId, String contentType) {
        // 查询用户对该类型内容的反馈历史
        LambdaQueryWrapper<RecommendationFeedback> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RecommendationFeedback::getUserId, userId)
               .eq(RecommendationFeedback::getFeedbackType, "LIKE");

        List<RecommendationFeedback> feedbacks = feedbackMapper.selectList(wrapper);
        if (feedbacks == null || feedbacks.isEmpty()) {
            return 0;
        }

        // 统计该类型内容的喜欢次数
        long likeCount = feedbacks.stream()
            .filter(f -> {
                Content c = contentMapper.selectById(f.getContentId());
                return c != null && contentType.equals(c.getType());
            })
            .count();

        return (int) Math.min(likeCount * 5, 20); // 最多加 20 分
    }

    /**
     * 获取已反馈的内容 ID 列表
     */
    private List<Long> getExcludedContentIds(Long userId) {
        LambdaQueryWrapper<RecommendationFeedback> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RecommendationFeedback::getUserId, userId);
        List<RecommendationFeedback> feedbacks = feedbackMapper.selectList(wrapper);

        if (feedbacks == null || feedbacks.isEmpty()) {
            return new ArrayList<>();
        }

        return feedbacks.stream()
            .map(RecommendationFeedback::getContentId)
            .collect(Collectors.toList());
    }

    /**
     * 更新推荐记录
     */
    private void updateRecommendation(Long userId, Long contentId, String field, int value) {
        LambdaQueryWrapper<Recommendation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Recommendation::getUserId, userId)
               .eq(Recommendation::getContentId, contentId);
        Recommendation recommendation = recommendationMapper.selectOne(wrapper);

        if (recommendation == null) {
            // 创建新推荐记录
            recommendation = new Recommendation();
            recommendation.setUserId(userId);
            recommendation.setContentId(contentId);
            recommendation.setReason("用户主动操作");
            recommendation.setScore(50);
            recommendation.setIsClicked(0);
            recommendation.setIsLiked(0);
            recommendation.setIsDisliked(0);
            recommendationMapper.insert(recommendation);
        }

        // 更新字段
        if ("isClicked".equals(field)) {
            recommendation.setIsClicked(value);
        } else if ("isLiked".equals(field)) {
            recommendation.setIsLiked(value);
        } else if ("isDisliked".equals(field)) {
            recommendation.setIsDisliked(value);
        }
        recommendationMapper.updateById(recommendation);
    }

    /**
     * 保存反馈记录
     */
    private void saveFeedback(Long userId, Long contentId, Long recommendationId, String feedbackType, Integer score) {
        // 检查是否已存在相同反馈
        LambdaQueryWrapper<RecommendationFeedback> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RecommendationFeedback::getUserId, userId)
               .eq(RecommendationFeedback::getContentId, contentId)
               .eq(RecommendationFeedback::getFeedbackType, feedbackType);

        RecommendationFeedback existing = feedbackMapper.selectOne(wrapper);
        if (existing != null) {
            return; // 已存在，不重复记录
        }

        RecommendationFeedback feedback = new RecommendationFeedback();
        feedback.setUserId(userId);
        feedback.setContentId(contentId);
        feedback.setRecommendationId(recommendationId);
        feedback.setFeedbackType(feedbackType);
        feedback.setScore(score);

        feedbackMapper.insert(feedback);
    }

    /**
     * 获取可配置的反馈权重
     * 从 UserPreference 表中获取用户设置的权重，若未设置则使用默认值
     */
    private int getConfigurableWeight(Long userId, String contentType, String feedbackType) {
        UserPreference pref = userPreferenceMapper.selectByUserIdAndContentType(userId, contentType);

        if (pref == null) {
            return getDefaultWeight(feedbackType);
        }

        switch (feedbackType) {
            case "CLICK":
                return (pref.getClickWeight() != null) ? pref.getClickWeight() : 5;
            case "LIKE":
            case "COLLECT":
                return (pref.getLikeWeight() != null) ? pref.getLikeWeight() : 10;
            case "DISLIKE":
                return (pref.getDislikeWeight() != null) ? pref.getDislikeWeight() : -20;
            default:
                return 0;
        }
    }

    /**
     * 获取默认权重
     */
    private int getDefaultWeight(String feedbackType) {
        switch (feedbackType) {
            case "CLICK":
                return 5;
            case "LIKE":
            case "COLLECT":
                return 10;
            case "DISLIKE":
                return -20;
            default:
                return 0;
        }
    }

    /**
     * 实体转 VO
     */
    private RecommendationVO convertToVO(Content content, int score, Long userId) {
        RecommendationVO vo = new RecommendationVO();
        vo.setContentId(content.getId());
        vo.setUserId(userId);
        vo.setContentTitle(content.getTitle());
        vo.setContentType(content.getType());
        vo.setContentGenre(content.getGenre());
        vo.setContentCover(content.getCover());
        vo.setReason(generateReason(content, score));
        vo.setScore(score);
        vo.setIsClicked(0);
        vo.setIsLiked(0);
        vo.setCreateTime(LocalDateTime.now());
        return vo;
    }

    /**
     * 生成推荐原因
     */
    private String generateReason(Content content, int score) {
        StringBuilder reason = new StringBuilder();
        
        if (content.getGenre() != null) {
            reason.append(content.getGenre());
            reason.append(" · ");
        }
        
        if (score >= 85) {
            reason.append("强烈推荐");
        } else if (score >= 75) {
            reason.append("精选推荐");
        } else if (score >= 65) {
            reason.append("热门推荐");
        } else {
            reason.append("猜你喜欢");
        }
        
        return reason.toString();
    }

    /**
     * 反馈实体转 VO
     */
    private RecommendationFeedbackVO convertFeedbackToVO(RecommendationFeedback feedback) {
        RecommendationFeedbackVO vo = new RecommendationFeedbackVO();
        vo.setId(feedback.getId());
        vo.setUserId(feedback.getUserId());
        vo.setContentId(feedback.getContentId());
        vo.setFeedbackType(feedback.getFeedbackType());
        vo.setScore(feedback.getScore());
        vo.setCreateTime(feedback.getCreateTime());

        // 获取内容标题
        Content content = contentMapper.selectById(feedback.getContentId());
        if (content != null) {
            vo.setContentTitle(content.getTitle());
        }

        return vo;
    }

    /**
     * 内部类：内容分数
     */
    private static class ContentScore {
        Content content;
        int score;

        ContentScore(Content content, int score) {
            this.content = content;
            this.score = score;
        }
    }
}
