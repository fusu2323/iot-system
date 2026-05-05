/**
 * 推荐管理相关 API
 * 接口文档：/docs/API 接口文档.md#12-推荐管理接口
 */

import request from '@/utils/request';

/**
 * 推荐信息
 */
export interface RecommendationInfo {
  id: number;
  userId: number;
  contentId: number;
  contentTitle: string;
  contentType: string;
  contentCover: string;
  reason: string;
  score: number;
  isClicked: number;
  isLiked: number;
  createTime?: string;
}

/**
 * 推荐查询参数
 */
export interface RecommendationQueryParams {
  userId: number;
  page?: number;
  size?: number;
}

/**
 * 分页响应数据
 */
export interface PageResult<T> {
  records: T[];
  total: number;
  size: number;
  current: number;
  pages: number;
}

/**
 * 获取推荐列表
 * GET /api/recommendations?userId={userId}
 */
export function getRecommendations(params: RecommendationQueryParams): Promise<PageResult<RecommendationInfo>> {
  return request.get('/recommendations', { params });
}

/**
 * 记录推荐点击
 * POST /api/recommendations/click?userId={userId}&contentId={contentId}
 */
export function recordClick(userId: number, contentId: number): Promise<void> {
  return request.post('/recommendations/click', null, { params: { userId, contentId } });
}

/**
 * 记录推荐喜欢
 * POST /api/recommendations/like?userId={userId}&contentId={contentId}
 */
export function recordLike(userId: number, contentId: number): Promise<void> {
  return request.post('/recommendations/like', null, { params: { userId, contentId } });
}

/**
 * 记录推荐不喜欢
 * POST /api/recommendations/dislike?userId={userId}&contentId={contentId}
 */
export function recordDislike(userId: number, contentId: number): Promise<void> {
  return request.post('/recommendations/dislike', null, { params: { userId, contentId } });
}

/**
 * 提交推荐反馈
 * POST /api/recommendations/feedback
 */
export function submitFeedback(data: {
  userId: number;
  contentId: number;
  feedbackType: string;
  score?: number;
}): Promise<void> {
  return request.post('/recommendations/feedback', data);
}

/**
 * 获取用户反馈历史
 * GET /api/recommendations/feedback/history?userId={userId}
 */
export function getFeedbackHistory(params: {
  userId: number;
  page?: number;
  size?: number;
}): Promise<PageResult<any>> {
  return request.get('/recommendations/feedback/history', { params });
}
