package com.novel.cloud.book.domain.repository;

import com.novel.cloud.book.domain.entity.ReadingHistory;
import java.util.List;

/**
 * Reading history repository interface
 */
public interface ReadingHistoryRepository {

    List<ReadingHistory> findByUserId(Long userId, Integer page, Integer pageSize);

    int countByUserId(Long userId);

    void save(ReadingHistory history);

    /**
     * 查询用户对特定书籍的最后阅读记录
     */
    ReadingHistory findLastReadByUserIdAndBookId(Long userId, Long bookId);
}
