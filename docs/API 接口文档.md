# API 接口文档

> 智能家居娱乐管理系统 - RESTful API 接口文档
> 版本：v1.2
> 最后更新：2026-03-12

---

## 目录

1. [接口概述](#1-接口概述)
2. [统一响应格式](#2-统一响应格式)
3. [错误码说明](#3-错误码说明)
4. [认证说明](#4-认证说明)
5. [用户认证接口](#5-用户认证接口)
6. [用户管理接口](#6-用户管理接口)
7. [偏好管理接口](#7-偏好管理接口)
8. [日志管理接口](#8-日志管理接口) - Phase 6 已更新
9. [设备管理接口](#9-设备管理接口) - Phase 3
10. [场景管理接口](#10-场景管理接口) - Phase 4
11. [内容管理接口](#11-内容管理接口) - Phase 4
12. [推荐管理接口](#12-推荐管理接口) - Phase 5
13. [数据统计接口](#13-数据统计接口) - Phase 6 已完成
14. [系统配置接口](#14-系统配置接口) - Phase 6

---

## 1. 接口概述

### 1.1 基本信息

| 项目 | 说明 |
|------|------|
| 服务地址 | `http://localhost:8080` |
| API 版本 | v1.0 |
| 接口风格 | RESTful API |
| 数据格式 | JSON |
| 字符编码 | UTF-8 |

### 1.2 API 文档地址

- **Knife4j 文档**: `http://localhost:8080/doc.html`
- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8080/v3/api-docs`

---

## 2. 统一响应格式

所有接口返回统一使用以下格式：

```json
{
  "code": 200,
  "message": "success",
  "data": {},
  "timestamp": 1710234567890
}
```

| 字段 | 类型 | 说明 |
|------|------|------|
| code | Integer | 状态码 |
| message | String | 响应消息 |
| data | Object | 响应数据 |
| timestamp | Long | 时间戳 |

### 2.1 成功响应示例

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "username": "admin"
  },
  "timestamp": 1710234567890
}
```

### 2.2 失败响应示例

```json
{
  "code": 1005,
  "message": "用户名或密码错误",
  "data": null,
  "timestamp": 1710234567890
}
```

---

## 3. 错误码说明

### 3.1 通用错误码

| 错误码 | 说明 |
|--------|------|
| 200 | 操作成功 |
| 400 | 参数错误 |
| 401 | 未授权，请先登录 |
| 403 | 拒绝访问 |
| 404 | 请求地址不存在 |
| 405 | 请求方法错误 |
| 500 | 服务器内部错误 |

### 3.2 认证相关错误码

| 错误码 | 说明 |
|--------|------|
| 1001 | Token 为空 |
| 1002 | Token 无效 |
| 1003 | Token 已过期 |
| 1004 | 登录失败 |
| 1005 | 用户名或密码错误 |
| 1006 | 用户不存在 |
| 1007 | 用户已存在 |
| 1008 | 密码错误 |
| 1009 | 注册失败 |

### 3.3 设备相关错误码

| 错误码 | 说明 |
|--------|------|
| 2001 | 设备不存在 |
| 2002 | 设备已存在 |
| 2003 | 设备状态错误 |

### 3.4 场景相关错误码

| 错误码 | 说明 |
|--------|------|
| 3001 | 场景不存在 |
| 3002 | 场景已存在 |
| 3003 | 场景设备关联不存在 |

### 3.5 内容相关错误码

| 错误码 | 说明 |
|--------|------|
| 4001 | 内容不存在 |

### 3.6 推荐相关错误码

| 错误码 | 说明 |
|--------|------|
| 5001 | 用户偏好不存在 |

### 3.7 日志相关错误码

| 错误码 | 说明 |
|--------|------|
| 6001 | 日志不存在 |

---

## 4. 认证说明

### 4.1 认证方式

采用 JWT (JSON Web Token) 进行认证。

### 4.2 Token 获取

通过登录接口获取 Token：

```bash
POST /api/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}
```

### 4.3 Token 使用

在请求头中携带 Token：

```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

### 4.4 Token 有效期

- 默认有效期：24 小时
- 过期后需重新登录

### 4.5 免认证接口

以下接口无需 Token 即可访问：

| 接口 | 说明 |
|------|------|
| POST /api/auth/register | 用户注册 |
| POST /api/auth/login | 用户登录 |
| GET /doc.html | API 文档页面 |
| GET /v3/api-docs/** | OpenAPI 文档 |
| GET /swagger-ui/** | Swagger UI |

---

## 5. 用户认证接口

### 5.1 用户注册

**接口**: `POST /api/auth/register`

**认证**: 不需要

**请求参数**:

```json
{
  "username": "zhangsan",
  "password": "123456",
  "nickname": "张三"
}
```

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| username | String | 是 | 用户名，3-50 字符 |
| password | String | 是 | 密码，6-20 字符 |
| nickname | String | 否 | 昵称 |

**响应示例**:

```json
{
  "code": 200,
  "message": "注册成功",
  "data": 10,
  "timestamp": 1710234567890
}
```

### 5.2 用户登录

**接口**: `POST /api/auth/login`

**认证**: 不需要

**请求参数**:

```json
{
  "username": "admin",
  "password": "admin123"
}
```

**响应示例**:

```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "tokenType": "Bearer",
    "userId": 1,
    "username": "admin",
    "nickname": "管理员",
    "avatar": null,
    "role": "ADMIN"
  },
  "timestamp": 1710234567890
}
```

---

## 6. 用户管理接口

### 6.1 获取当前用户信息

**接口**: `GET /api/users/me`

**认证**: 需要

**响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "username": "admin",
    "nickname": "管理员",
    "avatar": "https://example.com/avatar.jpg",
    "role": "ADMIN",
    "createTime": "2026-03-12 10:00:00",
    "updateTime": "2026-03-12 12:00:00"
  }
}
```

### 6.2 根据 ID 查询用户

**接口**: `GET /api/users/{id}`

**认证**: 需要

**路径参数**:

| 参数 | 类型 | 说明 |
|------|------|------|
| id | Long | 用户 ID |

**响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "username": "admin",
    "nickname": "管理员",
    "avatar": null,
    "role": "ADMIN",
    "createTime": "2026-03-12 10:00:00",
    "updateTime": "2026-03-12 12:00:00"
  }
}
```

### 6.3 查询用户列表

**接口**: `GET /api/users`

**认证**: 需要

**查询参数**:

| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| page | Integer | 否 | 1 | 页码 |
| size | Integer | 否 | 10 | 每页大小 |
| keyword | String | 否 | - | 搜索关键词 |

**响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "records": [
      {
        "id": 1,
        "username": "admin",
        "nickname": "管理员",
        "role": "ADMIN",
        "createTime": "2026-03-12 10:00:00",
        "updateTime": "2026-03-12 12:00:00"
      }
    ],
    "total": 2,
    "size": 10,
    "current": 1,
    "pages": 1
  }
}
```

### 6.4 更新用户信息

**接口**: `PUT /api/users/{id}`

**认证**: 需要

**路径参数**:

| 参数 | 类型 | 说明 |
|------|------|------|
| id | Long | 用户 ID |

**请求参数**:

```json
{
  "nickname": "新昵称",
  "avatar": "https://example.com/new-avatar.jpg"
}
```

**响应示例**:

```json
{
  "code": 200,
  "message": "更新成功",
  "data": {
    "id": 1,
    "username": "admin",
    "nickname": "新昵称",
    "avatar": "https://example.com/new-avatar.jpg",
    "role": "ADMIN",
    "createTime": "2026-03-12 10:00:00",
    "updateTime": "2026-03-12 14:00:00"
  }
}
```

### 6.5 删除用户

**接口**: `DELETE /api/users/{id}`

**认证**: 需要

**路径参数**:

| 参数 | 类型 | 说明 |
|------|------|------|
| id | Long | 用户 ID |

**响应示例**:

```json
{
  "code": 200,
  "message": "删除成功",
  "data": null,
  "timestamp": 1710234567890
}
```

---

## 7. 偏好管理接口

### 7.1 设置用户偏好

**接口**: `POST /api/preferences?userId={userId}`

**认证**: 需要

**查询参数**:

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| userId | Long | 是 | 用户 ID |

**请求参数**:

```json
{
  "contentType": "MOVIE",
  "preferenceScore": 80
}
```

**响应示例**:

```json
{
  "code": 200,
  "message": "设置成功",
  "data": null,
  "timestamp": 1710234567890
}
```

### 7.2 获取用户偏好列表

**接口**: `GET /api/preferences?userId={userId}`

**认证**: 需要

**查询参数**:

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| userId | Long | 是 | 用户 ID |

**响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "userId": 1,
      "contentType": "MOVIE",
      "preferenceScore": 80,
      "createTime": "2026-03-12 10:00:00",
      "updateTime": "2026-03-12 12:00:00"
    }
  ]
}
```

### 7.3 获取用户指定类型的偏好

**接口**: `GET /api/preferences/{contentType}?userId={userId}`

**认证**: 需要

**路径参数**:

| 参数 | 类型 | 说明 |
|------|------|------|
| contentType | String | 内容类型（MOVIE/MUSIC/GAME） |

**查询参数**:

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| userId | Long | 是 | 用户 ID |

**响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "userId": 1,
    "contentType": "MOVIE",
    "preferenceScore": 80,
    "createTime": "2026-03-12 10:00:00",
    "updateTime": "2026-03-12 12:00:00"
  }
}
```

### 7.4 删除用户偏好

**接口**: `DELETE /api/preferences/{contentType}?userId={userId}`

**认证**: 需要

**路径参数**:

| 参数 | 类型 | 说明 |
|------|------|------|
| contentType | String | 内容类型 |

**查询参数**:

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| userId | Long | 是 | 用户 ID |

**响应示例**:

```json
{
  "code": 200,
  "message": "删除成功",
  "data": null,
  "timestamp": 1710234567890
}
```

---

## 8. 日志管理接口

> Phase 6 已更新

### 8.1 查询操作日志列表

**接口**: `GET /api/logs`

**认证**: 需要

**查询参数**:

| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| page | Integer | 否 | 1 | 页码 |
| size | Integer | 否 | 10 | 每页大小 |
| userId | Long | 否 | - | 用户 ID |
| operation | String | 否 | - | 操作类型 |
| targetType | String | 否 | - | 目标类型 |
| startDate | String | 否 | - | 开始日期 (YYYY-MM-DD) |
| endDate | String | 否 | - | 结束日期 (YYYY-MM-DD) |

**响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "records": [
      {
        "id": 1,
        "userId": 1,
        "username": "admin",
        "operation": "LOGIN",
        "targetType": "USER",
        "targetId": 1,
        "ipAddress": "192.168.1.1",
        "createTime": "2026-03-12 10:00:00"
      }
    ],
    "total": 10,
    "size": 10,
    "current": 1,
    "pages": 1
  }
}
```

### 8.2 根据 ID 查询操作日志

**接口**: `GET /api/logs/{id}`

**认证**: 需要

**路径参数**:

| 参数 | 类型 | 说明 |
|------|------|------|
| id | Long | 日志 ID |

**响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "userId": 1,
    "username": "admin",
    "operation": "LOGIN",
    "targetType": "USER",
    "targetId": 1,
    "ipAddress": "192.168.1.1",
    "createTime": "2026-03-12 10:00:00"
  }
}
```

### 8.3 统计各操作类型的数量

**接口**: `GET /api/logs/stats/operation`

**认证**: 需要

**响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": [
    {"operation": "LOGIN", "count": 5},
    {"operation": "CREATE", "count": 3},
    {"operation": "UPDATE", "count": 2}
  ]
}
```

### 8.4 统计每日日志数量

**接口**: `GET /api/logs/stats/daily?days=7`

**认证**: 需要

**查询参数**:

| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| days | Integer | 否 | 7 | 天数 |

**响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": [
    {"date": "2026-03-06", "count": 10},
    {"date": "2026-03-07", "count": 15},
    {"date": "2026-03-08", "count": 12},
    {"date": "2026-03-09", "count": 8},
    {"date": "2026-03-10", "count": 20},
    {"date": "2026-03-11", "count": 18},
    {"date": "2026-03-12", "count": 25}
  ]
}
```

---

## 9. 设备管理接口

> Phase 3 已完成

### 9.1 查询设备列表

**接口**: `GET /api/devices`

**认证**: 需要

**查询参数**:

| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| page | Integer | 否 | 1 | 页码 |
| size | Integer | 否 | 10 | 每页大小 |
| keyword | String | 否 | - | 搜索关键词（设备名称） |
| type | String | 否 | - | 设备类型（TV/SPEAKER/LIGHT 等） |
| room | String | 否 | - | 房间 |
| status | Integer | 否 | - | 状态（0-禁用，1-启用） |
| isOnline | Integer | 否 | - | 在线状态（0-离线，1-在线） |
| userId | Long | 否 | - | 用户 ID |

**响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "records": [
      {
        "id": 1,
        "name": "客厅电视",
        "type": "TV",
        "room": "客厅",
        "status": 1,
        "isOnline": 1,
        "userId": 1,
        "createTime": "2026-03-12 10:00:00",
        "updateTime": "2026-03-12 12:00:00"
      }
    ],
    "total": 5,
    "size": 10,
    "current": 1,
    "pages": 1
  }
}
```

### 9.2 查询设备详情

**接口**: `GET /api/devices/{id}`

**认证**: 需要

**路径参数**:

| 参数 | 类型 | 说明 |
|------|------|------|
| id | Long | 设备 ID |

**响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "name": "客厅电视",
    "type": "TV",
    "room": "客厅",
    "status": 1,
    "isOnline": 1,
    "userId": 1,
    "createTime": "2026-03-12 10:00:00",
    "updateTime": "2026-03-12 12:00:00"
  }
}
```

### 9.3 创建设备

**接口**: `POST /api/devices?userId={userId}`

**认证**: 需要

**查询参数**:

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| userId | Long | 是 | 用户 ID |

**请求参数**:

```json
{
  "name": "客厅电视",
  "type": "TV",
  "room": "客厅",
  "status": 1
}
```

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| name | String | 是 | 设备名称 |
| type | String | 是 | 设备类型（TV/SPEAKER/LIGHT 等） |
| room | String | 否 | 房间 |
| status | Integer | 否 | 状态（0-禁用，1-启用），默认 1 |

**响应示例**:

```json
{
  "code": 200,
  "message": "创建成功",
  "data": 1,
  "timestamp": 1710234567890
}
```

### 9.4 更新设备信息

**接口**: `PUT /api/devices/{id}`

**认证**: 需要

**路径参数**:

| 参数 | 类型 | 说明 |
|------|------|------|
| id | Long | 设备 ID |

**请求参数**:

```json
{
  "name": "新客厅电视",
  "room": "大客厅",
  "status": 1
}
```

**响应示例**:

```json
{
  "code": 200,
  "message": "更新成功",
  "data": {
    "id": 1,
    "name": "新客厅电视",
    "type": "TV",
    "room": "大客厅",
    "status": 1,
    "isOnline": 1,
    "userId": 1,
    "createTime": "2026-03-12 10:00:00",
    "updateTime": "2026-03-12 14:00:00"
  }
}
```

### 9.5 删除设备

**接口**: `DELETE /api/devices/{id}`

**认证**: 需要

**路径参数**:

| 参数 | 类型 | 说明 |
|------|------|------|
| id | Long | 设备 ID |

**响应示例**:

```json
{
  "code": 200,
  "message": "删除成功",
  "data": null,
  "timestamp": 1710234567890
}
```

### 9.6 更新设备状态

**接口**: `PUT /api/devices/{id}/status`

**认证**: 需要

**路径参数**:

| 参数 | 类型 | 说明 |
|------|------|------|
| id | Long | 设备 ID |

**请求参数**:

```json
{
  "status": 1,
  "isOnline": 1
}
```

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| status | Integer | 否 | 状态（0-禁用，1-启用） |
| isOnline | Integer | 否 | 在线状态（0-离线，1-在线） |

**响应示例**:

```json
{
  "code": 200,
  "message": "状态更新成功",
  "data": {
    "id": 1,
    "name": "客厅电视",
    "type": "TV",
    "room": "客厅",
    "status": 1,
    "isOnline": 1,
    "userId": 1,
    "createTime": "2026-03-12 10:00:00",
    "updateTime": "2026-03-12 14:00:00"
  }
}
```

### 9.7 统计各类型设备数量

**接口**: `GET /api/devices/statistics/types`

**认证**: 需要

**响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": [
    {"name": "TV", "count": 3},
    {"name": "SPEAKER", "count": 2},
    {"name": "LIGHT", "count": 5}
  ]
}
```

### 9.8 统计各房间设备数量

**接口**: `GET /api/devices/statistics/rooms`

**认证**: 需要

**响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": [
    {"name": "客厅", "count": 5},
    {"name": "卧室", "count": 3},
    {"name": "厨房", "count": 2}
  ]
}
```

