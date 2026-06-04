package com.novel.user.domain.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * Daily check-in domain entity
 */
@Data
public class DailyCheckin {

    private Long id;
    private Long userId;
    private LocalDate checkinDate;
    private Integer consecutiveDays;
    private Integer rewardCoins;
    private Integer rewardPoints;
    private LocalDateTime createTime;
}
