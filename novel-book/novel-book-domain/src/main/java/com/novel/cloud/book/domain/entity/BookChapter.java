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
    private Integer wordscount;
    private Integer isVip;
    private Integer number;
    private Integer status;
    private Integer price;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private LocalDateTime deleteTime;
}