---

## 10. 场景管理接口

> Phase 4 已完成

### 10.1 查询场景列表

**接口**: `GET /api/scenes`

**认证**: 需要

**查询参数**:

| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| page | Integer | 否 | 1 | 页码 |
| size | Integer | 否 | 10 | 每页大小 |
| userId | Long | 否 | - | 用户 ID |

**响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "records": [
      {
        "id": 1,
        "name": "回家模式",
        "description": "打开灯光和空调，营造温馨氛围",
        "icon": "home",
        "isEnabled": 1,
        "userId": 1,
        "createTime": "2026-03-12 10:00:00",
        "updateTime": "2026-03-12 12:00:00",
        "devices": [
          {
            "deviceId": 1,
            "deviceName": "客厅灯",
            "config": "{\"switch\": true}"
          }
        ]
      }
    ],
    "total": 5,
    "size": 10,
    "current": 1,
    "pages": 1
  }
}
```

### 10.2 查询场景详情

**接口**: `GET /api/scenes/{id}`

**认证**: 需要

**路径参数**:

| 参数 | 类型 | 说明 |
|------|------|------|
| id | Long | 场景 ID |

**响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "name": "回家模式",
    "description": "打开灯光和空调，营造温馨氛围",
    "icon": "home",
    "isEnabled": 1,
    "userId": 1,
    "createTime": "2026-03-12 10:00:00",
    "updateTime": "2026-03-12 12:00:00",
    "devices": [
      {
        "id": 1,
        "deviceId": 1,
        "deviceName": "客厅灯",
        "config": "{\"switch\": true}"
      },
      {
        "id": 2,
        "deviceId": 2,
        "deviceName": "空调",
        "config": "{\"temperature\": 26}"
      }
    ]
  }
}
```

