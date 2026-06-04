package com.novel.user.dto;

import java.io.Serial;
import java.io.Serializable;
import lombok.Data;

/**
 * Claim task reward response DTO
 */
@Data
public class ClaimTaskRewardVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Boolean success;
    private Integer rewardCoins;
    private Integer rewardPoints;
    private String message;
}
