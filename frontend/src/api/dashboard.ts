/**
 * 数据统计相关 API
 * 接口文档：/docs/API 接口文档.md#13-数据统计接口
 */

import request from '@/utils/request';

/**
 * 概览统计数据
 */
export interface OverviewStats {
  totalDevices: number;
  onlineDevices: number;
  offlineDevices: number;
  totalScenes: number;
  enabledScenes: number;
  totalContents: number;
  totalUsers: number;
  todayLogs: number;
  onlineRate: number;
}

/**
 * 趋势数据项
 */
export interface TrendDataItem {
  date: string;
  deviceCount?: number;
  onlineCount?: number;
  newCount?: number;
  operationCount?: number;
  activeUsers?: number;
  newUsers?: number;
}

/**
 * 场景使用统计
 */
export interface SceneUsageStats {
  sceneId: number;
  sceneName: string;
  triggerCount: number;
}

/**
 * 内容分布统计
 */
export interface ContentDistribution {
  type: string;
  count: number;
}

/**
 * 系统信息
 */
export interface SystemInfo {
  javaVersion: string;
  javaVendor: string;
  osName: string;
  osVersion: string;
  availableProcessors: string;
  maxMemory: string;
  freeMemory: string;
  totalMemory: string;
}

/**
 * 设备统计概览
 */
export interface DeviceStatsOverview {
  total: number;
  online: number;
  offline: number;
}

/**
 * 获取概览统计数据
 * GET /api/dashboard/overview
 */
export function getOverviewStats(): Promise<OverviewStats> {
  return request.get('/dashboard/overview');
}

/**
 * 获取设备趋势数据
 * GET /api/dashboard/device-trend?days=7
 */
export function getDeviceTrend(days: number = 7): Promise<TrendDataItem[]> {
  return request.get('/dashboard/device-trend', { params: { days } });
}

/**
 * 获取用户活跃度数据
 * GET /api/dashboard/user-activity?days=7
 */
export function getUserActivity(days: number = 7): Promise<TrendDataItem[]> {
  return request.get('/dashboard/user-activity', { params: { days } });
}

/**
 * 获取场景使用统计
 * GET /api/dashboard/scene-usage
 */
export function getSceneUsage(): Promise<SceneUsageStats[]> {
  return request.get('/dashboard/scene-usage');
}

/**
 * 获取内容分布统计
 * GET /api/dashboard/content-distribution
 */
export function getContentDistribution(): Promise<ContentDistribution[]> {
  return request.get('/dashboard/content-distribution');
}

/**
 * 获取系统信息
 * GET /api/dashboard/system-info
 */
export function getSystemInfo(): Promise<SystemInfo> {
  return request.get('/dashboard/system-info');
}

/**
 * 获取设备统计概览
 * GET /api/device-statistics/overview
 */
export function getDeviceStatsOverview(): Promise<DeviceStatsOverview> {
  return request.get('/device-statistics/overview');
}
