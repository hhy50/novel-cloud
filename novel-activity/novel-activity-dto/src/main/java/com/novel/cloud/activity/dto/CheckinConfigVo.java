package com.novel.cloud.activity.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 签到配置响应 VO（admin 端使用）
 */
@Data
public class CheckinConfigVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String configName;

    private String configCode;

    private Integer mondayReward;
    private Integer tuesdayReward;
    private Integer wednesdayReward;
    private Integer thursdayReward;
    private Integer fridayReward;
    private Integer saturdayReward;
    private Integer sundayReward;

    private Integer dailyRewardDefault;

    /**
     * 连续签到奖励配置 JSON
     */
    private String continuousBonusJson;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    /**
     * 状态：0-禁用，1-启用
     */
    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
