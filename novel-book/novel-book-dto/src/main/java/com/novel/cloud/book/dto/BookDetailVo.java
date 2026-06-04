package com.novel.cloud.book.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

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
