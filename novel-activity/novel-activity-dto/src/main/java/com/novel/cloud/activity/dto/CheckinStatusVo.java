package com.novel.cloud.activity.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * 签到状态响应VO
 */
@Data
public class CheckinStatusVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 今天是否已签到
     */
    private Boolean checkedToday;

    /**
     * 当前年份第几周（格式：202623）
     */
    private Integer weekOfYear;

    /**
     * 本周开始日期（周一）
     */
    private LocalDate weekStartDate;

    /**
     * 本周结束日期（周日）
     */
    private LocalDate weekEndDate;

    /**
     * 当前连续签到天数
     */
    private Integer continuousDays;

    /**
     * 本周已签到天数
     */
    private Integer totalCheckinDays;

    /**
     * 明天签到奖励金币数
     */
    private Integer nextReward;

    /**
     * 本周7天签到状态详情
     */
    private List<DayCheckinStatusVo> weekCheckinStatus;

    /**
     * 连续签到奖励配置
     */
    private List<ContinuousBonusConfigVo> continuousBonusConfig;
}
