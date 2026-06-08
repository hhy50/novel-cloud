package com.novel.cloud.book.dto.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 阅读历史项（共用）
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
