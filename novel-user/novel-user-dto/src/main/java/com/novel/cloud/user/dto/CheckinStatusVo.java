package com.novel.cloud.user.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * Check-in status response DTO
 */
@Data
public class CheckinStatusVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Boolean checkedToday;
    private Integer consecutiveDays;
    private Integer nextRewardCoins;
    private Integer nextRewardPoints;
}
