package com.novel.book.domain.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

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