### 10.3 创建场景

**接口**: `POST /api/scenes?userId={userId}`

**认证**: 需要

**查询参数**:

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| userId | Long | 是 | 用户 ID |

**请求参数**:

```json
{
  "name": "影院模式",
  "description": "调暗灯光，打开音响和电视",
  "icon": "movie"
}
```

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| name | String | 是 | 场景名称 |
| description | String | 否 | 场景描述 |
| icon | String | 否 | 场景图标 |

**响应示例**:

```json
{
  "code": 200,
  "message": "创建成功",
  "data": 1,
  "timestamp": 1710234567890
}
```

### 10.4 更新场景信息

**接口**: `PUT /api/scenes/{id}`

**认证**: 需要

**路径参数**:

| 参数 | 类型 | 说明 |
|------|------|------|
| id | Long | 场景 ID |

**请求参数**:

```json
{
  "name": "新影院模式",
  "description": "全新的影院场景体验",
  "icon": "cinema"
}
```

**响应示例**:

```json
{
  "code": 200,
  "message": "更新成功",
  "data": {
    "id": 1,
    "name": "新影院模式",
    "description": "全新的影院场景体验",
    "icon": "cinema",
    "isEnabled": 1,
    "userId": 1,
    "createTime": "2026-03-12 10:00:00",
    "updateTime": "2026-03-12 14:00:00"
  }
}
```

