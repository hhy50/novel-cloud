package com.novel.cloud.activity.infrastructure.dataobject;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 用户周签到状态DO
 */
@Data
@TableName("activity_user_checkin_week_status")
public class UserCheckinWeekStatusDO {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long userId;

    private Integer weekOfYear;

    private Integer checkinDays;

    private Integer checkinCount;

    private Integer continuousDays;

    private Integer claimedBonusMask;

    private LocalDate weekStartDate;

    private LocalDate weekEndDate;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
