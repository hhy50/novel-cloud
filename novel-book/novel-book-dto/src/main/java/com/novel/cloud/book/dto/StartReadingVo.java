package com.novel.cloud.book.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 开始阅读响应 VO，提供前端进入阅读器所需的最小信息。
 * 章节正文不在此返回，前端拿到 chapterId 后单独调 /chapter/content。
 */
@Data
public class StartReadingVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 书籍 ID */
    private Long bookId;

    /** 实际起始章节 ID */
    private Long chapterId;

    /** 章节标题 */
    private String chapterTitle;

    /** 章节序号（第几章） */
    private Integer chapterOrder;

    /** 章节内进度 0-100；从未读过返回 0 */
    private Integer progress;

    /** 是否 VIP 章节 */
    private Boolean isVip;

    /** 是否首次阅读（true=该用户对该书无任何流水记录） */
    private Boolean isFirstRead;

    /** 全书章节总数（便于前端展示 "x/N" 进度） */
    private Integer totalChapters;
}
