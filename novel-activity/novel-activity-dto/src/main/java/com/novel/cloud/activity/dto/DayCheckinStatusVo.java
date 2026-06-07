package com.novel.cloud.activity.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * 某一天的签到状态VO
 */
@Data
public class DayCheckinStatusVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 日期
     */
    private LocalDate date;

    /**
     * 星期几（1-7，1=周一）
     */
    private Integer dayOfWeek;

    /**
     * 状态：locked-未到日期，available-可签到，done-已签到，missed-已错过
     */
    private String status;

    /**
     * 签到奖励金币数
     */
    private Integer reward;

    /**
     * 是否为今天
     */
    private Boolean isToday;
}
