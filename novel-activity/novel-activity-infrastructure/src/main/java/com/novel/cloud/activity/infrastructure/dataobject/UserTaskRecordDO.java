package com.novel.cloud.activity.infrastructure.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("t_user_task_record")
public class UserTaskRecordDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Long taskId;

    private LocalDate taskDate;

    private Integer currentCount;

    private Integer targetCount;

    private Boolean completed;

    private Boolean rewardClaimed;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
