package com.novel.cloud.user.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.novel.cloud.user.domain.entity.DailyCheckin;
import com.novel.cloud.user.domain.repository.DailyCheckinRepository;
import com.novel.cloud.user.infrastructure.dataobject.DailyCheckinDO;
import com.novel.cloud.user.infrastructure.mapper.DailyCheckinMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DailyCheckinRepositoryImpl implements DailyCheckinRepository {

    private final DailyCheckinMapper dailyCheckinMapper;

    @Override
    public DailyCheckin findByUserIdAndDate(Long userId, LocalDate date) {
        DailyCheckinDO checkinDO = dailyCheckinMapper.selectOne(
                new LambdaQueryWrapper<DailyCheckinDO>()
                        .eq(DailyCheckinDO::getUserId, userId)
                        .eq(DailyCheckinDO::getCheckinDate, date)
                        .last("limit 1")
        );
        if (checkinDO == null) {
            return null;
        }
        DailyCheckin checkin = new DailyCheckin();
        BeanUtils.copyProperties(checkinDO, checkin);
        return checkin;
    }

    @Override
    public DailyCheckin findLatestByUserId(Long userId) {
        DailyCheckinDO checkinDO = dailyCheckinMapper.selectOne(
                new LambdaQueryWrapper<DailyCheckinDO>()
                        .eq(DailyCheckinDO::getUserId, userId)
                        .orderByDesc(DailyCheckinDO::getCheckinDate)
                        .last("limit 1")
        );
        if (checkinDO == null) {
            return null;
        }
        DailyCheckin checkin = new DailyCheckin();
        BeanUtils.copyProperties(checkinDO, checkin);
        return checkin;
    }

    @Override
    public void save(DailyCheckin checkin) {
        DailyCheckinDO checkinDO = new DailyCheckinDO();
        BeanUtils.copyProperties(checkin, checkinDO);
        dailyCheckinMapper.insert(checkinDO);
    }
}
