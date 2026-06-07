package com.novel.cloud.book.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 章节内容 VO
 */
@Data
public class BookChapterVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 章节ID
     */
    private Long chapterId;

    /**
     * 章节名称
     */
    private String chapterName;

    /**
     * 章节内容（完整正文）
     */
    private String content;

    /**
     * 是否免费
     */
    private Boolean isFree;

    /**
     * 是否VIP章节
     */
    private Boolean isVip;

    /**
     * 是否已购买
     */
    private Boolean purchased;

    /**
     * 字数
     */
    private Integer wordCount;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 上一章ID
     */
    private Long prevChapterId;

    /**
     * 下一章ID
     */
    private Long nextChapterId;

    // ========== 以下字段仅用于章节列表 ==========

    /**
     * 章节标题（章节列表使用，与chapterName同义）
     */
    private String chapterTitle;

    /**
     * 章节序号
     */
    private Integer chapterOrder;

    /**
     * 金币价格
     */
    private Integer coinPrice;

    /**
     * 段落列表（兼容旧版本）
     */
    private java.util.List<String> paragraphs;
}
