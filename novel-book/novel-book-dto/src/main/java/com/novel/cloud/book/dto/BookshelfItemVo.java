package com.novel.cloud.book.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Bookshelf item DTO
 */
@Data
public class BookshelfItemVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long bookId;
    private String bookName;
    private String authorName;
    private String coverUrl;
    private Long lastChapterId;
    private String lastChapterTitle;
    private LocalDateTime lastReadTime;
}
