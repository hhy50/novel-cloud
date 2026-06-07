package com.novel.cloud.activity.infrastructure.dataobject;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 签到配置DO
 */
@Data
@TableName("activity_checkin_config")
public class CheckinConfigDO {

    @TableId(type = IdType.ASSIGN_ID)
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

    private String continuousBonusJson;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
