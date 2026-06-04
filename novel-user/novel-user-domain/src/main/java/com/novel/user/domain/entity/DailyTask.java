package com.novel.user.domain.entity;

import java.time.LocalDateTime;
import lombok.Data;

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
