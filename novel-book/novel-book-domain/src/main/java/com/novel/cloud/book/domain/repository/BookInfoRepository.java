package com.novel.cloud.book.domain.repository;

import com.novel.cloud.book.domain.entity.BookInfo;
import com.novel.cloud.common.domain.PageResult;

import java.util.List;

/**
 * 图书信息仓储接口
 */
public interface BookInfoRepository {

    BookInfo findById(Long id);

    /**
     * Search books by keyword (matches bookName, authorName, or description)
     * Returns paginated results using MyBatis-Plus Page
     */
    PageResult<BookInfo> searchByKeyword(String keyword, Integer page, Integer pageSize);

    /**
     * Find recommended books (e.g., by view/like count or editorial picks)
     */
    List<BookInfo> findRecommendations(Integer limit);

    /**
     * Find similar books for the detail page, excluding the current book.
     */
    List<BookInfo> findSimilarBooks(BookInfo currentBook, Integer limit);
}
