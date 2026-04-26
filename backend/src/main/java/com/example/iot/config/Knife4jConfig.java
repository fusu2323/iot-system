package com.example.iot.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * Knife4j API 文档配置
 */
@Configuration
public class Knife4jConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("智能家居娱乐管理系统 API 文档")
                .version("1.0.0")
                .description("基于 Spring Boot 3.x 的智能家居娱乐管理系统 RESTful API")
                .contact(new Contact()
                    .name("IoT System")
                    .email("admin@example.com"))
                .license(new License()
                    .name("Apache 2.0")
                    .url("https://www.apache.org/licenses/LICENSE-2.0")))
            .addSecurityItem(new SecurityRequirement().addList("Authorization"))
            .schemaRequirement("Authorization", createSecurityScheme());
    }

    /**
     * 创建 JWT 安全方案
     */
    private SecurityScheme createSecurityScheme() {
        return new SecurityScheme()
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT")
            .name("Authorization")
            .in(SecurityScheme.In.HEADER)
            .description("JWT Token，格式：Bearer {token}");
    }

    /**
     * 全局 API 自定义器 - 为所有响应添加统一响应结构
     */
    @Bean
    public GlobalOpenApiCustomizer globalOpenApiCustomizer() {
        return openApi -> {
            // 添加统一的标签
            List<Tag> tags = new ArrayList<>();
            tags.add(new Tag().name("用户认证").description("用户注册、登录相关接口"));
            tags.add(new Tag().name("用户管理").description("用户信息 CRUD 接口"));
            tags.add(new Tag().name("设备管理").description("设备信息 CRUD 接口"));
            tags.add(new Tag().name("场景管理").description("场景配置 CRUD 接口"));
            tags.add(new Tag().name("内容管理").description("内容信息 CRUD 接口"));
            tags.add(new Tag().name("推荐管理").description("推荐和偏好设置接口"));
            tags.add(new Tag().name("日志管理").description("操作日志查询接口"));
            tags.add(new Tag().name("数据统计").description(" dashboard 统计接口"));
            openApi.tags(tags);
        };
    }
}
