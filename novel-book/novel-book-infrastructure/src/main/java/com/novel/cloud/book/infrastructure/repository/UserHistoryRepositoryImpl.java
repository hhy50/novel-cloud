package com.novel.cloud.book.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.novel.cloud.book.domain.entity.UserHistory;
import com.novel.cloud.book.domain.repository.UserHistoryRepository;
import com.novel.cloud.book.infrastructure.dataobject.UserHistoryDO;
import com.novel.cloud.book.infrastructure.mapper.UserHistoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserHistoryRepositoryImpl implements UserHistoryRepository {

    private final UserHistoryMapper userHistoryMapper;

    @Override
    public void save(UserHistory history) {
        UserHistoryDO historyDO = new UserHistoryDO();
        BeanUtils.copyProperties(history, historyDO);
        // create_time 走 DB DEFAULT CURRENT_TIMESTAMP，这里不显式赋值，保留 DB 时钟为唯一来源
        userHistoryMapper.insert(historyDO);
    }

    @Override
    public UserHistory findLatestByUserIdAndBookId(Long userId, Long bookId) {
        UserHistoryDO historyDO = userHistoryMapper.selectOne(
                new LambdaQueryWrapper<UserHistoryDO>()
                        .eq(UserHistoryDO::getUserId, userId)
                        .eq(UserHistoryDO::getBookId, bookId)
                        .orderByDesc(UserHistoryDO::getCreateTime)
                        .orderByDesc(UserHistoryDO::getId)
                        .last("LIMIT 1")
        );
        if (historyDO == null) {
            return null;
        }
        UserHistory history = new UserHistory();
        BeanUtils.copyProperties(historyDO, history);
        return history;
    }
}
