package com.novel.cloud.book.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

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
