/**
 * 系统配置相关 API
 * 接口文档：/docs/API 接口文档.md#14-系统配置接口
 */

import request from '@/utils/request';

/**
 * 系统配置信息
 */
export interface SystemConfig {
  id: number;
  configKey: string;
  configValue: string;
  description?: string;
  configType: string;
  deleted: number;
  createTime?: string;
  updateTime?: string;
}

/**
 * 根据键获取配置值
 * GET /api/system/configs/{configKey}
 */
export function getConfigByKey(configKey: string): Promise<string> {
  return request.get(`/system/configs/${configKey}`);
}

/**
 * 设置配置值
 * POST /api/system/configs
 */
export function setConfig(data: {
  configKey: string;
  configValue: string;
  description?: string;
}): Promise<void> {
  return request.post('/system/configs', data);
}

/**
 * 获取所有系统配置
 * GET /api/system/configs
 */
export function getAllConfigs(): Promise<SystemConfig[]> {
  return request.get('/system/configs');
}

/**
 * 删除配置
 * DELETE /api/system/configs/{configKey}
 */
export function deleteConfig(configKey: string): Promise<void> {
  return request.delete(`/system/configs/${configKey}`);
}
