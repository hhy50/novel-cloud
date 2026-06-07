package com.novel.cloud.book.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 书架书籍项 VO
 */
@Data
public class BookshelfItemVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 书籍ID */
    private Long bookId;

    /** 书籍名称 */
    private String bookName;

    /** 作者名称 */
    private String authorName;

    /** 封面URL */
    private String coverUrl;

    /** 分类名称 */
    private String categoryName;

    /** 书籍简介 */
    private String description;

    /** 是否完结 (status == 2) */
    private Boolean finished;

    /** 状态 1连载 2完结 3下架 */
    private Integer status;

    /** 总字数 */
    private Integer totalWords;

    /** 总章节数 */
    private Integer totalChapters;

    /** 评分 (0-10) */
    private Integer score;

    /** 最后阅读章节ID */
    private Long lastChapterId;

    /** 最后阅读章节标题 */
    private String lastChapterTitle;

    /** 阅读进度百分比（0-100） */
    private Integer readProgress;

    /** 最后阅读时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastReadTime;

    /** 是否有更新 */
    private Boolean hasUpdate;

    /** 新章节数 */
    private Integer newChapterCount;

    /** 最新章节标题 */
    private String latestChapterTitle;

    /** 最新章节ID */
    private Integer latestChapterId;

    /** 书籍更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
