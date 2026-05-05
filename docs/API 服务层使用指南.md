# API 服务层使用指南

## 目录结构

```
frontend/src/api/
├── index.ts        # 统一导出文件
├── auth.ts         # 用户认证（登录/注册）
├── user.ts         # 用户管理（CRUD）
├── preferences.ts  # 偏好管理
├── log.ts          # 日志管理
├── device.ts       # 设备管理
├── scene.ts        # 场景管理
├── content.ts      # 内容管理
├── recommend.ts    # 推荐管理
├── dashboard.ts    # 数据统计
└── system.ts       # 系统配置
```

## 快速开始

### 1. 导入 API 函数

```typescript
// 方式一：单独导入（推荐）
import { login } from '@/api/auth';
import { getUserList } from '@/api/user';
import { getDeviceList } from '@/api/device';

// 方式二：批量导入
import * as userAPI from '@/api/user';
import * as deviceAPI from '@/api/device';

// 方式三：从 index.ts 统一导入
import { login, getUserList, getDeviceList } from '@/api';
```

### 2. 调用 API 示例

#### 用户认证
```typescript
import { login, register } from '@/api/auth';

// 登录
const loginResult = await login({
  username: 'admin',
  password: 'admin123'
});
// 返回：{ token, tokenType, userId, username, nickname, avatar, role }

// 注册
const userId = await register({
  username: 'zhangsan',
  password: '123456',
  nickname: '张三'
});
// 返回：新用户的 ID
```

#### 用户管理
```typescript
import { getCurrentUser, getUserList, updateUser, deleteUser } from '@/api/user';

// 获取当前登录用户信息
const currentUser = await getCurrentUser();

// 获取用户列表（分页）
const userList = await getUserList({
  page: 1,
  size: 10,
  keyword: '张'
});
// 返回：{ records, total, size, current, pages }

// 更新用户信息
const updatedUser = await updateUser(1, {
  nickname: '新昵称',
  avatar: 'https://example.com/avatar.jpg'
});

// 删除用户
await deleteUser(1);
```

#### 设备管理
```typescript
import {
  getDeviceList,
  getDeviceById,
  createDevice,
  updateDevice,
  deleteDevice,
  updateDeviceStatus
} from '@/api/device';

// 获取设备列表
const devices = await getDeviceList({
  page: 1,
  size: 10,
  type: 'TV',
  room: '客厅'
});

// 获取设备详情
const device = await getDeviceById(1);

// 创建设备（需要传递 userId）
const newDeviceId = await createDevice(1, {
  name: '客厅电视',
  type: 'TV',
  room: '客厅',
  status: 1
});

// 更新设备信息
await updateDevice(1, {
  name: '新客厅电视',
  room: '大客厅'
});

// 更新设备状态
await updateDeviceStatus(1, {
  status: 1,
  isOnline: 1
});

// 删除设备
await deleteDevice(1);
```

#### 场景管理
```typescript
import {
  getSceneList,
  getSceneById,
  createScene,
  updateScene,
  deleteScene,
  toggleScene,
  triggerScene,
  addSceneDevice,
  removeSceneDevice
} from '@/api/scene';

// 获取场景列表
const scenes = await getSceneList({
  page: 1,
  size: 10,
  userId: 1
});

// 创建场景
const sceneId = await createScene(1, {
  name: '回家模式',
  description: '打开灯光和空调',
  icon: 'home'
});

// 触发场景
await triggerScene(1);

// 添加场景设备关联
await addSceneDevice(1, 10, '{"switch": true}');

// 移除场景设备关联
await removeSceneDevice(1, 10);
```

#### 数据统计
```typescript
import {
  getOverviewStats,
  getDeviceTrend,
  getUserActivity,
  getSceneUsage,
  getContentDistribution,
  getSystemInfo
} from '@/api/dashboard';

// 获取概览统计数据
const overview = await getOverviewStats();
// 返回：{ totalDevices, onlineDevices, totalScenes, ... }

// 获取设备趋势（最近 7 天）
const trend = await getDeviceTrend(7);
// 返回：[{ date, deviceCount, onlineCount, newCount }, ...]

// 获取用户活跃度
const activity = await getUserActivity(7);

// 获取场景使用统计
const usage = await getSceneUsage();

// 获取内容分布统计
const distribution = await getContentDistribution();
```

