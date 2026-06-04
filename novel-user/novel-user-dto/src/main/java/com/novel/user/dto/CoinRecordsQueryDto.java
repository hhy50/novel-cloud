package com.novel.user.dto;

import java.io.Serial;
import java.io.Serializable;
import jakarta.validation.constraints.Min;
import lombok.Data;

/**
 * Coin records query DTO
 */
@Data
public class CoinRecordsQueryDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Min(value = 1, message = "Page number must be at least 1")
    private Integer page = 1;

    @Min(value = 1, message = "Page size must be at least 1")
    private Integer pageSize = 20;

    private String type;
}
