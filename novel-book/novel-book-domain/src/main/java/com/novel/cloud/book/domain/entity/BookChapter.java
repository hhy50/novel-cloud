package com.novel.cloud.book.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 书籍章节 - 领域实体（对齐 n_book_chapter DDL）
 */
@Data
public class BookChapter {

    private Long id;
    private Long bookId;
    private String title;
    private Integer wordsCount;
    private Integer isVip;
    private Integer number;
    private Integer status;
    private Integer price;

    private LocalDateTime updateTime;
    private Integer unlockStatus;

    /**
     * 判断是否是VIP章节
     */
    public boolean isVipChapter() {
        return isVip != null && isVip == 1;
    }

    /**
     * 判断是否是免费章节
     */
    public boolean isFree() {
        if (price != null && price == 0) {
            return true;
        }
        return !isVipChapter();
    }

    /**
     * 判断是否需要购买
     */
    public boolean needsPurchase() {
        if (price != null && price > 0) {
            return true;
        }
        return isVipChapter();
    }

    /**
     * 判断章节是否已发布
     */
    public boolean isPublished() {
        return status != null && status == 1;
    }

    /**
     * 获取章节价格（返回原始值）
     */
    public Integer getPriceValue() {
        if (price != null) {
            return price;
        }
        return isVipChapter() ? 10 : 0;
    }

    /**
     * 获取字数（返回原始值）
     */
    public Integer getWordCountValue() {
        if (wordsCount != null) {
            return wordsCount;
        }
        return 0;
    }

    /**
     * 判断是否是第一章
     */
    public boolean isFirstChapter() {
        return number != null && number == 1;
    }

    /**
     * 判断章节序号是否在范围内
     */
    public boolean isNumberInRange(int start, int end) {
        if (number == null) {
            return false;
        }
        return number >= start && number <= end;
    }
}
