package com.novel.book.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.novel.book.domain.entity.ReadingStats;
import com.novel.book.domain.repository.ReadingStatsRepository;
import com.novel.book.infrastructure.dataobject.ReadingStatsDO;
import com.novel.book.infrastructure.mapper.ReadingStatsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class ReadingStatsRepositoryImpl implements ReadingStatsRepository {

    private final ReadingStatsMapper readingStatsMapper;

    @Override
    public ReadingStats findByUserIdAndDate(Long userId, LocalDate date) {
        ReadingStatsDO statsDO = readingStatsMapper.selectOne(
                new LambdaQueryWrapper<ReadingStatsDO>()
                        .eq(ReadingStatsDO::getUserId, userId)
                        .eq(ReadingStatsDO::getStatDate, date)
                        .last("limit 1")
        );
        if (statsDO == null) {
            return null;
        }
        ReadingStats stats = new ReadingStats();
        BeanUtils.copyProperties(statsDO, stats);
        return stats;
    }

    @Override
    public ReadingStats findSummaryByUserId(Long userId) {
        ReadingStatsDO statsDO = readingStatsMapper.selectSummaryByUserId(userId);
        if (statsDO == null) {
            return null;
        }
        ReadingStats stats = new ReadingStats();
        BeanUtils.copyProperties(statsDO, stats);
        return stats;
    }

    @Override
    public void save(ReadingStats stats) {
        ReadingStatsDO statsDO = new ReadingStatsDO();
        BeanUtils.copyProperties(stats, statsDO);
        readingStatsMapper.insert(statsDO);
    }

    @Override
    public void updateById(ReadingStats stats) {
        ReadingStatsDO statsDO = new ReadingStatsDO();
        BeanUtils.copyProperties(stats, statsDO);
        readingStatsMapper.updateById(statsDO);
    }
}
