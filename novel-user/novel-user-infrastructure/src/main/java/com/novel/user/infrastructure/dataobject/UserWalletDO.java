package com.novel.user.infrastructure.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@TableName("t_user_wallet")
public class UserWalletDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Long coins;

    private Long points;

    private Long totalCoins;

    private Long totalPoints;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
