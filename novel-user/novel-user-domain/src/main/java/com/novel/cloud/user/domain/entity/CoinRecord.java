package com.novel.cloud.user.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Coin transaction record domain entity
 */
@Data
public class CoinRecord {

    private Long id;
    private Long userId;
    private Long amount;
    private Long balance;
    private String type;
    private String description;
    private LocalDateTime createTime;
}
