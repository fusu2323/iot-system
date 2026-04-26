# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 项目概述

基于 Spring Boot 的智能家居娱乐管理系统，解决多品牌设备协议各异、信息孤立问题，提供统一的设备管理、场景联动和内容推荐功能。

详细需求见 [PRD.md](./PRD.md)。

## 技术栈

- **后端**: Spring Boot 3.x + RESTful API
- **数据库**: MySQL 8.x
- **安全**: Spring Security + JWT
- **设备通信**: MQTT/HTTP/WebSocket
- **前端**: 待选型 (Vue/React)

## 命令

当前项目处于初始化阶段，暂无构建/测试命令。

待项目初始化后补充：
- `mvn spring-boot:run` - 启动后端服务
- `mvn test` - 运行测试
- `mvn package` - 打包构建

## 架构概要

```
前端 (Web/Mobile) ←→ Spring Boot 后端 ←→ MySQL
                          ↓
                    设备通信模块 (MQTT/HTTP)
                          ↓
                    智能家居设备
```

### 核心模块
1. **设备管理** - 设备接入、发现、状态监控、控制指令
2. **场景联动** - 预设/自定义场景、一键触发、定时/条件触发
3. **内容推荐** - 用户画像、个性化推荐
4. **用户权限** - 注册登录、权限分级、操作日志
5. **安全通信** - 数据加密、隐私保护

## 开发注意事项

- 采用前后端分离架构
- API 设计遵循 RESTful 规范
- 设备控制指令响应时间 < 500ms
- 支持 50+ 设备并发管理
- 系统需 7x24 小时稳定运行
