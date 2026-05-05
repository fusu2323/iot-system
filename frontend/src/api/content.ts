/**
 * 内容管理相关 API
 * 接口文档：/docs/API 接口文档.md#11-内容管理接口
 */

import request from '@/utils/request';

/**
 * 内容类型枚举
 */
export enum ContentType {
  MOVIE = 'MOVIE',
  MUSIC = 'MUSIC',
  GAME = 'GAME',
}

/**
 * 内容信息
 */
export interface ContentInfo {
  id: number;
  title: string;
  type: string;
  cover?: string;
  description?: string;
  rating?: number;
  createTime?: string;
  updateTime?: string;
}

/**
 * 内容查询参数
 */
export interface ContentQueryParams {
  page?: number;
  size?: number;
  keyword?: string;
  type?: string;
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
 * 查询内容列表
 * GET /api/contents
 */
export function getContentList(params?: ContentQueryParams): Promise<PageResult<ContentInfo>> {
  return request.get('/contents', { params });
}

/**
 * 查询内容详情
 * GET /api/contents/{id}
 */
export function getContentById(id: number): Promise<ContentInfo> {
  return request.get(`/contents/${id}`);
}

/**
 * 创建内容
 * POST /api/contents
 */
export function createContent(data: {
  title: string;
  type: string;
  cover?: string;
  description?: string;
  rating?: number;
}): Promise<number> {
  return request.post('/contents', data);
}

/**
 * 更新内容信息
 * PUT /api/contents/{id}
 */
export function updateContent(id: number, data: {
  title?: string;
  rating?: number;
}): Promise<ContentInfo> {
  return request.put(`/contents/${id}`, data);
}

/**
 * 删除内容
 * DELETE /api/contents/{id}
 */
export function deleteContent(id: number): Promise<void> {
  return request.delete(`/contents/${id}`);
}
