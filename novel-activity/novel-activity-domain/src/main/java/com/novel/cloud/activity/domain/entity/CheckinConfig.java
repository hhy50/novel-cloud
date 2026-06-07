package com.novel.cloud.activity.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 签到配置领域实体
 */
@Data
public class CheckinConfig {

    private Long id;

    /**
     * 配置名称
     */
    private String configName;

    /**
     * 配置编码
     */
    private String configCode;

    /**
     * 周一奖励金币数
     */
    private Integer mondayReward;

    /**
     * 周二奖励金币数
     */
    private Integer tuesdayReward;

    /**
     * 周三奖励金币数
     */
    private Integer wednesdayReward;

    /**
     * 周四奖励金币数
     */
    private Integer thursdayReward;

    /**
     * 周五奖励金币数
     */
    private Integer fridayReward;

    /**
     * 周六奖励金币数
     */
    private Integer saturdayReward;

    /**
     * 周日奖励金币数
     */
    private Integer sundayReward;

    /**
     * 默认每日奖励（当某天未单独配置时使用）
     */
    private Integer dailyRewardDefault;

    /**
     * 连续签到奖励配置JSON
     * 格式：[{"days":3,"bonus":30,"name":"连续3天奖励"},...]
     */
    private String continuousBonusJson;

    /**
     * 生效开始时间
     */
    private LocalDateTime startTime;

    /**
     * 生效结束时间（null表示永久）
     */
    private LocalDateTime endTime;

    /**
     * 状态：0-禁用，1-启用
     */
    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    /**
     * 根据星期几获取奖励
     * @param dayOfWeek 1=周一，7=周日
     */
    public int getRewardByDayOfWeek(int dayOfWeek) {
        return switch (dayOfWeek) {
            case 1 -> mondayReward != null ? mondayReward : dailyRewardDefault;
            case 2 -> tuesdayReward != null ? tuesdayReward : dailyRewardDefault;
            case 3 -> wednesdayReward != null ? wednesdayReward : dailyRewardDefault;
            case 4 -> thursdayReward != null ? thursdayReward : dailyRewardDefault;
            case 5 -> fridayReward != null ? fridayReward : dailyRewardDefault;
            case 6 -> saturdayReward != null ? saturdayReward : dailyRewardDefault;
            case 7 -> sundayReward != null ? sundayReward : dailyRewardDefault;
            default -> dailyRewardDefault;
        };
    }
}
