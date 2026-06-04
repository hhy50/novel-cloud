package com.novel.user.domain.entity;

import java.time.LocalDateTime;
import lombok.Data;

/**
 * User wallet domain entity
 */
@Data
public class UserWallet {

    private Long id;
    private Long userId;
    private Long coins;
    private Long points;
    private Long totalCoins;
    private Long totalPoints;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
