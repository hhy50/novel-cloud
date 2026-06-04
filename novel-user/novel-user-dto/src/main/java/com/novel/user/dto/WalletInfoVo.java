package com.novel.user.dto;

import java.io.Serial;
import java.io.Serializable;
import lombok.Data;

/**
 * Wallet info response DTO
 */
@Data
public class WalletInfoVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long coins;
    private Long points;
    private Long totalCoins;
    private Long totalPoints;
}
