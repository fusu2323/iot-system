# 后端开发文档

## 一、项目骨架说明

### 1.1 技术栈
- **框架**: Spring Boot 3.2.0
- **JDK**: 17
- **数据库**: MySQL 8.x
- **ORM**: MyBatis-Plus 3.5.5
- **安全**: Spring Security + JWT (jjwt 0.12.3)
- **API 文档**: Knife4j 4.4.0 (Swagger 3)

### 1.2 项目结构
```
backend/
├── pom.xml                          # Maven 配置
└── src/main/
    ├── java/com/example/iot/
    │   ├── IotSystemApplication.java          # 主启动类
    │   ├── common/
    │   │   ├── result/
    │   │   │   ├── Result.java                # 统一响应结果
    │   │   │   └── ResultCode.java            # 统一错误码枚举
    │   │   └── exception/
    │   │       ├── BusinessException.java     # 业务异常类
    │   │       └── GlobalExceptionHandler.java # 全局异常处理器
    │   ├── config/
    │   │   ├── Knife4jConfig.java             # API 文档配置
    │   │   └── SecurityConfig.java            # Spring Security 配置
    │   ├── security/
    │   │   ├── JwtAuthenticationFilter.java   # JWT 认证过滤器
    │   │   └── JwtUserDetailsService.java     # 用户详情服务
    │   ├── util/
    │   │   └── JwtUtil.java                   # JWT 工具类
    │   ├── entity/                            # 实体类（待创建）
    │   ├── dto/                               # 数据传输对象（待创建）
    │   ├── vo/                                # 视图对象（待创建）
    │   ├── mapper/                            # 数据访问层（待创建）
    │   ├── service/                           # 业务逻辑层（待创建）
    │   │   └── impl/                          # 服务实现（待创建）
    │   └── controller/                        # 控制器层（待创建）
    └── resources/
        ├── application.yml                    # 主配置文件
        ├── application-dev.yml                # 开发环境配置
        ├── application-test.yml               # 测试环境配置
        ├── application-prod.yml               # 生产环境配置
        └── db/
            └── init.sql                       # 数据库初始化脚本
```

---

## 二、数据库设计

### 2.1 数据库信息
- **数据库名**: `iot_system`
- **字符集**: `utf8mb4`
- **排序规则**: `utf8mb4_unicode_ci`

### 2.2 数据表清单

| 表名 | 说明 | 记录数 |
|------|------|--------|
| `user` | 用户表 | 2 |
| `device` | 设备表 | 0 |
| `scene` | 场景表 | 5 |
| `scene_device` | 场景设备关联表 | 0 |
| `content` | 内容表 | 8 |
| `user_preference` | 用户偏好表 | 0 |
| `operation_log` | 操作日志表 | 0 |

### 2.3 初始化数据
- **管理员账号**: admin / admin123
- **普通用户**: user / admin123
- **预设场景**: 回家模式、离家模式、影院模式、睡眠模式、阅读模式
- **示例内容**: 8 条影视/音乐/游戏内容

---

## 三、API 接口

### 3.1 访问 API 文档
启动项目后访问：http://localhost:8080/doc.html

### 3.2 接口规范

#### 统一响应格式
```json
{
  "code": 200,
  "message": "success",
  "data": {},
  "timestamp": 1710234567890
}
```

#### 错误码规范
| 错误码范围 | 说明 |
|-----------|------|
| 200-999 | 通用错误码 |
| 1000-1999 | 认证相关错误 |
| 2000-2999 | 设备相关错误 |
| 3000-3999 | 场景相关错误 |
| 4000-4999 | 内容相关错误 |
| 5000-5999 | 推荐相关错误 |
| 6000-6999 | 日志相关错误 |

### 3.3 待开发接口
详见 PRD.md 第 6 章 API 接口设计

---

## 四、运行项目

### 4.1 前置要求
- JDK 17+
- Maven 3.6+
- MySQL 8.0+

### 4.2 启动步骤

1. **初始化数据库**
```bash
mysql -u root iot_system < src/main/resources/db/init.sql
```

2. **启动项目**
```bash
cd backend
mvn spring-boot:run
```

3. **访问 API 文档**
```
http://localhost:8080/doc.html
```

### 4.3 配置文件说明

| 配置文件 | 说明 |
|---------|------|
| application.yml | 主配置，包含数据库连接、JWT 配置等 |
| application-dev.yml | 开发环境，开启 SQL 日志 |
| application-test.yml | 测试环境，使用 test 数据库 |
| application-prod.yml | 生产环境，关闭日志，使用环境变量 |

---

## 五、开发注意事项

### 5.1 密码加密
所有用户密码使用 BCrypt 加密存储

### 5.2 JWT Token
- Token 有效期：24 小时（86400000ms）
- Token 格式：`Bearer {token}`
- 从请求头 `Authorization` 中传递

### 5.3 免认证接口
以下接口无需 Token 即可访问：
- `/api/auth/**` - 登录、注册接口
- `/v3/api-docs/**` - API 文档
- `/swagger-ui/**` - Swagger UI
- `/doc.html` - Knife4j 文档页

### 5.4 逻辑删除
所有表都有 `deleted` 字段，使用逻辑删除而非物理删除

---

## 六、第一阶段完成标志

- [x] 项目骨架创建完成
- [x] Maven 依赖配置完成
- [x] 多环境配置完成
- [x] 统一响应格式配置完成
- [x] 全局异常处理完成
- [x] API 文档配置完成
- [x] 数据库表创建完成
- [x] 初始化数据插入完成
- [x] Spring Security 配置完成
- [x] JWT 工具类实现完成
- [x] JWT 认证过滤器实现完成

---

## 修订记录

| 日期 | 版本 | 说明 |
|------|------|------|
| 2026-03-12 | v1.0 | 初始版本，完成 Phase 1 任务 |
