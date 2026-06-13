package com.novel.cloud.activity.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

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
