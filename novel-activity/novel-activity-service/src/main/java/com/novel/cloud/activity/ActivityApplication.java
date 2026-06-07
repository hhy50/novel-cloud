package com.novel.cloud.activity;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 活动服务启动类
 */
@SpringBootApplication(scanBasePackages = "com.novel.cloud.activity")
@EnableDiscoveryClient
@MapperScan("com.novel.cloud.activity.infrastructure.mapper")
public class ActivityApplication {

    public static void main(String[] args) {
        SpringApplication.run(ActivityApplication.class, args);
    }
}
