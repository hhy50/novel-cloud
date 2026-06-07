package com.novel.cloud.activity.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 连续签到奖励VO
 */
@Data
public class BonusRewardVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 需要连续天数
     */
    private Integer days;

    /**
     * 奖励金额
     */
    private Integer bonus;

    /**
     * 奖励名称
     */
    private String name;
}
