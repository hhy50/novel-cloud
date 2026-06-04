package com.novel.book.domain.entity;

import java.time.LocalDateTime;
import lombok.Data;

/**
 * User bookshelf domain entity
 */
@Data
public class UserBookshelf {

    private Long id;
    private Long userId;
    private Long bookId;
    private Long lastChapterId;
    private LocalDateTime lastReadTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
