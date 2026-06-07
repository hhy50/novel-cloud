package com.novel.cloud.book.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 章节正文 - 领域实体（对齐 n_chapter_content_{0..9} 分表 DDL）。
 * 与章节元数据（{@link BookChapter}）分开存储：元数据轻、可全表索引；
 * 正文重、按 book_id 取模 10 分表。
 */
@Data
public class ChapterContent {

    /** 章节ID（与 BookChapter.id 一致） */
    private Long id;

    /** 书籍ID，分表路由键 */
    private Long bookId;

    /** 字数 */
    private Integer wordscount;

    /** 章节正文 */
    private String content;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private LocalDateTime deleteTime;
}
