# 智能家居娱乐管理系统 - PRD v3.0

## 1. 产品概述

### 1.1 产品定位
基于 Spring Boot 的智能家居娱乐管理系统，提供设备信息管理、场景配置、内容推荐和用户管理的 Web 应用系统，配套微信小程序客户端。本系统专注于业务逻辑的 CRUD 操作，不涉及真实物联网设备通信。

### 1.2 目标用户
- 需要管理家庭娱乐设备信息的用户
- 希望配置和管理场景模式的家庭
- 需要内容推荐功能的用户
- 希望通过微信小程序便捷控制的家庭成员

### 1.3 核心价值
- 提供设备信息的集中化管理
- 支持场景配置与管理
- 提供个性化内容推荐
- 多用户权限管理
- 微信小程序便捷访问

---

## 2. 功能需求

### 2.1 设备管理模块

| 功能点 | 描述 | 优先级 |
|--------|------|--------|
| 设备列表 | 查看所有设备，支持分页、搜索、筛选 | P0 |
| 设备详情 | 查看设备详细信息 | P0 |
| 设备新增 | 添加新设备（名称、类型、房间、状态等） | P0 |
| 设备编辑 | 修改设备信息 | P0 |
| 设备删除 | 删除设备 | P0 |
| 设备状态管理 | 手动设置设备状态（在线/离线、启用/禁用） | P0 |
| 设备分类 | 按类型（电视、音响、灯光等）或房间分类 | P1 |
| 设备搜索 | 按名称、类型搜索设备 | P0 |

### 2.2 场景管理模块

| 功能点 | 描述 | 优先级 |
|--------|------|--------|
| 场景列表 | 查看所有场景，支持分页、筛选 | P0 |
| 场景详情 | 查看场景详细信息（包含关联设备） | P0 |
| 场景新增 | 创建新场景（名称、描述、关联设备列表） | P0 |
| 场景编辑 | 修改场景配置 | P0 |
| 场景删除 | 删除场景 | P0 |
| 场景启用/禁用 | 手动启用或禁用场景 | P0 |
| 定时配置 | 设置场景定时触发（时间、重复周期） | P1 |
| 预设场景 | 系统预置常用场景模板 | P1 |

### 2.3 内容管理模块

| 功能点 | 描述 | 优先级 |
|--------|------|--------|
| 内容列表 | 查看影视、音乐、游戏等内容 | P0 |
| 内容详情 | 查看内容详细信息 | P0 |
| 内容新增 | 添加新内容（标题、类型、封面、描述等） | P0 |
| 内容编辑 | 修改内容信息 | P0 |
| 内容删除 | 删除内容 | P0 |
| 内容分类 | 按类型（电影、音乐、游戏）分类管理 | P0 |
| 内容搜索 | 按标题、类型搜索内容 | P0 |

### 2.4 用户管理模块

| 功能点 | 描述 | 优先级 |
|--------|------|--------|
| 用户注册 | 新用户注册账号 | P0 |
| 用户登录 | 账号密码登录，返回 JWT Token | P0 |
| 用户列表 | 查看所有用户，支持分页、搜索 | P0 |
| 用户详情 | 查看用户详细信息 | P0 |
| 用户编辑 | 修改用户信息（昵称、头像等） | P0 |
| 用户删除 | 删除用户 | P0 |
| 角色管理 | 管理员/普通用户角色分配 | P0 |
| 权限控制 | 基于角色的接口访问控制 | P0 |

### 2.5 推荐管理模块

| 功能点 | 描述 | 优先级 |
|--------|------|--------|
| 用户偏好设置 | 用户设置喜欢的内容类型 | P0 |
| 推荐列表 | 根据用户偏好展示推荐内容 | P0 |
| 推荐记录 | 记录用户浏览、收藏、评分行为 | P1 |
| 推荐反馈 | 用户对推荐内容进行评分/不喜欢 | P1 |

### 2.6 系统管理模块

