package com.novel.cloud.activity.domain.repository;

import com.novel.cloud.activity.domain.entity.DailyTask;
import java.util.List;

/**
 * Daily task repository interface
 */
public interface DailyTaskRepository {

    List<DailyTask> findAllActive();

    DailyTask findById(Long id);
}
