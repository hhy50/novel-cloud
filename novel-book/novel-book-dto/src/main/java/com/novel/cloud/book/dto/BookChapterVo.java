package com.novel.cloud.book.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class BookChapterVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long chapterId;

    private String chapterTitle;

    private List<String> paragraphs;
}
