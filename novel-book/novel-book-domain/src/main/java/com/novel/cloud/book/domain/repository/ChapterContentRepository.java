package com.novel.cloud.book.domain.repository;

import com.novel.cloud.book.domain.entity.ChapterContent;

/**
 * 章节正文仓储接口。
 * 实现层负责按 bookId 路由到对应分片表（n_chapter_content_0..9）。
 */
public interface ChapterContentRepository {

    /**
     * 按 (bookId, chapterId) 查询章节正文；找不到或已逻辑删除返回 null。
     * bookId 既是分表路由键，也作为兜底 where 条件，避免错路由时拿到同 id 的脏数据。
     */
    ChapterContent findByBookIdAndChapterId(Long bookId, Long chapterId);
}
