package com.novel.cloud.activity.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.novel.cloud.activity.domain.entity.UserCheckinRecord;
import com.novel.cloud.activity.domain.repository.UserCheckinRecordRepository;
import com.novel.cloud.activity.infrastructure.dataobject.UserCheckinRecordDO;
import com.novel.cloud.activity.infrastructure.mapper.UserCheckinRecordMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * 用户签到记录仓储实现
 */
@Component
@RequiredArgsConstructor
public class UserCheckinRecordRepositoryImpl implements UserCheckinRecordRepository {

    private final UserCheckinRecordMapper userCheckinRecordMapper;

    @Override
    public UserCheckinRecord findByUserIdAndDate(Long userId, LocalDate date) {
        LambdaQueryWrapper<UserCheckinRecordDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserCheckinRecordDO::getUserId, userId)
                .eq(UserCheckinRecordDO::getCheckinDate, date)
                .last("limit 1");
        UserCheckinRecordDO recordDO = userCheckinRecordMapper.selectOne(wrapper);
        return toEntity(recordDO);
    }

    @Override
    public UserCheckinRecord findByUserIdAndWeek(Long userId, Integer weekOfYear) {
        LambdaQueryWrapper<UserCheckinRecordDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserCheckinRecordDO::getUserId, userId)
                .eq(UserCheckinRecordDO::getWeekOfYear, weekOfYear)
                .orderByDesc(UserCheckinRecordDO::getCheckinDate)
                .last("limit 1");
        UserCheckinRecordDO recordDO = userCheckinRecordMapper.selectOne(wrapper);
        return toEntity(recordDO);
    }

    @Override
    public UserCheckinRecord findLatestByUserId(Long userId) {
        LambdaQueryWrapper<UserCheckinRecordDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserCheckinRecordDO::getUserId, userId)
                .orderByDesc(UserCheckinRecordDO::getCheckinDate)
                .last("limit 1");
        UserCheckinRecordDO recordDO = userCheckinRecordMapper.selectOne(wrapper);
        return toEntity(recordDO);
    }

    @Override
    public void save(UserCheckinRecord record) {
        UserCheckinRecordDO recordDO = toDO(record);
        userCheckinRecordMapper.insert(recordDO);
        record.setId(recordDO.getId());
    }

    private UserCheckinRecord toEntity(UserCheckinRecordDO recordDO) {
        if (recordDO == null) {
            return null;
        }
        UserCheckinRecord record = new UserCheckinRecord();
        BeanUtils.copyProperties(recordDO, record);
        return record;
    }

    private UserCheckinRecordDO toDO(UserCheckinRecord record) {
        if (record == null) {
            return null;
        }
        UserCheckinRecordDO recordDO = new UserCheckinRecordDO();
        BeanUtils.copyProperties(record, recordDO);
        return recordDO;
    }
}
