package com.novel.cloud.book.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class BookChapterListQueryDto implements Serializable {

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
