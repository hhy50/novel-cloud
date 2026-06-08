package com.novel.cloud.book.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * Add to bookshelf request
 */
@Data
public class AddBookshelfReq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "bookId cannot be null")
    private Long bookId;
}
