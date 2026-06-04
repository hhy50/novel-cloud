package com.novel.user.dto;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import lombok.Data;

/**
 * Coin records list response DTO
 */
@Data
public class CoinRecordsVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private List<CoinRecordVo> records;
    private Integer total;
}
