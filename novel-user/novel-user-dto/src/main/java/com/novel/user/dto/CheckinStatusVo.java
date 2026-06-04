package com.novel.user.dto;

import java.io.Serial;
import java.io.Serializable;
import lombok.Data;

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
