/**
 * 设备管理相关 API
 * 接口文档：/docs/API 接口文档.md#9-设备管理接口
 */

import request from '@/utils/request';

/**
 * 设备类型枚举
 */
export enum DeviceType {
  TV = 'TV',
  SPEAKER = 'SPEAKER',
  LIGHT = 'LIGHT',
  AIR_CONDITIONER = 'AIR_CONDITIONER',
  CURTAIN = 'CURTAIN',
  DOOR_LOCK = 'DOOR_LOCK',
  CAMERA = 'CAMERA',
  SENSOR = 'SENSOR',
  OTHER = 'OTHER',
}

/**
 * 设备查询参数
 */
export interface DeviceQueryParams {
  page?: number;
  size?: number;
  keyword?: string;
  type?: string;
  room?: string;
  status?: number;
  isOnline?: number;
  userId?: number;
}

/**
 * 设备信息
 */
export interface DeviceInfo {
  id: number;
  name: string;
  type: string;
  room: string;
  status: number;
  isOnline: number;
  userId: number;
  createTime?: string;
  updateTime?: string;
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
 * 查询设备列表
 * GET /api/devices
 */
export function getDeviceList(params?: DeviceQueryParams): Promise<PageResult<DeviceInfo>> {
  return request.get('/devices', { params });
}

/**
 * 查询设备详情
 * GET /api/devices/{id}
 */
export function getDeviceById(id: number): Promise<DeviceInfo> {
  return request.get(`/devices/${id}`);
}

/**
 * 创建设备
 * POST /api/devices?userId={userId}
 */
export function createDevice(userId: number, data: {
  name: string;
  type: string;
  room?: string;
  status?: number;
}): Promise<number> {
  return request.post('/devices', data, { params: { userId } });
}

/**
 * 更新设备信息
 * PUT /api/devices/{id}
 */
export function updateDevice(id: number, data: {
  name?: string;
  room?: string;
  status?: number;
}): Promise<DeviceInfo> {
  return request.put(`/devices/${id}`, data);
}

/**
 * 删除设备
 * DELETE /api/devices/{id}
 */
export function deleteDevice(id: number): Promise<void> {
  return request.delete(`/devices/${id}`);
}

/**
 * 更新设备状态
 * PUT /api/devices/{id}/status
 */
export function updateDeviceStatus(id: number, data: {
  status?: number;
  isOnline?: number;
}): Promise<DeviceInfo> {
  return request.put(`/devices/${id}/status`, data);
}

/**
 * 统计各类型设备数量
 * GET /api/devices/statistics/types
 */
export function getDeviceTypesCount(): Promise<{ name: string; count: number }[]> {
  return request.get('/devices/statistics/types');
}

/**
 * 统计各房间设备数量
 * GET /api/devices/statistics/rooms
 */
export function getDeviceRoomsCount(): Promise<{ name: string; count: number }[]> {
  return request.get('/devices/statistics/rooms');
}
