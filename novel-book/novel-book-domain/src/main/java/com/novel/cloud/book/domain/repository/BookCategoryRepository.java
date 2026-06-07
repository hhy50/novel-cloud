package com.novel.cloud.book.domain.repository;

import com.novel.cloud.book.domain.entity.BookCategory;
import com.novel.cloud.book.domain.entity.BookInfo;

import java.util.List;

/**
 * 书籍-分类关联仓储接口
 */
public interface BookCategoryRepository {

    /**
     * 查询指定分类下的所有书籍ID
     */
    List<BookCategory> findByCategoryId(Long categoryId);

    /**
     * 批量查询书籍信息
     */
    List<BookInfo> findBooksByIds(List<Long> bookIds);
}
