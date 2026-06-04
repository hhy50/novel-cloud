package com.novel.user.domain.repository;

import com.novel.user.domain.entity.DailyTask;
import java.util.List;

/**
 * Daily task repository interface
 */
public interface DailyTaskRepository {

    List<DailyTask> findAllActive();

    DailyTask findById(Long id);
}
