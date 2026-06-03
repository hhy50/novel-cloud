package com.novel.payment;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 支付服务启动类
 */
@MapperScan("com.novel.payment.infrastructure.mapper")
@EnableFeignClients(basePackages = "com.novel")
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"com.novel.payment", "com.novel.common.config"})
public class NovelPaymentApplication {

    public static void main(String[] args) {
        SpringApplication.run(NovelPaymentApplication.class, args);
    }
}
