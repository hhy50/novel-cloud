package com.novel.cloud.book.domain.entity;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 阅读统计 - 领域实体
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

    /**
     * 增加阅读书籍数
     */
    public void incrementBooksRead() {
        if (booksRead == null) {
            booksRead = 0;
        }
        booksRead++;
    }

    /**
     * 增加阅读章节数
     */
    public void incrementChaptersRead() {
        if (chaptersRead == null) {
            chaptersRead = 0;
        }
        chaptersRead++;
    }

    /**
     * 增加阅读时长（分钟）
     */
    public void addMinutesRead(Integer minutes) {
        if (minutesRead == null) {
            minutesRead = 0;
        }
        if (minutes != null) {
            minutesRead += minutes;
        }
    }

    /**
     * 获取阅读书籍数（默认0）
     */
    public Integer getBooksReadOrDefault() {
        return booksRead != null ? booksRead : 0;
    }

    /**
     * 获取阅读章节数（默认0）
     */
    public Integer getChaptersReadOrDefault() {
        return chaptersRead != null ? chaptersRead : 0;
    }

    /**
     * 获取阅读时长（默认0）
     */
    public Integer getMinutesReadOrDefault() {
        return minutesRead != null ? minutesRead : 0;
    }
}
