package com.novel.user.dto;

import java.io.Serial;
import java.io.Serializable;
import lombok.Data;

/**
 * Check-in response DTO
 */
@Data
public class CheckinVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Boolean success;
    private Integer consecutiveDays;
    private Integer rewardCoins;
    private Integer rewardPoints;
    private String message;
}
