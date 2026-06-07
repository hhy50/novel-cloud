package com.novel.cloud.activity.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 连续签到奖励配置VO
 */
@Data
public class ContinuousBonusConfigVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 需要连续签到天数
     */
    private Integer days;

    /**
     * 奖励金币数
     */
    private Integer bonus;

    /**
     * 奖励名称
     */
    private String name;

    /**
     * 是否已达成
     */
    private Boolean achieved;

    /**
     * 是否已领取
     */
    private Boolean claimed;
}
