package com.novel.book.dto;

import java.io.Serial;
import java.io.Serializable;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Record reading progress request DTO
 */
@Data
public class RecordReadingDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "bookId cannot be null")
    private Long bookId;

    @NotNull(message = "chapterId cannot be null")
    private Long chapterId;

    private Integer progress;
    private Integer duration;
}
