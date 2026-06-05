package com.novel.cloud.user.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Daily task domain entity
 */
@Data
public class DailyTask {

    private Long id;
    private String taskCode;
    private String taskName;
    private String taskDesc;
    private Integer rewardCoins;
    private Integer rewardPoints;
    private Integer targetCount;
    private Integer sortOrder;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
