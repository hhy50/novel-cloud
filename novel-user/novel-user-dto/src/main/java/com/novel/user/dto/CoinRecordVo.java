package com.novel.user.dto;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

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
