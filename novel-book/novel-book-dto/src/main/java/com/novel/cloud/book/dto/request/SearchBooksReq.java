package com.novel.cloud.book.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * Search books request
 */
@Data
public class SearchBooksReq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "Keyword cannot be blank")
    private String keyword;

    @Min(value = 1, message = "Page number must be at least 1")
    private Integer page = 1;

    @Min(value = 1, message = "Page size must be at least 1")
    private Integer pageSize = 20;

    private String category;
    private Integer finishedStatus;
}