| 功能点 | 描述 | 优先级 |
|--------|------|--------|
| 操作日志 | 记录用户操作日志（增删改查） | P0 |
| 日志列表 | 查看操作日志，支持筛选、搜索 | P0 |
| 数据统计 | 设备数量、用户数量、场景数量等统计 | P1 |
| 系统配置 | 系统参数配置（如分页大小等） | P2 |

---

## 3. 客户端功能需求（微信小程序）

### 3.1 首页模块

| 功能点 | 描述 | 优先级 |
|--------|------|--------|
| 家庭概览 | 显示设备总数、在线数、场景数等 | P0 |
| 常用场景 | 快速触发常用场景 | P0 |
| 快捷设备 | 快速控制常用设备开关 | P0 |
| 欢迎语 | 根据时间显示问候语 | P1 |

### 3.2 设备控制模块

| 功能点 | 描述 | 优先级 |
|--------|------|--------|
| 设备列表 | 查看所有设备，按房间或类型分组 | P0 |
| 设备开关 | 快速控制设备启用/禁用 | P0 |
| 设备详情 | 查看设备详细信息 | P0 |
| 设备搜索 | 搜索设备 | P1 |

### 3.3 场景模块

| 功能点 | 描述 | 优先级 |
|--------|------|--------|
| 场景列表 | 查看所有场景 | P0 |
| 场景触发 | 一键激活场景 | P0 |
| 场景详情 | 查看场景包含的设备 | P0 |
| 我的场景 | 查看和管理自定义场景 | P1 |

### 3.4 推荐模块

| 功能点 | 描述 | 优先级 |
|--------|------|--------|
| 每日推荐 | 展示推荐内容 | P0 |
| 内容详情 | 查看内容详情 | P0 |
| 偏好设置 | 设置内容偏好 | P1 |
| 收藏记录 | 查看收藏的内容 | P1 |

### 3.5 个人中心模块

| 功能点 | 描述 | 优先级 |
|--------|------|--------|
| 用户信息 | 显示头像、昵称 | P0 |
| 家庭成员 | 查看家庭成员列表 | P0 |
| 操作记录 | 查看个人操作历史 | P1 |
| 设置 | 修改个人信息、退出登录 | P0 |

---

## 4. 非功能需求

### 4.1 性能要求
- 接口响应时间 < 200ms
- 支持 100+ 并发访问
- 数据库查询优化，支持万级数据

### 4.2 可用性要求
- 系统可用性 > 99%
- 友好的错误提示
- 小程序首屏加载时间 < 2s

### 4.3 扩展性要求
- 模块化设计，支持功能扩展
- 预留扩展接口

### 4.4 安全要求
- 采用 HTTPS 加密通信
- 用户密码加密存储（BCrypt）
- JWT Token 认证
- 接口权限校验
- 小程序登录态管理

---

## 5. 技术架构

### 5.1 整体架构
```
┌─────────────────┐     ┌─────────────────┐
│   Web 管理后台   │────▶│                 │
│   (Vue 3)       │     │                 │
├─────────────────┤     │                 │
│   微信小程序     │────▶│  Spring Boot    │
│                 │     │  后端服务        │
└─────────────────┘     └────────┬────────┘
                                 │
                    ┌────────────┴────────────┐
                    ▼                         ▼
              ┌──────────┐           ┌──────────┐
              │  MySQL   │           │  Redis   │
              │  数据库   │           │  缓存    │
              └──────────           └──────────┘
```

### 5.2 后端技术栈
- 框架：Spring Boot 3.x
- 数据库：MySQL 8.x
- ORM：MyBatis-Plus / JPA
- 缓存：Redis（可选）
- 安全：Spring Security + JWT
- API 文档：Swagger / Knife4j
- API 风格：RESTful API

### 5.3 Web 管理后台技术栈
- 框架：Vue 3 + Vite
- UI 组件：Element Plus
- 状态管理：Pinia
- 路由：Vue Router
- HTTP 请求：Axios

### 5.4 微信小程序技术栈
- 框架：微信小程序原生框架
- 语言：WXML + WXSS + JavaScript
- 组件库：Vant Weapp / TDesign
- 状态管理：MobX-miniprogram（可选）
- 网络请求：微信小程序 wx.request

### 5.5 项目结构

