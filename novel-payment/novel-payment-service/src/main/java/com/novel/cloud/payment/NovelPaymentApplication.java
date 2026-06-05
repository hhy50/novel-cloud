package com.novel.cloud.payment;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 支付服务启动类
 */
@MapperScan("com.novel.cloud.payment.infrastructure.mapper")
@EnableFeignClients(basePackages = "com.novel.cloud")
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"com.novel.cloud.payment", "com.novel.cloud.config"})
public class NovelPaymentApplication {

    public static void main(String[] args) {
        SpringApplication.run(NovelPaymentApplication.class, args);
    }
}
