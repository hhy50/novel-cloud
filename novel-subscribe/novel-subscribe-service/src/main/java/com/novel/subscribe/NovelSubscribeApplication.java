package com.novel.subscribe;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 订阅服务启动类
 */
@MapperScan("com.novel.subscribe.infrastructure.mapper")
@EnableFeignClients(basePackages = "com.novel")
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"com.novel.subscribe", "com.novel.common.config"})
public class NovelSubscribeApplication {

    public static void main(String[] args) {
        SpringApplication.run(NovelSubscribeApplication.class, args);
    }
}