**后端项目结构：**
```
src/main/java/com/example/iot/
├── controller/          # 控制器层（REST API）
├── service/            # 业务逻辑层
│   └── impl/          # 服务实现
├── mapper/            # 数据访问层
├── entity/            # 实体类
├── dto/               # 数据传输对象
├── vo/                # 视图对象
├── config/            # 配置类
├── security/          # 安全相关
├── common/            # 公共类
│   ├── result/       # 统一响应
│   └── exception/    # 异常处理
└── util/              # 工具类
```

**Web 管理后台结构：**
```
src/
├── api/              # API 接口
├── assets/           # 静态资源
├── components/       # 公共组件
├── views/            # 页面组件
│   ├── dashboard/   # 首页
│   ├── device/      # 设备管理
│   ├── scene/       # 场景管理
│   ├── content/     # 内容管理
│   ├── recommend/   # 推荐管理
│   ├── user/        # 用户管理
│   └── log/         # 日志管理
├── stores/           # 状态管理
├── router/           # 路由配置
├── utils/            # 工具函数
└── styles/           # 全局样式
```

**微信小程序结构：**
```
miniprogram/
├── pages/            # 页面
│   ├── index/       # 首页
│   ├── device/      # 设备页
│   ├── scene/       # 场景页
│   ├── recommend/   # 推荐页
│   └── mine/        # 个人中心
├── components/       # 自定义组件
├── utils/            # 工具函数
├── api/              # API 接口
├── styles/           # 全局样式
└── images/           # 图片资源
```

### 5.6 核心实体设计
- **User** - 用户（id, username, password, nickname, avatar, role, create_time）
- **Device** - 设备（id, name, type, room, status, is_online, user_id, create_time）
- **Scene** - 场景（id, name, description, icon, is_enabled, user_id, create_time）
- **SceneDevice** - 场景设备关联（id, scene_id, device_id, config）
- **Content** - 内容（id, title, type, cover, description, rating, create_time）
- **UserPreference** - 用户偏好（id, user_id, content_type, preference_score）
- **OperationLog** - 操作日志（id, user_id, operation, target_type, target_id, create_time）

---

## 6. API 接口设计

### 6.1 用户相关
- POST /api/auth/register - 用户注册
- POST /api/auth/login - 用户登录
- POST /api/auth/wechat - 微信登录
- GET /api/users - 用户列表
- GET /api/users/{id} - 用户详情
- PUT /api/users/{id} - 更新用户
- DELETE /api/users/{id} - 删除用户

### 6.2 设备相关
- GET /api/devices - 设备列表
- GET /api/devices/{id} - 设备详情
- POST /api/devices - 创建设备
- PUT /api/devices/{id} - 更新设备
- DELETE /api/devices/{id} - 删除设备
- PUT /api/devices/{id}/status - 更新设备状态

### 6.3 场景相关
- GET /api/scenes - 场景列表
- GET /api/scenes/{id} - 场景详情
- POST /api/scenes - 创建场景
- PUT /api/scenes/{id} - 更新场景
- DELETE /api/scenes/{id} - 删除场景
- POST /api/scenes/{id}/toggle - 启用/禁用场景
- POST /api/scenes/{id}/trigger - 触发场景

### 6.4 内容相关
- GET /api/contents - 内容列表
- GET /api/contents/{id} - 内容详情
- POST /api/contents - 创建内容
- PUT /api/contents/{id} - 更新内容
- DELETE /api/contents/{id} - 删除内容

### 6.5 推荐相关
- GET /api/recommendations - 推荐列表
- POST /api/preferences - 设置偏好
- GET /api/preferences - 获取偏好

### 6.6 日志相关
- GET /api/logs - 日志列表
- GET /api/logs/{id} - 日志详情

---

## 7. 里程碑规划