### 10.5 删除场景

**接口**: `DELETE /api/scenes/{id}`

**认证**: 需要

**路径参数**:

| 参数 | 类型 | 说明 |
|------|------|------|
| id | Long | 场景 ID |

**响应示例**:

```json
{
  "code": 200,
  "message": "删除成功",
  "data": null,
  "timestamp": 1710234567890
}
```

### 10.6 启用/禁用场景

**接口**: `POST /api/scenes/{id}/toggle`

**认证**: 需要

**路径参数**:

| 参数 | 类型 | 说明 |
|------|------|------|
| id | Long | 场景 ID |

**响应示例**:

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "name": "回家模式",
    "description": "打开灯光和空调，营造温馨氛围",
    "icon": "home",
    "isEnabled": 0,
    "userId": 1,
    "createTime": "2026-03-12 10:00:00",
    "updateTime": "2026-03-12 14:00:00"
  }
}
```

### 10.7 触发场景

**接口**: `POST /api/scenes/{id}/trigger`

**认证**: 需要

**路径参数**:

| 参数 | 类型 | 说明 |
|------|------|------|
| id | Long | 场景 ID |

**响应示例**:

```json
{
  "code": 200,
  "message": "场景触发成功",
  "data": null,
  "timestamp": 1710234567890
}
```

### 10.8 添加场景设备关联

**接口**: `POST /api/scenes/{id}/devices?deviceId={deviceId}&config={config}`

**认证**: 需要

**路径参数**:

| 参数 | 类型 | 说明 |
|------|------|------|
| id | Long | 场景 ID |

**查询参数**:

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| deviceId | Long | 是 | 设备 ID |
| config | String | 否 | 设备配置（JSON 格式） |

**响应示例**:

```json
{
  "code": 200,
  "message": "关联成功",
  "data": null,
  "timestamp": 1710234567890
}
```

### 10.9 移除场景设备关联

**接口**: `DELETE /api/scenes/{id}/devices?deviceId={deviceId}`

**认证**: 需要

**路径参数**:

| 参数 | 类型 | 说明 |
|------|------|------|
| id | Long | 场景 ID |

**查询参数**:

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| deviceId | Long | 是 | 设备 ID |

**响应示例**:

```json
{
  "code": 200,
  "message": "移除成功",
  "data": null,
  "timestamp": 1710234567890
}
```

---

## 11. 内容管理接口

> Phase 5 已完成

### 11.1 查询内容列表

**接口**: `GET /api/contents`

**认证**: 需要

**查询参数**:

| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| page | Integer | 否 | 1 | 页码 |
| size | Integer | 否 | 10 | 每页大小 |
| keyword | String | 否 | - | 搜索关键词（内容标题） |
| type | String | 否 | - | 内容类型（MOVIE/MUSIC/GAME） |

**响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "records": [
      {
        "id": 1,
        "title": "流浪地球 2",
        "type": "MOVIE",
        "cover": "https://example.com/cover1.jpg",
        "description": "科幻冒险电影",
        "rating": 4.5,
        "createTime": "2026-03-12 10:00:00",
        "updateTime": "2026-03-12 12:00:00"
      }
    ],
    "total": 8,
    "size": 10,
    "current": 1,
    "pages": 1
  }
}
```

