package com.novel.cloud.book.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * Book chapter list query request
 */
@Data
public class BookChapterListQueryReq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "bookId can not be null")
    private Long bookId;

    /**
     * 查询数量限制
     * 默认值: 10
     * -1 表示查询全部章节
     */
    private Integer length = 10;
}
