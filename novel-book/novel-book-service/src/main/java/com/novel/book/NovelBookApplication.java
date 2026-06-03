package com.novel.book;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@MapperScan("com.novel.book.infrastructure.mapper")
@EnableFeignClients(basePackages = "com.novel")
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"com.novel.book", "com.novel.common.config"})
public class NovelBookApplication {

    public static void main(String[] args) {
        SpringApplication.run(NovelBookApplication.class, args);
    }
}
