package com.novel.cloud.user.infrastructure.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_coin_record")
public class CoinRecordDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Long amount;

    private Long balance;

    private String type;

    private String description;

    private LocalDateTime createTime;
}
