package com.novel.book.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 书籍信息 - 领域实体
 */
@Data
public class BookInfo {

    private Long id;
    private String bookName;
    private String authorName;
    private String categoryName;
    private String coverUrl;
    private String description;
    private Integer finishedStatus;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
