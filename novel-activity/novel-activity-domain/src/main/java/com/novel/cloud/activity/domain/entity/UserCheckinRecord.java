package com.novel.cloud.activity.domain.entity;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 用户签到记录领域实体
 */
@Data
public class UserCheckinRecord {

    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 签到日期
     */
    private LocalDate checkinDate;

    /**
     * 签到时间
     */
    private LocalDateTime checkinTime;

    /**
     * 年份第几周（格式：202623）
     */
    private Integer weekOfYear;

    /**
     * 星期几（1-7，1=周一）
     */
    private Integer dayOfWeek;

    /**
     * 每日签到奖励金币数
     */
    private Integer dailyReward;

    /**
     * 本次签到获得的额外奖励总和
     */
    private Integer totalBonus;

    /**
     * 签到后用户总金币数
     */
    private Integer totalCoins;

    /**
     * 本次签到后连续签到天数
     */
    private Integer continuousDays;

    /**
     * 本次获得的额外奖励详情JSON
     * 格式：[{"days":3,"bonus":30,"name":"连续3天奖励"},...]
     */
    private String bonusDetailsJson;

    private LocalDateTime createTime;
}
