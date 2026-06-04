package com.novel.book.domain.repository;

import com.novel.book.domain.entity.BookInfo;
import com.novel.common.core.domain.PageResult;
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
}
