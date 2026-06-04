package com.novel.user.domain.entity;

import java.time.LocalDateTime;
import lombok.Data;

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
