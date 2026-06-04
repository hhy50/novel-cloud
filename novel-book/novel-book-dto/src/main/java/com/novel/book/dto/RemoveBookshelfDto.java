package com.novel.book.dto;

import java.io.Serial;
import java.io.Serializable;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Remove from bookshelf request DTO
 */
@Data
public class RemoveBookshelfDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "bookId cannot be null")
    private Long bookId;
}