### 11.2 查询内容详情

**接口**: `GET /api/contents/{id}`

**认证**: 需要

**路径参数**:

| 参数 | 类型 | 说明 |
|------|------|------|
| id | Long | 内容 ID |

**响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "title": "流浪地球 2",
    "type": "MOVIE",
    "cover": "https://example.com/cover1.jpg",
    "description": "科幻冒险电影",
    "rating": 4.5,
    "createTime": "2026-03-12 10:00:00",
    "updateTime": "2026-03-12 12:00:00"
  }
}
```

### 11.3 创建内容

**接口**: `POST /api/contents`

**认证**: 需要

**请求参数**:

```json
{
  "title": "流浪地球 2",
  "type": "MOVIE",
  "cover": "https://example.com/cover1.jpg",
  "description": "科幻冒险电影",
  "rating": 4.5
}
```

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| title | String | 是 | 内容标题 |
| type | String | 是 | 内容类型（MOVIE/MUSIC/GAME） |
| cover | String | 否 | 封面 URL |
| description | String | 否 | 内容描述 |
| rating | BigDecimal | 否 | 评分（0.0-5.0） |

**响应示例**:

```json
{
  "code": 200,
  "message": "创建成功",
  "data": 1,
  "timestamp": 1710234567890
}
```

### 11.4 更新内容信息

**接口**: `PUT /api/contents/{id}`

**认证**: 需要

**路径参数**:

| 参数 | 类型 | 说明 |
|------|------|------|
| id | Long | 内容 ID |

**请求参数**:

```json
{
  "title": "流浪地球 2 IMAX 版",
  "rating": 4.8
}
```

**响应示例**:

```json
{
  "code": 200,
  "message": "更新成功",
  "data": {
    "id": 1,
    "title": "流浪地球 2 IMAX 版",
    "type": "MOVIE",
    "cover": "https://example.com/cover1.jpg",
    "description": "科幻冒险电影",
    "rating": 4.8,
    "createTime": "2026-03-12 10:00:00",
    "updateTime": "2026-03-12 14:00:00"
  }
}
```

### 11.5 删除内容

**接口**: `DELETE /api/contents/{id}`

**认证**: 需要

**路径参数**:

| 参数 | 类型 | 说明 |
|------|------|------|
| id | Long | 内容 ID |

**响应示例**:

```json
{
  "code": 200,
  "message": "删除成功",
  "data": null,
  "timestamp": 1710234567890
}
```

---

## 12. 推荐管理接口

> Phase 5 已完成

### 12.1 获取推荐列表

**接口**: `GET /api/recommendations?userId={userId}`

**认证**: 需要

**查询参数**:

| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| userId | Long | 是 | - | 用户 ID |
| page | Integer | 否 | 1 | 页码 |
| size | Integer | 否 | 10 | 每页大小 |

**响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "records": [
      {
        "id": 1,
        "userId": 1,
        "contentId": 1,
        "contentTitle": "流浪地球 2",
        "contentType": "MOVIE",
        "contentCover": "https://example.com/cover1.jpg",
        "reason": "高度匹配您的偏好",
        "score": 85,
        "isClicked": 0,
        "isLiked": 0,
        "createTime": "2026-03-12 10:00:00"
      }
    ],
    "total": 5,
    "size": 10,
    "current": 1,
    "pages": 1
  }
}
```

