package com.novel.cloud.book.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * Remove from bookshelf request
 */
@Data
public class RemoveBookshelfReq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "bookId cannot be null")
    private Long bookId;
}
