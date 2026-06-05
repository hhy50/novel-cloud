package com.novel.cloud.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@MapperScan("com.novel.cloud.user.infrastructure.mapper")
@EnableFeignClients(basePackages = "com.novel.cloud")
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"com.novel.cloud.user", "com.novel.cloud.config"})
public class NovelUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(NovelUserApplication.class, args);
    }
}