### 12.2 记录推荐点击

**接口**: `POST /api/recommendations/click?userId={userId}&contentId={contentId}`

**认证**: 需要

**查询参数**:

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| userId | Long | 是 | 用户 ID |
| contentId | Long | 是 | 内容 ID |

**响应示例**:

```json
{
  "code": 200,
  "message": "已记录点击",
  "data": null,
  "timestamp": 1710234567890
}
```

### 12.3 记录推荐喜欢

**接口**: `POST /api/recommendations/like?userId={userId}&contentId={contentId}`

**认证**: 需要

**查询参数**:

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| userId | Long | 是 | 用户 ID |
| contentId | Long | 是 | 内容 ID |

**响应示例**:

```json
{
  "code": 200,
  "message": "已记录喜欢",
  "data": null,
  "timestamp": 1710234567890
}
```

### 12.4 记录不喜欢

**接口**: `POST /api/recommendations/dislike?userId={userId}&contentId={contentId}`

**认证**: 需要

**查询参数**:

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| userId | Long | 是 | 用户 ID |
| contentId | Long | 是 | 内容 ID |

**响应示例**:

```json
{
  "code": 200,
  "message": "已记录不喜欢",
  "data": null,
  "timestamp": 1710234567890
}
```

### 12.5 提交推荐反馈

