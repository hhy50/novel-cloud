package com.novel.cloud.book.infrastructure.dataobject;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 章节内容 DO，对齐 n_chapter_content_{0..9} 分表 DDL。
 * 表按 book_id % 10 路由，不固定 @TableName / @TableId —— 物理表名在 Mapper 层由 Provider 注入。
 */
@Data
public class ChapterContentDO {

    /** 章节ID（与 n_book_chapter.id 一致） */
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
