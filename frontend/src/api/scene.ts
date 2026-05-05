/**
 * 场景管理相关 API
 * 接口文档：/docs/API 接口文档.md#10-场景管理接口
 */

import request from '@/utils/request';

/**
 * 场景设备配置
 */
export interface SceneDeviceConfig {
  deviceId: number;
  deviceName: string;
  config?: string;
}

/**
 * 场景信息
 */
export interface SceneInfo {
  id: number;
  name: string;
  description: string;
  icon: string;
  isEnabled: number;
  userId: number;
  createTime?: string;
  updateTime?: string;
  devices?: SceneDeviceConfig[];
}

/**
 * 场景查询参数
 */
export interface SceneQueryParams {
  page?: number;
  size?: number;
  userId?: number;
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
 * 查询场景列表
 * GET /api/scenes
 */
export function getSceneList(params?: SceneQueryParams): Promise<PageResult<SceneInfo>> {
  return request.get('/scenes', { params });
}

/**
 * 查询场景详情
 * GET /api/scenes/{id}
 */
export function getSceneById(id: number): Promise<SceneInfo> {
  return request.get(`/scenes/${id}`);
}

/**
 * 创建场景
 * POST /api/scenes?userId={userId}
 */
export function createScene(userId: number, data: {
  name: string;
  description?: string;
  icon?: string;
}): Promise<number> {
  return request.post('/scenes', data, { params: { userId } });
}

/**
 * 更新场景信息
 * PUT /api/scenes/{id}
 */
export function updateScene(id: number, data: {
  name?: string;
  description?: string;
  icon?: string;
}): Promise<SceneInfo> {
  return request.put(`/scenes/${id}`, data);
}

/**
 * 删除场景
 * DELETE /api/scenes/{id}
 */
export function deleteScene(id: number): Promise<void> {
  return request.delete(`/scenes/${id}`);
}

/**
 * 启用/禁用场景
 * POST /api/scenes/{id}/toggle
 */
export function toggleScene(id: number): Promise<SceneInfo> {
  return request.post(`/scenes/${id}/toggle`);
}

/**
 * 触发场景
 * POST /api/scenes/{id}/trigger
 */
export function triggerScene(id: number): Promise<void> {
  return request.post(`/scenes/${id}/trigger`);
}

/**
 * 添加场景设备关联
 * POST /api/scenes/{id}/devices?deviceId={deviceId}&config={config}
 */
export function addSceneDevice(id: number, deviceId: number, config?: string): Promise<void> {
  return request.post(`/scenes/${id}/devices`, null, {
    params: { deviceId, config }
  });
}

/**
 * 移除场景设备关联
 * DELETE /api/scenes/{id}/devices?deviceId={deviceId}
 */
export function removeSceneDevice(id: number, deviceId: number): Promise<void> {
  return request.delete(`/scenes/${id}/devices`, {
    params: { deviceId }
  });
}
