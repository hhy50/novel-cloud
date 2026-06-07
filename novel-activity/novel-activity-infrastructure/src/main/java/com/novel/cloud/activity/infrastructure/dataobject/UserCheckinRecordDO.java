package com.novel.cloud.activity.infrastructure.dataobject;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 用户签到记录DO
 */
@Data
@TableName("activity_user_checkin_record")
public class UserCheckinRecordDO {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long userId;

    private LocalDate checkinDate;

    private LocalDateTime checkinTime;

    private Integer weekOfYear;

    private Integer dayOfWeek;

    private Integer dailyReward;

    private Integer totalBonus;

    private Integer totalCoins;

    private Integer continuousDays;

    private String bonusDetailsJson;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
