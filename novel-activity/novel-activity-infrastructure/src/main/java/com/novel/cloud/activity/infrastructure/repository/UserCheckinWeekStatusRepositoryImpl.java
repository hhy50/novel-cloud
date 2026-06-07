package com.novel.cloud.activity.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.novel.cloud.activity.domain.entity.UserCheckinWeekStatus;
import com.novel.cloud.activity.domain.repository.UserCheckinWeekStatusRepository;
import com.novel.cloud.activity.infrastructure.dataobject.UserCheckinWeekStatusDO;
import com.novel.cloud.activity.infrastructure.mapper.UserCheckinWeekStatusMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

/**
 * 用户周签到状态仓储实现
 */
@Component
@RequiredArgsConstructor
public class UserCheckinWeekStatusRepositoryImpl implements UserCheckinWeekStatusRepository {

    private final UserCheckinWeekStatusMapper userCheckinWeekStatusMapper;

    @Override
    public UserCheckinWeekStatus findByUserIdAndWeek(Long userId, Integer weekOfYear) {
        LambdaQueryWrapper<UserCheckinWeekStatusDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserCheckinWeekStatusDO::getUserId, userId)
                .eq(UserCheckinWeekStatusDO::getWeekOfYear, weekOfYear)
                .last("limit 1");
        UserCheckinWeekStatusDO statusDO = userCheckinWeekStatusMapper.selectOne(wrapper);
        return toEntity(statusDO);
    }

    @Override
    public void save(UserCheckinWeekStatus status) {
        UserCheckinWeekStatusDO statusDO = toDO(status);
        userCheckinWeekStatusMapper.insert(statusDO);
        status.setId(statusDO.getId());
    }

    @Override
    public void updateById(UserCheckinWeekStatus status) {
        UserCheckinWeekStatusDO statusDO = toDO(status);
        userCheckinWeekStatusMapper.updateById(statusDO);
    }

    private UserCheckinWeekStatus toEntity(UserCheckinWeekStatusDO statusDO) {
        if (statusDO == null) {
            return null;
        }
        UserCheckinWeekStatus status = new UserCheckinWeekStatus();
        BeanUtils.copyProperties(statusDO, status);
        return status;
    }

    private UserCheckinWeekStatusDO toDO(UserCheckinWeekStatus status) {
        if (status == null) {
            return null;
        }
        UserCheckinWeekStatusDO statusDO = new UserCheckinWeekStatusDO();
        BeanUtils.copyProperties(status, statusDO);
        return statusDO;
    }
}
