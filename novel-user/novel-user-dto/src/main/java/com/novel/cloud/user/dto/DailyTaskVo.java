package com.novel.cloud.user.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * Daily task item DTO
 */
@Data
public class DailyTaskVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long taskId;
    private String taskCode;
    private String taskName;
    private String taskDesc;
    private Integer rewardCoins;
    private Integer rewardPoints;
    private Integer currentCount;
    private Integer targetCount;
    private Boolean completed;
    private Boolean rewardClaimed;
}
