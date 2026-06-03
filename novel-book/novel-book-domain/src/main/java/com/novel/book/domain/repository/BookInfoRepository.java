package com.novel.book.domain.repository;

import com.novel.book.domain.entity.BookInfo;

/**
 * 图书信息仓储接口
 */
public interface BookInfoRepository {

    BookInfo findById(Long id);
}