**接口**: `POST /api/recommendations/feedback`

**认证**: 需要

**请求参数**:

```json
{
  "userId": 1,
  "contentId": 1,
  "feedbackType": "LIKE",
  "score": 5
}
```

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| userId | Long | 是 | 用户 ID |
| contentId | Long | 是 | 内容 ID |
| feedbackType | String | 是 | 反馈类型（LIKE/DISLIKE/CLICK/COLLECT） |
| score | Integer | 否 | 反馈分数（1-5） |

**响应示例**:

```json
{
  "code": 200,
  "message": "反馈提交成功",
  "data": null,
  "timestamp": 1710234567890
}
```

### 12.6 获取用户反馈历史

**接口**: `GET /api/recommendations/feedback/history?userId={userId}`

**认证**: 需要

**查询参数**:

| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| userId | Long | 是 | - | 用户 ID |
| page | Integer | 否 | 1 | 页码 |
| size | Integer | 否 | 10 | 每页大小 |

**响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "records": [
      {
        "id": 1,
        "userId": 1,
        "contentId": 1,
        "contentTitle": "流浪地球 2",
        "feedbackType": "LIKE",
        "score": 5,
        "createTime": "2026-03-12 10:00:00"
      }
    ],
    "total": 5,
    "size": 10,
    "current": 1,
    "pages": 1
  }
}
```

---

## 13. 数据统计接口

> Phase 6 已完成

### 13.1 获取概览统计数据

**接口**: `GET /api/dashboard/overview`

**认证**: 需要

**响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "totalDevices": 10,
    "onlineDevices": 7,
    "offlineDevices": 3,
    "totalScenes": 5,
    "enabledScenes": 4,
    "totalContents": 20,
    "totalUsers": 8,
    "todayLogs": 15,
    "onlineRate": 70.0
  }
}
```

### 13.2 获取设备趋势数据

**接口**: `GET /api/dashboard/device-trend?days=7`

**认证**: 需要

**查询参数**:

| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| days | Integer | 否 | 7 | 天数 |

**响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": [
    {"date": "2026-03-06", "deviceCount": 8, "onlineCount": 5, "newCount": 0},
    {"date": "2026-03-07", "deviceCount": 8, "onlineCount": 6, "newCount": 0},
    {"date": "2026-03-08", "deviceCount": 9, "onlineCount": 6, "newCount": 1},
    {"date": "2026-03-09", "deviceCount": 9, "onlineCount": 7, "newCount": 0},
    {"date": "2026-03-10", "deviceCount": 10, "onlineCount": 7, "newCount": 1},
    {"date": "2026-03-11", "deviceCount": 10, "onlineCount": 7, "newCount": 0},
    {"date": "2026-03-12", "deviceCount": 10, "onlineCount": 7, "newCount": 0}
  ]
}
```

### 13.3 获取用户活跃度数据

**接口**: `GET /api/dashboard/user-activity?days=7`

**认证**: 需要

**查询参数**:

| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| days | Integer | 否 | 7 | 天数 |

**响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": [
    {"date": "2026-03-06", "operationCount": 10, "activeUsers": 3, "newUsers": 0},
    {"date": "2026-03-07", "operationCount": 15, "activeUsers": 4, "newUsers": 0},
    {"date": "2026-03-08", "operationCount": 12, "activeUsers": 3, "newUsers": 0},
    {"date": "2026-03-09", "operationCount": 8, "activeUsers": 2, "newUsers": 0},
    {"date": "2026-03-10", "operationCount": 20, "activeUsers": 5, "newUsers": 1},
    {"date": "2026-03-11", "operationCount": 18, "activeUsers": 4, "newUsers": 0},
    {"date": "2026-03-12", "operationCount": 25, "activeUsers": 6, "newUsers": 0}
  ]
}
```

### 13.4 获取场景使用统计

**接口**: `GET /api/dashboard/scene-usage`

**认证**: 需要

