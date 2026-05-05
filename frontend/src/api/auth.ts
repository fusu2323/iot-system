/**
 * 用户认证相关 API
 * 接口文档：/docs/API 接口文档.md#5-用户认证接口
 */

import request from '@/utils/request';

/**
 * 登录请求参数
 */
export interface LoginRequest {
  username: string;
  password: string;
}

/**
 * 登录响应数据
 */
export interface LoginResponse {
  token: string;
  tokenType: string;
  userId: number;
  username: string;
  nickname: string;
  avatar: string | null;
  role: string;
}

/**
 * 注册请求参数
 */
export interface RegisterRequest {
  username: string;
  password: string;
  nickname?: string;
}

/**
 * 用户登录
 * POST /api/auth/login
 */
export function login(data: LoginRequest): Promise<LoginResponse> {
  return request.post('/auth/login', data);
}

/**
 * 用户注册
 * POST /api/auth/register
 */
export function register(data: RegisterRequest): Promise<number> {
  return request.post('/auth/register', data);
}
