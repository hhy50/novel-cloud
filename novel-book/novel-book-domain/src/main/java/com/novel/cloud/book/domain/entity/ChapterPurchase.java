package com.novel.cloud.book.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Chapter purchase domain entity
 */
@Data
public class ChapterPurchase {

    private Long id;
    private Long userId;
    private Long bookId;
    private Long chapterId;
    private Integer costCoins;
    private LocalDateTime createTime;
}
