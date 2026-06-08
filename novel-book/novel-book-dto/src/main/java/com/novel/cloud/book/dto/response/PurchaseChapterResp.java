package com.novel.cloud.book.dto.response;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * Purchase chapter response
 */
@Data
public class PurchaseChapterResp implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Boolean success;
    private Integer costCoins;
    private Long remainingCoins;
    private String message;
}
