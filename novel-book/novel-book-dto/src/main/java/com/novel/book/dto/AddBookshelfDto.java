package com.novel.book.dto;

import java.io.Serial;
import java.io.Serializable;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Add to bookshelf request DTO
 */
@Data
public class AddBookshelfDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "bookId cannot be null")
    private Long bookId;
}
