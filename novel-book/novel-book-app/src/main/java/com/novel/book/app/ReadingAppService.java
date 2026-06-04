package com.novel.book.app;

import cn.dev33.satoken.stp.StpUtil;
import com.novel.book.domain.entity.BookInfo;
import com.novel.book.domain.entity.ReadingHistory;
import com.novel.book.domain.entity.ReadingStats;
import com.novel.book.domain.repository.BookInfoRepository;
import com.novel.book.domain.repository.ReadingHistoryRepository;
import com.novel.book.domain.repository.ReadingStatsRepository;
import com.novel.book.domain.repository.UserBookshelfRepository;
import com.novel.book.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Reading history and stats application service
 */
@Service
@RequiredArgsConstructor
public class ReadingAppService {

    private final ReadingHistoryRepository readingHistoryRepository;
    private final ReadingStatsRepository readingStatsRepository;
    private final UserBookshelfRepository userBookshelfRepository;
    private final BookInfoRepository bookInfoRepository;

    public Mono<ReadingHistoryVo> getReadingHistory(ReadingHistoryQueryDto params) {
        return Mono.fromCallable(() -> {
            Long userId = StpUtil.getLoginIdAsLong();

            List<ReadingHistory> historyList = readingHistoryRepository.findByUserId(
                    userId,
                    params.getPage(),
                    params.getPageSize()
            );

            int total = readingHistoryRepository.countByUserId(userId);

            List<ReadingHistoryItemVo> items = historyList.stream()
                    .map(history -> {
                        BookInfo bookInfo = bookInfoRepository.findById(history.getBookId());
                        if (bookInfo == null) {
                            return null;
                        }

                        ReadingHistoryItemVo item = new ReadingHistoryItemVo();
                        item.setBookId(history.getBookId());
                        item.setBookName(bookInfo.getBookName());
                        item.setAuthorName(bookInfo.getAuthorName());
                        item.setCoverUrl(bookInfo.getCoverUrl());
                        item.setChapterId(history.getChapterId());
                        // TODO: get chapter title from chapter repository
                        item.setChapterTitle("Chapter " + history.getChapterId());
                        item.setProgress(history.getProgress());
                        item.setDuration(history.getDuration());
                        item.setCreateTime(history.getCreateTime());
                        return item;
                    })
                    .filter(item -> item != null)
                    .collect(Collectors.toList());

            ReadingHistoryVo result = new ReadingHistoryVo();
            result.setRecords(items);
            result.setTotal(total);
            return result;
        });
    }

    @Transactional
    public Mono<Boolean> recordReading(RecordReadingDto params) {
        return Mono.fromCallable(() -> {
            Long userId = StpUtil.getLoginIdAsLong();

            // Save reading history
            ReadingHistory history = new ReadingHistory();
            history.setUserId(userId);
            history.setBookId(params.getBookId());
            history.setChapterId(params.getChapterId());
            history.setProgress(params.getProgress() != null ? params.getProgress() : 0);
            history.setDuration(params.getDuration() != null ? params.getDuration() : 0);
            readingHistoryRepository.save(history);

            // Update bookshelf last read
            userBookshelfRepository.updateLastRead(userId, params.getBookId(), params.getChapterId());

            // Update reading stats
            updateReadingStats(userId, params.getDuration() != null ? params.getDuration() : 0);

            return true;
        });
    }

    public Mono<ReadingStatsVo> getReadingStats() {
        return Mono.fromCallable(() -> {
            Long userId = StpUtil.getLoginIdAsLong();

            ReadingStats stats = readingStatsRepository.findSummaryByUserId(userId);

            ReadingStatsVo result = new ReadingStatsVo();
            if (stats != null) {
                result.setBooksRead(stats.getBooksRead() != null ? stats.getBooksRead() : 0);
                result.setChaptersRead(stats.getChaptersRead() != null ? stats.getChaptersRead() : 0);
                result.setMinutesRead(stats.getMinutesRead() != null ? stats.getMinutesRead() : 0);
                // daysActive is calculated in the SQL query
                result.setDaysActive(stats.getBooksRead() != null ? stats.getBooksRead() : 0);
            } else {
                result.setBooksRead(0);
                result.setChaptersRead(0);
                result.setMinutesRead(0);
                result.setDaysActive(0);
            }
            return result;
        });
    }

    private void updateReadingStats(Long userId, Integer durationSeconds) {
        LocalDate today = LocalDate.now();
        ReadingStats stats = readingStatsRepository.findByUserIdAndDate(userId, today);

        if (stats == null) {
            stats = new ReadingStats();
            stats.setUserId(userId);
            stats.setStatDate(today);
            stats.setBooksRead(0);
            stats.setChaptersRead(1);
            stats.setMinutesRead(durationSeconds / 60);
            readingStatsRepository.save(stats);
        } else {
            stats.setChaptersRead(stats.getChaptersRead() + 1);
            stats.setMinutesRead(stats.getMinutesRead() + (durationSeconds / 60));
            readingStatsRepository.updateById(stats);
        }
    }
}
