package com.novel.user.domain.repository;

import com.novel.user.domain.entity.DailyCheckin;
import java.time.LocalDate;

/**
 * Daily check-in repository interface
 */
public interface DailyCheckinRepository {

    DailyCheckin findByUserIdAndDate(Long userId, LocalDate date);

    DailyCheckin findLatestByUserId(Long userId);

    void save(DailyCheckin checkin);
}
