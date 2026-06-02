package com.novel.book.dto;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class BookstoreBookVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long bookId;

    private String title;

    private String author;

    private String description;

    private String coverHexColor;

    private List<BookChapterVo> chapters;
}
