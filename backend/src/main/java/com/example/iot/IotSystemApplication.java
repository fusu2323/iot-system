package com.example.iot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 智能家居娱乐管理系统 - 主启动类
 */
@SpringBootApplication
@MapperScan("com.example.iot.mapper")
public class IotSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(IotSystemApplication.class, args);
    }
}
