package com.novel.cloud.subscribe;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 订阅服务启动类
 */
@MapperScan("com.novel.cloud.subscribe.infrastructure.mapper")
@EnableFeignClients(basePackages = "com.novel.cloud")
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"com.novel.cloud.subscribe", "com.novel.cloud.config"})
public class NovelSubscribeApplication {

    public static void main(String[] args) {
        SpringApplication.run(NovelSubscribeApplication.class, args);
    }
}
