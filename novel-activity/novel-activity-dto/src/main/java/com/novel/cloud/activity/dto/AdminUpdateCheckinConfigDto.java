package com.novel.cloud.activity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Admin 创建 / 更新签到配置请求 DTO
 * <p>id 为空 → 新建；id 非空 → 更新</p>
 */
@Data
public class AdminUpdateCheckinConfigDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键，null 表示新建
     */
    private Long id;

    @NotBlank(message = "configName cannot be blank")
    private String configName;

    @NotBlank(message = "configCode cannot be blank")
    private String configCode;

    @NotNull(message = "mondayReward cannot be null")
    private Integer mondayReward;

    @NotNull(message = "tuesdayReward cannot be null")
    private Integer tuesdayReward;

    @NotNull(message = "wednesdayReward cannot be null")
    private Integer wednesdayReward;

    @NotNull(message = "thursdayReward cannot be null")
    private Integer thursdayReward;

    @NotNull(message = "fridayReward cannot be null")
    private Integer fridayReward;

    @NotNull(message = "saturdayReward cannot be null")
    private Integer saturdayReward;

    @NotNull(message = "sundayReward cannot be null")
    private Integer sundayReward;

    @NotNull(message = "dailyRewardDefault cannot be null")
    private Integer dailyRewardDefault;

    /**
     * 连续签到奖励 JSON
     * 格式：[{"days":3,"bonus":30,"name":"连续3天奖励"},...]
     */
    @NotBlank(message = "continuousBonusJson cannot be blank")
    private String continuousBonusJson;

    @NotNull(message = "startTime cannot be null")
    private LocalDateTime startTime;

    /**
     * 生效结束时间，null 表示永久生效
     */
    private LocalDateTime endTime;

    /**
     * 状态：0-禁用，1-启用
     */
    @NotNull(message = "status cannot be null")
    private Integer status;
}
