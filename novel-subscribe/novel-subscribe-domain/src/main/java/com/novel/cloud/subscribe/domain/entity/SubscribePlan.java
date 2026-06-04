package com.novel.cloud.subscribe.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 订阅计划 - 领域实体
 */
@Data
public class SubscribePlan {

    private Long id;
    private String planName;
    private String planCode;
    private Integer durationDays;
    private Long price;
    private Long originalPrice;
    private Integer sortOrder;
    private Integer status;
    private LocalDateTime createTime;
}
