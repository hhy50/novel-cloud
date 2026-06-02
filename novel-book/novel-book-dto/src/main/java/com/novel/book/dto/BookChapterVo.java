package com.novel.book.dto;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class BookChapterVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long chapterId;

    private String chapterTitle;

    private List<String> paragraphs;
}
