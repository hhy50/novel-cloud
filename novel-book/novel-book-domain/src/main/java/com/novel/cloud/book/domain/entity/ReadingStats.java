package com.novel.cloud.book.domain.entity;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Reading statistics domain entity
 */
@Data
public class ReadingStats {

    private Long id;
    private Long userId;
    private LocalDate statDate;
    private Integer booksRead;
    private Integer chaptersRead;
    private Integer minutesRead;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
