package com.novel.cloud.activity.domain.entity;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 用户周签到状态领域实体
 */
@Data
public class UserCheckinWeekStatus {

    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 年份第几周（格式：202623）
     */
    private Integer weekOfYear;

    /**
     * 7天签到状态位掩码（第0位=周一）
     * 每一位表示当天是否已签到：0-未签到，1-已签到
     */
    private Integer checkinDays;

    /**
     * 已签到天数
     */
    private Integer checkinCount;

    /**
     * 当前连续签到天数
     */
    private Integer continuousDays;

    /**
     * 已领取的连续奖励掩码
     * 每一位表示是否已领取对应配置的连续奖励
     */
    private Integer claimedBonusMask;

    /**
     * 周开始日期（周一）
     */
    private LocalDate weekStartDate;

    /**
     * 周结束日期（周日）
     */
    private LocalDate weekEndDate;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    /**
     * 检查某一天是否已签到
     * @param dayIndex 0=周一，6=周日
     */
    public boolean isDayChecked(int dayIndex) {
        return checkinDays != null && (checkinDays & (1 << dayIndex)) != 0;
    }

    /**
     * 标记某一天为已签到
     * @param dayIndex 0=周一，6=周日
     */
    public void markDayChecked(int dayIndex) {
        if (checkinDays == null) {
            checkinDays = 0;
        }
        checkinDays |= (1 << dayIndex);
        if (checkinCount == null) {
            checkinCount = 0;
        }
        checkinCount++;
    }

    /**
     * 检查某个连续奖励是否已领取
     * @param index 奖励配置索引
     */
    public boolean isBonusClaimed(int index) {
        return claimedBonusMask != null && (claimedBonusMask & (1 << index)) != 0;
    }

    /**
     * 标记某个连续奖励为已领取
     * @param index 奖励配置索引
     */
    public void markBonusClaimed(int index) {
        if (claimedBonusMask == null) {
            claimedBonusMask = 0;
        }
        claimedBonusMask |= (1 << index);
    }
}
