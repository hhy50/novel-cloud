package com.novel.book.domain.repository;

import com.novel.book.domain.entity.ReadingHistory;
import java.util.List;

/**
 * Reading history repository interface
 */
public interface ReadingHistoryRepository {

    List<ReadingHistory> findByUserId(Long userId, Integer page, Integer pageSize);

    int countByUserId(Long userId);

    void save(ReadingHistory history);
}
