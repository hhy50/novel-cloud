package com.novel.book.dto;

import java.io.Serial;
import java.io.Serializable;

import lombok.Data;

@Data
public class BookDetailVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long bookId;

    private String bookName;

    private String authorName;

    private String categoryName;

    private String coverUrl;

    private String description;

    private Boolean finished;
}
