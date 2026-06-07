package com.novel.cloud.activity.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 连续签到奖励配置项
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContinuousBonusItem {

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
}
