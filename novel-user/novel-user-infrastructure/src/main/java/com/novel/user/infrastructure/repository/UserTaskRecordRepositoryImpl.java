package com.novel.user.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.novel.user.domain.entity.UserTaskRecord;
import com.novel.user.domain.repository.UserTaskRecordRepository;
import com.novel.user.infrastructure.dataobject.UserTaskRecordDO;
import com.novel.user.infrastructure.mapper.UserTaskRecordMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserTaskRecordRepositoryImpl implements UserTaskRecordRepository {

    private final UserTaskRecordMapper userTaskRecordMapper;

    @Override
    public List<UserTaskRecord> findByUserIdAndDate(Long userId, LocalDate date) {
        List<UserTaskRecordDO> recordDOList = userTaskRecordMapper.selectList(
                new LambdaQueryWrapper<UserTaskRecordDO>()
                        .eq(UserTaskRecordDO::getUserId, userId)
                        .eq(UserTaskRecordDO::getTaskDate, date)
        );

        return recordDOList.stream()
                .map(recordDO -> {
                    UserTaskRecord record = new UserTaskRecord();
                    BeanUtils.copyProperties(recordDO, record);
                    return record;
                })
                .collect(Collectors.toList());
    }

    @Override
    public UserTaskRecord findByUserIdAndTaskIdAndDate(Long userId, Long taskId, LocalDate date) {
        UserTaskRecordDO recordDO = userTaskRecordMapper.selectOne(
                new LambdaQueryWrapper<UserTaskRecordDO>()
                        .eq(UserTaskRecordDO::getUserId, userId)
                        .eq(UserTaskRecordDO::getTaskId, taskId)
                        .eq(UserTaskRecordDO::getTaskDate, date)
                        .last("limit 1")
        );
        if (recordDO == null) {
            return null;
        }
        UserTaskRecord record = new UserTaskRecord();
        BeanUtils.copyProperties(recordDO, record);
        return record;
    }

    @Override
    public void save(UserTaskRecord record) {
        UserTaskRecordDO recordDO = new UserTaskRecordDO();
        BeanUtils.copyProperties(record, recordDO);
        userTaskRecordMapper.insert(recordDO);
    }

    @Override
    public void updateById(UserTaskRecord record) {
        UserTaskRecordDO recordDO = new UserTaskRecordDO();
        BeanUtils.copyProperties(record, recordDO);
        userTaskRecordMapper.updateById(recordDO);
    }
}
