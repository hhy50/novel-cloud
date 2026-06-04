package com.novel.user.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.novel.user.domain.entity.DailyTask;
import com.novel.user.domain.repository.DailyTaskRepository;
import com.novel.user.infrastructure.dataobject.DailyTaskDO;
import com.novel.user.infrastructure.mapper.DailyTaskMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DailyTaskRepositoryImpl implements DailyTaskRepository {

    private final DailyTaskMapper dailyTaskMapper;

    @Override
    public List<DailyTask> findAllActive() {
        List<DailyTaskDO> taskDOList = dailyTaskMapper.selectList(
                new LambdaQueryWrapper<DailyTaskDO>()
                        .eq(DailyTaskDO::getStatus, 1)
                        .orderByAsc(DailyTaskDO::getSortOrder)
        );

        return taskDOList.stream()
                .map(taskDO -> {
                    DailyTask task = new DailyTask();
                    BeanUtils.copyProperties(taskDO, task);
                    return task;
                })
                .collect(Collectors.toList());
    }

    @Override
    public DailyTask findById(Long id) {
        DailyTaskDO taskDO = dailyTaskMapper.selectById(id);
        if (taskDO == null) {
            return null;
        }
        DailyTask task = new DailyTask();
        BeanUtils.copyProperties(taskDO, task);
        return task;
    }
}
