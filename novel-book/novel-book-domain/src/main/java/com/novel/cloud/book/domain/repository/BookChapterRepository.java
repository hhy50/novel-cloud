package com.novel.cloud.book.domain.repository;

import com.novel.cloud.book.domain.entity.BookChapter;

import java.util.List;

/**
 * 书籍章节仓储接口
 */
public interface BookChapterRepository {

    /**
     * 根据书籍ID查询章节列表
     * @param bookId 书籍ID
     * @param limit 限制数量，-1表示查询全部
     * @return 章节列表
     */
    List<BookChapter> findByBookId(Long bookId, Integer limit);

    /**
     * 根据章节ID查询章节详情
     * @param chapterId 章节ID
     * @return 章节详情
     */
    BookChapter findById(Long chapterId);

    /**
     * 根据书籍ID和章节号查询章节详情
     * @param bookId 书籍ID
     * @param chapterNo 章节号
     * @return 章节详情
     */
    BookChapter findByBookIdAndChapterNo(Long bookId, Integer chapterNo);
}
