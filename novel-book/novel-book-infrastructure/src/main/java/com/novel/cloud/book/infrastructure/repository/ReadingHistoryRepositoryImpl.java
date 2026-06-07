package com.novel.cloud.book.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.novel.cloud.book.domain.entity.ReadingHistory;
import com.novel.cloud.book.domain.repository.ReadingHistoryRepository;
import com.novel.cloud.book.infrastructure.dataobject.ReadingHistoryDO;
import com.novel.cloud.book.infrastructure.mapper.ReadingHistoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ReadingHistoryRepositoryImpl implements ReadingHistoryRepository {

    private final ReadingHistoryMapper readingHistoryMapper;

    @Override
    public List<ReadingHistory> findByUserId(Long userId, Integer page, Integer pageSize) {
        Page<ReadingHistoryDO> pageResult = readingHistoryMapper.selectPage(
                new Page<>(page, pageSize),
                new LambdaQueryWrapper<ReadingHistoryDO>()
                        .eq(ReadingHistoryDO::getUserId, userId)
                        .orderByDesc(ReadingHistoryDO::getCreateTime)
        );

        return pageResult.getRecords().stream()
                .map(historyDO -> {
                    ReadingHistory history = new ReadingHistory();
                    BeanUtils.copyProperties(historyDO, history);
                    return history;
                })
                .collect(Collectors.toList());
    }

    @Override
    public int countByUserId(Long userId) {
        return readingHistoryMapper.selectCount(
                new LambdaQueryWrapper<ReadingHistoryDO>()
                        .eq(ReadingHistoryDO::getUserId, userId)
        ).intValue();
    }

    @Override
    public void save(ReadingHistory history) {
        ReadingHistoryDO historyDO = new ReadingHistoryDO();
        BeanUtils.copyProperties(history, historyDO);
        readingHistoryMapper.insert(historyDO);
    }

    @Override
    public ReadingHistory findLastReadByUserIdAndBookId(Long userId, Long bookId) {
        ReadingHistoryDO historyDO = readingHistoryMapper.selectOne(
                new LambdaQueryWrapper<ReadingHistoryDO>()
                        .eq(ReadingHistoryDO::getUserId, userId)
                        .eq(ReadingHistoryDO::getBookId, bookId)
                        .orderByDesc(ReadingHistoryDO::getCreateTime)
                        .last("LIMIT 1")
        );

        if (historyDO == null) {
            return null;
        }

        ReadingHistory history = new ReadingHistory();
        BeanUtils.copyProperties(historyDO, history);
        return history;
    }
}
