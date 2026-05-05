/**
 * 日志管理相关 API
 * 接口文档：/docs/API 接口文档.md#8-日志管理接口
 */

import request from '@/utils/request';

/**
 * 操作日志信息
 */
export interface OperationLogInfo {
  id: number;
  userId: number;
  username: string;
  operation: string;
  targetType?: string;
  targetId?: number;
  ipAddress: string;
  createTime?: string;
}

/**
 * 日志查询参数
 */
export interface LogQueryParams {
  page?: number;
  size?: number;
  userId?: number;
  operation?: string;
  targetType?: string;
  startDate?: string;
  endDate?: string;
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
 * 查询操作日志列表
 * GET /api/logs
 */
export function getLogList(params?: LogQueryParams): Promise<PageResult<OperationLogInfo>> {
  return request.get('/logs', { params });
}

/**
 * 根据 ID 查询操作日志
 * GET /api/logs/{id}
 */
export function getLogById(id: number): Promise<OperationLogInfo> {
  return request.get(`/logs/${id}`);
}

/**
 * 统计各操作类型的数量
 * GET /api/logs/stats/operation
 */
export function getOperationStats(): Promise<{ operation: string; count: number }[]> {
  return request.get('/logs/stats/operation');
}

/**
 * 统计每日日志数量
 * GET /api/logs/stats/daily?days=7
 */
export function getDailyStats(days: number = 7): Promise<{ date: string; count: number }[]> {
  return request.get('/logs/stats/daily', { params: { days } });
}
