package com.novel.cloud.book.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * Record reading progress request
 */
@Data
public class RecordReadingReq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "bookId cannot be null")
    private Long bookId;

    @NotNull(message = "chapterId cannot be null")
    private Long chapterId;

    private Integer progress;
    private Integer duration;
}
