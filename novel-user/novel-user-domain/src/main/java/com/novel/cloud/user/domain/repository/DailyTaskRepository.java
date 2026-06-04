package com.novel.cloud.user.domain.repository;

import com.novel.cloud.user.domain.entity.DailyTask;
import java.util.List;

/**
 * Daily task repository interface
 */
public interface DailyTaskRepository {

    List<DailyTask> findAllActive();

    DailyTask findById(Long id);
}