## 响应格式说明

所有 API 返回统一格式：

```typescript
{
  code: number,      // 状态码：200 表示成功
  message: string,   // 响应消息
  data: any,         // 响应数据（已自动解包）
  timestamp: number  // 时间戳
}
```

前端已通过 Axios 拦截器自动解包 `data` 字段，因此直接获取到实际数据。

## 错误处理

API 调用失败时会自动抛出错误：

```typescript
try {
  const result = await login({ username: 'admin', password: 'wrong' });
} catch (error) {
  console.error('登录失败:', error);
  // 错误信息可通过 error.message 获取
}
```

### 常见错误码

| 错误码 | 说明 |
|--------|------|
| 200 | 成功 |
| 400 | 参数错误 |
| 401 | 未授权，请先登录 |
| 403 | 拒绝访问 |
| 404 | 请求地址不存在 |
| 500 | 服务器内部错误 |

### 业务错误码

| 错误码范围 | 说明 |
|------------|------|
| 1001-1009 | 认证相关错误 |
| 2001-2003 | 设备相关错误 |
| 3001-3003 | 场景相关错误 |
| 4001 | 内容相关错误 |
| 5001 | 推荐相关错误 |
| 6001 | 日志相关错误 |

## 分页参数说明

所有列表接口支持分页，参数统一：

```typescript
{
  page?: number;   // 页码，默认 1
  size?: number;   // 每页大小，默认 10
  keyword?: string; // 搜索关键词（可选）
}
```

返回数据格式：

```typescript
{
  records: T[];    // 当前页数据
  total: number;   // 总记录数
  size: number;    // 每页大小
  current: number; // 当前页码
  pages: number;   // 总页数
}
```

## Token 自动注入

前端已配置 Axios 拦截器，会自动将 Token 注入到请求头：

```
Authorization: Bearer <token>
```

登录成功后，Token 会自动保存到本地存储，后续请求自动携带。

## 枚举值说明

### 设备类型 (DeviceType)

```typescript
enum DeviceType {
  TV = 'TV',           // 电视
  SPEAKER = 'SPEAKER', // 音响
  LIGHT = 'LIGHT',     // 灯光
  AIR_CONDITIONER = 'AIR_CONDITIONER', // 空调
  CURTAIN = 'CURTAIN', // 窗帘
  DOOR_LOCK = 'DOOR_LOCK', // 门锁
  CAMERA = 'CAMERA',   // 摄像头
  SENSOR = 'SENSOR',   // 传感器
  OTHER = 'OTHER'      // 其他
}
```

### 内容类型 (ContentType)

```typescript
enum ContentType {
  MOVIE = 'MOVIE',  // 电影
  MUSIC = 'MUSIC',  // 音乐
  GAME = 'GAME'     // 游戏
}
```

## 最佳实践

1. **统一导入**：建议在组件中统一导入需要的 API 函数
   ```typescript
   import { login, register } from '@/api/auth';
   import { getUserList, deleteUser } from '@/api/user';
   ```

2. **错误处理**：重要操作建议添加 try-catch
   ```typescript
   try {
     await deleteUser(id);
     showToast('success', '删除成功');
   } catch (error) {
     showToast('error', '删除失败');
   }
   ```

3. **类型提示**：使用 TypeScript 接口获得完整类型提示
   ```typescript
   import type { DeviceInfo } from '@/api/device';

   const device: DeviceInfo = await getDeviceById(1);
   ```

4. **参数验证**：在调用 API 前验证必填参数
   ```typescript
   if (!formData.username || !formData.password) {
     showToast('error', '请填写完整信息');
     return;
   }
   ```

## 更新记录

| 日期 | 更新内容 |
|------|----------|
| 2026-03-13 | 新增 preferences.ts, dashboard.ts, system.ts，完善 log.ts |
| 2026-03-12 | 初始版本，创建基础 API 服务层 |
