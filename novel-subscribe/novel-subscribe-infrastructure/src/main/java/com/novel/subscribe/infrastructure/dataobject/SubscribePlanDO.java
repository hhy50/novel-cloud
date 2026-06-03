package com.novel.subscribe.infrastructure.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("subscribe_plan")
public class SubscribePlanDO {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String planName;
    private String planCode;
    private Integer durationDays;
    private Long price;
    private Long originalPrice;
    private Integer sortOrder;
    private Integer status;
    private LocalDateTime createTime;
}
