package com.novel.cloud.book.dto.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 章节信息（共用）
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
     * 章节
     */
    private String chapterTitle;

    /**
     * 字数
     */
    private Integer wordsCount;

    /**
     * 是否解锁
     */
    private Integer unlockStatus;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
