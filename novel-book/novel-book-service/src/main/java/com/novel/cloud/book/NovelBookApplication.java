package com.novel.cloud.book;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@MapperScan("com.novel.cloud.book.infrastructure.mapper")
@EnableFeignClients(basePackages = "com.novel.cloud")
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"com.novel.cloud.book", "com.novel.cloud.config"})
public class NovelBookApplication {

    public static void main(String[] args) {
        SpringApplication.run(NovelBookApplication.class, args);
    }
}
