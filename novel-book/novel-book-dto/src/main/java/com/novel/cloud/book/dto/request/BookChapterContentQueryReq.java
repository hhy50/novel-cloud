package com.novel.cloud.book.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * Book chapter content query request
 */
@Data
public class BookChapterContentQueryReq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "bookId can not be null")
    private Long bookId;

    @NotNull(message = "chapterId can not be null")
    private Long chapterId;
}
