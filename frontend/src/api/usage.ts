/**
 * 使用统计相关 API
 */

import request from '@/utils/request';

/**
 * 统计条目
 */
export interface StatItem {
  statKey: string;
  count: number;
}

/**
 * 使用统计响应
 */
export interface UsageStats {
  targetType: string;
  dimension: string;
  statistics: StatItem[];
}

/**
 * 排行条目
 */
export interface RankItem {
  targetId: number;
  targetName: string;
  targetType: string;
  count: number;
}

/**
 * 排行响应
 */
export interface UsageRanking {
  rankings: RankItem[];
}

/**
 * 时间线条目
 */
export interface TimelineItem {
  activateTime: string;
  action: string;
}

/**
 * 时间线响应
 */
export interface UsageTimeline {
  targetType: string;
  targetId: number;
  targetName: string;
  timeline: TimelineItem[];
}

/**
 * STATS-03: 查询我的使用统计
 */
export function getMyStatistics(params: {
  dimension?: string;
  targetType?: string;
  startDate?: string;
  endDate?: string;
}) {
  return request.get<any, UsageStats>('/usage/stats/my', { params });
}

/**
 * STATS-04: 查询全局使用排行（仅管理员）
 */
export function getGlobalRanking(params?: {
  targetType?: string;
  limit?: number;
}) {
  return request.get<any, UsageRanking>('/usage/stats/ranking', { params });
}

/**
 * STATS-05: 查询单条设备/场景的历史时间线
 */
export function getTimeline(
  targetType: string,
  targetId: number,
  params?: { page?: number; size?: number }
) {
  return request.get<any, UsageTimeline>(
    `/usage/timeline/${targetType}/${targetId}`,
    { params }
  );
}
