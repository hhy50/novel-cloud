package com.novel.cloud.book.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 获取章节前后范围请求
 */
@Data
public class ChapterRangeQueryReq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 书籍ID */
    @NotNull(message = "bookId cannot be null")
    private Long bookId;

    /** 当前章节ID */
    @NotNull(message = "chapterId cannot be null")
    private Long chapterId;

    /** 前后各取多少章（可选，默认20） */
    private Integer rangeSize;
}
