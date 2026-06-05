package com.novel.cloud.book.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Reading history domain entity
 */
@Data
public class ReadingHistory {

    private Long id;
    private Long userId;
    private Long bookId;
    private Long chapterId;
    private Integer progress;
    private Integer duration;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
