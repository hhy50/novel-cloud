package com.novel.cloud.book.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 章节购买记录 - 领域实体
 */
@Data
public class ChapterPurchase {

    private Long id;
    private Long userId;
    private Long bookId;
    private Long chapterId;
    private Integer costCoins;
    private LocalDateTime createTime;

    /**
     * 获取花费金币数
     */
    public Integer getCostCoinsValue() {
        return costCoins != null ? costCoins : 0;
    }

    /**
     * 判断是否免费获得
     */
    public boolean isFree() {
        return costCoins != null && costCoins == 0;
    }

    /**
     * 判断是否是付费购买
     */
    public boolean isPaidPurchase() {
        return costCoins != null && costCoins > 0;
    }
}
