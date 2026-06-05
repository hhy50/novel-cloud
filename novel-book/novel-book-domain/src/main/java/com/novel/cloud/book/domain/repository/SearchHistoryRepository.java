package com.novel.cloud.book.domain.repository;

import com.novel.cloud.book.domain.entity.SearchHistory;
import java.util.List;

/**
 * Search history repository interface
 */
public interface SearchHistoryRepository {

    List<SearchHistory> findByUserId(Long userId, Integer limit);

    SearchHistory findByUserIdAndKeyword(Long userId, String keyword);

    void save(SearchHistory history);

    void updateSearchCount(Long id, Integer count);

    void deleteByUserId(Long userId);

    /**
     * Find top hot search keywords ordered by search count descending
     */
    List<SearchHistory> findTopBySearchCount(Integer limit);
}
