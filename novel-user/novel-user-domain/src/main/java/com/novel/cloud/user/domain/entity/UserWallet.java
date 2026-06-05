package com.novel.cloud.user.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

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
