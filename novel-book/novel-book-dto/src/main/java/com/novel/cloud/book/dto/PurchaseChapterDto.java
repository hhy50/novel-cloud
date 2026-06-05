package com.novel.cloud.book.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * Purchase chapter request DTO
 */
@Data
public class PurchaseChapterDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "bookId cannot be null")
    private Long bookId;

    @NotNull(message = "chapterId cannot be null")
    private Long chapterId;
}
