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
}
