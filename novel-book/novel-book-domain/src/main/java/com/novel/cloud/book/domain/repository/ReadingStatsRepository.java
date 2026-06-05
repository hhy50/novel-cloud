package com.novel.cloud.book.domain.repository;

import com.novel.cloud.book.domain.entity.ReadingStats;
import java.time.LocalDate;

/**
 * Reading statistics repository interface
 */
public interface ReadingStatsRepository {

    ReadingStats findByUserIdAndDate(Long userId, LocalDate date);

    ReadingStats findSummaryByUserId(Long userId);

    void save(ReadingStats stats);

    void updateById(ReadingStats stats);
}
