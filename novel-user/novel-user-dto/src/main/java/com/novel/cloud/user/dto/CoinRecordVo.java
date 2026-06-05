package com.novel.cloud.user.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Coin record DTO
 */
@Data
public class CoinRecordVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long amount;
    private Long balance;
    private String type;
    private String description;
    private LocalDateTime createTime;
}