| 阶段 | 内容 | 交付物 |
|------|------|--------|
| Phase 1 | 项目初始化、数据库设计 | 项目骨架、SQL 脚本 |
| Phase 2 | 用户模块开发 | 注册登录、用户 CRUD |
| Phase 3 | 设备模块开发 | 设备 CRUD、状态管理 |
| Phase 4 | 场景模块开发 | 场景 CRUD、设备关联 |
| Phase 5 | 内容与推荐模块 | 内容 CRUD、偏好管理 |
| Phase 6 | 日志与统计 | 操作日志、数据统计 |
| Phase 7 | Web 管理后台开发 | Vue3 管理后台 |
| Phase 8 | 微信小程序开发 | 小程序客户端 |
| Phase 9 | 联调与测试 | 完整系统 |

---

## 8. 数据库设计

### 8.1 用户表 (user)
```sql
CREATE TABLE user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    nickname VARCHAR(50),
    avatar VARCHAR(255),
    role VARCHAR(20) DEFAULT 'USER',
    openid VARCHAR(100),        -- 微信 openid
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### 8.2 设备表 (device)
```sql
CREATE TABLE device (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(50) NOT NULL,
    room VARCHAR(50),
    status TINYINT DEFAULT 1,
    is_online TINYINT DEFAULT 0,
    user_id BIGINT,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(id)
);
```

### 8.3 场景表 (scene)
```sql
CREATE TABLE scene (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    icon VARCHAR(50),
    is_enabled TINYINT DEFAULT 1,
    user_id BIGINT,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(id)
);
```

### 8.4 场景设备关联表 (scene_device)
```sql
CREATE TABLE scene_device (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    scene_id BIGINT NOT NULL,
    device_id BIGINT NOT NULL,
    config JSON,
    FOREIGN KEY (scene_id) REFERENCES scene(id),
    FOREIGN KEY (device_id) REFERENCES device(id)
);
```

### 8.5 内容表 (content)
```sql
CREATE TABLE content (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(100) NOT NULL,
    type VARCHAR(20) NOT NULL,
    cover VARCHAR(255),
    description TEXT,
    rating DECIMAL(2,1),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
);
```

### 8.6 用户偏好表 (user_preference)
```sql
CREATE TABLE user_preference (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    content_type VARCHAR(20),
    preference_score INT DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES user(id)
);
```

### 8.7 操作日志表 (operation_log)
```sql
CREATE TABLE operation_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT,
    operation VARCHAR(50),
    target_type VARCHAR(50),
    target_id BIGINT,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(id)
);
```

---

## 9. UI 设计规范

### 9.1 色彩规范
- **主色调**: 橄榄绿 `#5A5D43`
- **背景色**: 燕麦色 `#F5F5F0`、米白色 `#FAFAF8`
- **卡片背景**: 纯白 `#FFFFFF`
- **文字主色**: 深灰 `#333333`
- **文字次要色**: 中灰 `#666666`
- **文字提示色**: 浅灰 `#999999`
- **边框色**: 浅米色 `#E8E8E3`
- **成功色**: 薄荷绿 `#C8E6D0`
- **警告色**: 暖黄 `#FFF4D6`
- **错误色**: 淡粉 `#FFE0E0`

### 9.2 字体规范
- **标题字体**: 衬线字体 (Noto Serif SC, Source Han Serif SC, 宋体)
- **正文字体**: 无衬线字体 (PingFang SC, Microsoft YaHei)
- **标题字号**: 20px / 18px / 16px
- **正文字号**: 14px
- **辅助文字**: 12px

### 9.3 组件规范
- **卡片圆角**: 16px
- **按钮圆角**: 20px (胶囊状)
- **输入框圆角**: 10px
- **标签圆角**: 12px
- **边框粗细**: 1px
- **阴影**: 柔和弥散阴影

### 9.4 间距规范
- **页面边距**: 24px
- **卡片内边距**: 20px
- **元素间距**: 12px / 16px / 24px

---

## 10. 修订记录

| 版本 | 日期 | 修订内容 | 作者 |
|------|------|----------|------|
| v1.0 | 2026-03-11 | 初始版本，基于开题报告整理 | - |
| v2.0 | 2026-03-12 | 移除物联网通信部分，专注于 SpringBoot CRUD 功能 | - |
| v3.0 | 2026-03-12 | 新增微信小程序客户端，补充 UI 设计规范 | - |
