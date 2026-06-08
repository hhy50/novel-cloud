package com.novel.cloud.book.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 章节正文 - 领域实体（对齐 n_chapter_content_{0..9} 分表 DDL）。
 * 与章节元数据（BookChapter）分开存储：元数据轻、可全表索引；正文重、按 bookId 取模分表。
 */
@Data
public class ChapterContent {

    private Long id;
    private Long bookId;
    private Integer wordscount;
    private String content;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private LocalDateTime deleteTime;

    /**
     * 判断是否有内容
     */
    public boolean hasContent() {
        return content != null && !content.isBlank();
    }

    /**
     * 判断内容是否有效（未删除）
     */
    public boolean isValid() {
        return deleteTime == null;
    }

    /**
     * 获取字数（返回原始值）
     */
    public Integer getWordCountValue() {
        if (wordscount != null) {
            return wordscount;
        }
        return 0;
    }

    /**
     * 获取内容长度
     */
    public int getContentLength() {
        return content != null ? content.length() : 0;
    }
}
