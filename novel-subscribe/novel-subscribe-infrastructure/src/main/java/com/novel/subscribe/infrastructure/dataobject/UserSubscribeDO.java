package com.novel.subscribe.infrastructure.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("user_subscribe")
public class UserSubscribeDO {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long planId;
    private String orderNo;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer status;
    private Boolean autoRenew;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
