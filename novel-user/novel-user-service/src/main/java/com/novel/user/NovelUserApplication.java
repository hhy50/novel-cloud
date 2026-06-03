package com.novel.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@MapperScan("com.novel.user.infrastructure.mapper")
@EnableFeignClients(basePackages = "com.novel")
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"com.novel.user", "com.novel.common.config"})
public class NovelUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(NovelUserApplication.class, args);
    }
}
