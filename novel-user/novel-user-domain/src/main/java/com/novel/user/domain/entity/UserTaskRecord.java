package com.novel.user.domain.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * User task completion record domain entity
 */
@Data
public class UserTaskRecord {

    private Long id;
    private Long userId;
    private Long taskId;
    private LocalDate taskDate;
    private Integer currentCount;
    private Integer targetCount;
    private Boolean completed;
    private Boolean rewardClaimed;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
