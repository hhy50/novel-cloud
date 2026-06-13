package com.novel.cloud.activity.domain.repository;

import com.novel.cloud.activity.domain.entity.UserTaskRecord;
import java.time.LocalDate;
import java.util.List;

/**
 * User task record repository interface
 */
public interface UserTaskRecordRepository {

    List<UserTaskRecord> findByUserIdAndDate(Long userId, LocalDate date);

    UserTaskRecord findByUserIdAndTaskIdAndDate(Long userId, Long taskId, LocalDate date);

    void save(UserTaskRecord record);

    void updateById(UserTaskRecord record);
}
