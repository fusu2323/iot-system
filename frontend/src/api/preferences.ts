/**
 * 偏好管理相关 API
 * 接口文档：/docs/API 接口文档.md#7-偏好管理接口
 */

import request from '@/utils/request';

/**
 * 用户偏好信息
 */
export interface UserPreference {
  id: number;
  userId: number;
  contentType: string; // MOVIE/MUSIC/GAME
  preferenceScore: number;
  createTime?: string;
  updateTime?: string;
}

/**
 * 设置用户偏好
 * POST /api/preferences?userId={userId}
 */
export function setPreference(userId: number, data: {
  contentType: string;
  preferenceScore: number;
}): Promise<void> {
  return request.post('/preferences', data, { params: { userId } });
}

/**
 * 获取用户偏好列表
 * GET /api/preferences?userId={userId}
 */
export function getPreferences(userId: number): Promise<UserPreference[]> {
  return request.get('/preferences', { params: { userId } });
}

/**
 * 获取用户指定类型的偏好
 * GET /api/preferences/{contentType}?userId={userId}
 */
export function getPreferenceByType(userId: number, contentType: string): Promise<UserPreference> {
  return request.get(`/preferences/${contentType}`, { params: { userId } });
}

/**
 * 删除用户偏好
 * DELETE /api/preferences/{contentType}?userId={userId}
 */
export function deletePreference(userId: number, contentType: string): Promise<void> {
  return request.delete(`/preferences/${contentType}`, { params: { userId } });
}
