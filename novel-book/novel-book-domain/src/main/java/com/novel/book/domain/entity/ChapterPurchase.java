package com.novel.book.domain.entity;

import java.time.LocalDateTime;
import lombok.Data;

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
