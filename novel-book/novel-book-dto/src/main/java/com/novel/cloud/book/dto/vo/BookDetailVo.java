package com.novel.cloud.book.dto.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 书籍详情（共用）
 */
@Data
public class BookDetailVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 书籍ID */
    private Long bookId;

    /** 书籍名称 */
    private String title;

    /** 作者名称 */
    private String author;

    /** 书籍简介 */
    private String description;

    /** 封面 URL */
    private String coverUrl;

    /** 是否完结 */
    private Boolean finished;

    /** 评分 (0-10) */
    private Integer score;

    /** 收藏数 */
    private Integer likes;

    /** 浏览数 */
    private Integer views;

    /** 评分 (0.0-5.0) */
    private Double rating;

    /** 标签列表 */
    private List<String> tags;

    /** 是否为热门作品 */
    private Integer isHot;

    /** 是否为新品 */
    private Integer isNew;

    /** 是否为限时免费 */
    private Integer isLimitedFree;

    /** 是否为会员作品 */
    private Integer isBaoyue;

    /** 章节总数 */
    private Integer totalChapters;

    /** 最大章节序号（自增） */
    private Integer maxChapterOrder;

    /** 最后阅读章节ID */
    private Long lastReadChapterId;

    /** 最后阅读章节标题 */
    private String lastReadChapterTitle;

    /** 是否在书架中 */
    private Boolean inBookshelf;
}
