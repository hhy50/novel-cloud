package com.novel.book.dto;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * Reading history item DTO
 */
@Data
public class ReadingHistoryItemVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long bookId;
    private String bookName;
    private String authorName;
    private String coverUrl;
    private Long chapterId;
    private String chapterTitle;
    private Integer progress;
    private Integer duration;
    private LocalDateTime createTime;
}
