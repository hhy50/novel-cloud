package com.novel.book.dto;

import java.io.Serial;
import java.io.Serializable;
import lombok.Data;

/**
 * Purchase chapter response DTO
 */
@Data
public class PurchaseChapterVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Boolean success;
    private Integer costCoins;
    private Long remainingCoins;
    private String message;
}
