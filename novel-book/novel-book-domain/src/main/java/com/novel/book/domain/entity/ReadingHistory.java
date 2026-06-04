package com.novel.book.domain.entity;

import java.time.LocalDateTime;
import lombok.Data;

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