**响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": [
    {"sceneId": 1, "sceneName": "回家模式", "triggerCount": 0},
    {"sceneId": 2, "sceneName": "影院模式", "triggerCount": 0},
    {"sceneId": 3, "sceneName": "睡眠模式", "triggerCount": 0}
  ]
}
```

### 13.5 获取内容分布统计

**接口**: `GET /api/dashboard/content-distribution`

**认证**: 需要

**响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": [
    {"type": "MOVIE", "count": 10},
    {"type": "MUSIC", "count": 5},
    {"type": "GAME", "count": 5}
  ]
}
```

### 13.6 获取系统信息

**接口**: `GET /api/dashboard/system-info`

**认证**: 需要

**响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "javaVersion": "17.0.10",
    "javaVendor": "Oracle Corporation",
    "osName": "Windows 10",
    "osVersion": "10.0",
    "availableProcessors": "8",
    "maxMemory": "4096 MB",
    "freeMemory": "2048 MB",
    "totalMemory": "3072 MB"
  }
}
```

### 13.7 获取设备统计概览

**接口**: `GET /api/device-statistics/overview`

**认证**: 需要

**响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "total": 10,
    "online": 7,
    "offline": 3
  }
}
```

---

## 14. 系统配置接口

> Phase 6 新增

### 14.1 根据键获取配置值

**接口**: `GET /api/system/configs/{configKey}`

**认证**: 需要

**路径参数**:

| 参数 | 类型 | 说明 |
|------|------|------|
| configKey | String | 配置键 |

**响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": "智能家居娱乐管理系统"
}
```

### 14.2 设置配置值

**接口**: `POST /api/system/configs`

**认证**: 需要

**请求参数**:

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| configKey | String | 是 | 配置键 |
| configValue | String | 是 | 配置值 |
| description | String | 否 | 配置描述 |

**响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": null,
  "timestamp": 1710234567890
}
```

### 14.3 获取所有系统配置

**接口**: `GET /api/system/configs`

**认证**: 需要

**响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "configKey": "app.name",
      "configValue": "智能家居娱乐管理系统",
      "description": "系统名称",
      "configType": "SYSTEM",
      "deleted": 0,
      "createTime": "2026-03-12 10:00:00",
      "updateTime": "2026-03-12 12:00:00"
    },
    {
      "id": 2,
      "configKey": "app.version",
      "configValue": "1.0.0",
      "description": "系统版本",
      "configType": "SYSTEM",
      "deleted": 0,
      "createTime": "2026-03-12 10:00:00",
      "updateTime": "2026-03-12 12:00:00"
    }
  ]
}
```

### 14.4 删除配置

**接口**: `DELETE /api/system/configs/{configKey}`

**认证**: 需要

**路径参数**:

| 参数 | 类型 | 说明 |
|------|------|------|
| configKey | String | 配置键 |

**响应示例**:

```json
{
  "code": 200,
  "message": "删除成功",
  "data": null,
  "timestamp": 1710234567890
}
```

### 13.2 按类型统计设备

**接口**: `GET /api/device-statistics/types`

**认证**: 需要

**响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": [
    {"name": "TV", "count": 3},
    {"name": "SPEAKER", "count": 2},
    {"name": "LIGHT", "count": 5}
  ]
}
```

### 13.3 按房间统计设备

**接口**: `GET /api/device-statistics/rooms`

**认证**: 需要

**响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": [
    {"name": "客厅", "count": 5},
    {"name": "卧室", "count": 3},
    {"name": "厨房", "count": 2}
  ]
}
```

### 13.4 按状态统计设备

**接口**: `GET /api/device-statistics/status`

**认证**: 需要

**响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": [
    {"name": "启用", "count": 8},
    {"name": "禁用", "count": 2}
  ]
}
```

---

## 附录

### A. 设备类型枚举

| 类型 | 说明 |
|------|------|
| TV | 电视 |
| SPEAKER | 音响 |
| LIGHT | 灯光 |
| AIR_CONDITIONER | 空调 |
| CURTAIN | 窗帘 |
| DOOR_LOCK | 门锁 |
| CAMERA | 摄像头 |
| SENSOR | 传感器 |
| OTHER | 其他 |

### B. 内容类型枚举

| 类型 | 说明 |
|------|------|
| MOVIE | 电影 |
| MUSIC | 音乐 |
| GAME | 游戏 |

### C. 操作类型枚举

| 类型 | 说明 |
|------|------|
| REGISTER | 注册 |
| LOGIN | 登录 |
| LOGOUT | 登出 |
| CREATE | 创建 |
| UPDATE | 更新 |
| DELETE | 删除 |
| QUERY | 查询 |

---

**文档版本**: v1.0
**最后更新**: 2026-03-12
