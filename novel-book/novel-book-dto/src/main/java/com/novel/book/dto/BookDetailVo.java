package com.novel.book.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 书籍详情 VO
 */
@Data
public class BookDetailVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 书籍ID
     */
    private Long bookId;

    /**
     * 书籍名称
     */
    private String bookName;

    /**
     * 作者名称
     */
    private String authorName;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 封面URL
     */
    private String coverUrl;

    /**
     * 书籍简介
     */
    private String description;

    /**
     * 是否完结
     */
    private Boolean finished;

    /**
     * 总章节数
     */
    private Long totalChapters;

    /**
     * 总字数
     */
    private Long wordCount;

    /**
     * 最后更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 热度值
     */
    private Integer hotScore;

    /**
     * 是否已加入书架
     */
    private Boolean inBookshelf;
}
