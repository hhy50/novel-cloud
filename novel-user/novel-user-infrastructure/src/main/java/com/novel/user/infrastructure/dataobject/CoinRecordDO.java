package com.novel.user.infrastructure.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

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
