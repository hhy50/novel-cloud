package com.novel.cloud.subscribe.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户订阅记录 - 聚合根
 */
@Data
public class UserSubscribe {

    public static final int STATUS_PENDING = 0;
    public static final int STATUS_ACTIVE = 1;
    public static final int STATUS_EXPIRED = 2;
    public static final int STATUS_CANCELLED = 3;

    private Long id;
    private Long userId;
    private Long planId;
    private String orderNo;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer status;
    private Boolean autoRenew;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public boolean isPending() {
        return this.status != null && this.status == STATUS_PENDING;
    }

    public boolean isActive() {
        return this.status != null && this.status == STATUS_ACTIVE;
    }

    public boolean isExpiredByTime() {
        return this.endTime != null && this.endTime.isBefore(LocalDateTime.now());
    }
}
