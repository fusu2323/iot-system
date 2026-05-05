/**
 * 定时任务管理相关 API
 */

import request from '@/utils/request';

/**
 * 时间配置
 */
export interface TimeConfig {
  hour?: number;
  minute?: number;
  cron?: string;
}

/**
 * 定时任务信息
 */
export interface ScheduledTaskInfo {
  id: number;
  name: string;
  sceneId: number;
  sceneName?: string;
  scheduleType: number;
  scheduleTypeDesc?: string;
  cronExpression: string;
  timeConfig: string; // JSON string
  isEnabled: number;
  nextFireTime?: string;
  createdBy: number;
  createTime?: string;
  updateTime?: string;
}

/**
 * 创建定时任务请求
 */
export interface CreateScheduledTaskRequest {
  name: string;
  sceneId: number;
  scheduleType: number;
  timeConfig: string; // JSON string
}

/**
 * 更新定时任务请求
 */
export interface UpdateScheduledTaskRequest {
  name?: string;
  sceneId?: number;
  scheduleType?: number;
  timeConfig?: string; // JSON string
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
 * 查询定时任务列表
 * GET /api/scheduled-tasks
 */
export function getScheduledTaskList(params?: {
  page?: number;
  size?: number;
  name?: string;
}): Promise<PageResult<ScheduledTaskInfo>> {
  return request.get('/scheduled-tasks', { params });
}

/**
 * 查询定时任务详情
 * GET /api/scheduled-tasks/{id}
 */
export function getScheduledTaskById(id: number): Promise<ScheduledTaskInfo> {
  return request.get(`/scheduled-tasks/${id}`);
}

/**
 * 创建定时任务
 * POST /api/scheduled-tasks
 */
export function createScheduledTask(
  userId: number,
  data: CreateScheduledTaskRequest
): Promise<number> {
  return request.post('/scheduled-tasks', data, { params: { userId } });
}

/**
 * 更新定时任务
 * PUT /api/scheduled-tasks/{id}
 */
export function updateScheduledTask(
  id: number,
  data: UpdateScheduledTaskRequest
): Promise<ScheduledTaskInfo> {
  return request.put(`/scheduled-tasks/${id}`, data);
}

/**
 * 删除定时任务
 * DELETE /api/scheduled-tasks/{id}
 */
export function deleteScheduledTask(id: number): Promise<void> {
  return request.delete(`/scheduled-tasks/${id}`);
}

/**
 * 启用/禁用定时任务
 * POST /api/scheduled-tasks/{id}/toggle
 */
export function toggleScheduledTask(id: number): Promise<ScheduledTaskInfo> {
  return request.post(`/scheduled-tasks/${id}/toggle`);
}