/**
 * 用户管理相关 API
 * 接口文档：/docs/API 接口文档.md#6-用户管理接口
 */

import request from '@/utils/request';
import type { UserInfo } from '@/stores/user';

/**
 * 用户列表查询参数
 */
export interface UserQueryParams {
  page?: number;
  size?: number;
  keyword?: string;
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
 * 更新用户信息请求参数
 */
export interface UserUpdateRequest {
  nickname?: string;
  avatar?: string;
  email?: string;
  phone?: string;
  role?: string;
}

/**
 * 创建用户请求参数
 */
export interface UserCreateRequest {
  username: string;
  password: string;
  nickname?: string;
  email?: string;
  phone?: string;
  role?: string;
}

/**
 * 获取当前登录用户信息
 * GET /api/users/me
 */
export function getCurrentUser(): Promise<UserInfo> {
  return request.get('/users/me');
}

/**
 * 根据 ID 查询用户
 * GET /api/users/{id}
 */
export function getUserById(id: number): Promise<UserInfo> {
  return request.get(`/users/${id}`);
}

/**
 * 查询用户列表
 * GET /api/users
 */
export function getUserList(params?: UserQueryParams): Promise<PageResult<UserInfo>> {
  return request.get('/users', { params });
}

/**
 * 创建用户
 * POST /api/users
 */
export function createUser(data: UserCreateRequest): Promise<UserInfo> {
  return request.post('/users', data);
}

/**
 * 更新用户信息
 * PUT /api/users/{id}
 */
export function updateUser(id: number, data: UserUpdateRequest): Promise<UserInfo> {
  return request.put(`/users/${id}`, data);
}

/**
 * 删除用户
 * DELETE /api/users/{id}
 */
export function deleteUser(id: number): Promise<void> {
  return request.delete(`/users/${id}`);
}
