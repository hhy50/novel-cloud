package com.novel.cloud.book.domain.service;

import com.novel.cloud.book.domain.entity.*;
import com.novel.cloud.book.domain.repository.*;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReadingDomainService {

    private final BookInfoRepository bookInfoRepository;
    private final BookChapterRepository bookChapterRepository;
    private final ReadingHistoryRepository readingHistoryRepository;
    private final ReadingStatsRepository readingStatsRepository;
    private final UserBookshelfRepository userBookshelfRepository;
    private final UserHistoryRepository userHistoryRepository;

    @Data
    @Builder
    public static class StartReadingResult {
        private BookInfo bookInfo;
        private BookChapter startChapter;
        private Integer progress;
        private boolean isFirstRead;
    }

    public StartReadingResult startReading(Long userId, Long bookId, Long explicitChapterId) {
        BookInfo bookInfo = bookInfoRepository.findById(bookId);
        if (bookInfo == null) {
            return null;
        }

        UserHistory lastHistory = userHistoryRepository.findLatestByUserIdAndBookId(userId, bookId);
        boolean isFirstRead = (lastHistory == null);

        BookChapter chapter = resolveStartChapter(bookId, explicitChapterId, lastHistory);
        if (chapter == null) {
            return null;
        }

        int progress = calculateInitialProgress(lastHistory, chapter.getId());
        userBookshelfRepository.addOrUpdate(userId, bookId, chapter.getId());

        UserHistory historyToSave = new UserHistory();
        historyToSave.setUserId(userId);
        historyToSave.setBookId(bookId);
        historyToSave.setChapterId(chapter.getId());
        historyToSave.setProgress(progress);
        userHistoryRepository.save(historyToSave);

        return StartReadingResult.builder()
                .bookInfo(bookInfo)
                .startChapter(chapter)
                .progress(progress)
                .isFirstRead(isFirstRead)
                .build();
    }

    public void recordReadingProgress(Long userId, Long bookId, Long chapterId, Integer progress, Integer duration) {
        ReadingHistory history = new ReadingHistory();
        history.setUserId(userId);
        history.setBookId(bookId);
        history.setChapterId(chapterId);
        history.setProgress(progress != null ? progress : 0);
        history.setDuration(duration != null ? duration : 0);
        readingHistoryRepository.save(history);

        userBookshelfRepository.updateLastRead(userId, bookId, chapterId);
        updateReadingStats(userId, duration);
    }

    public BookChapter resolveStartChapter(Long bookId, Long explicitChapterId, UserHistory lastHistory) {
        if (explicitChapterId != null) {
            BookChapter chapter = bookChapterRepository.findById(explicitChapterId);
            if (chapter != null && chapter.getBookId() != null && chapter.getBookId().equals(bookId)) {
                return chapter;
            }
        }
        if (lastHistory != null && lastHistory.getChapterId() != null) {
            BookChapter chapter = bookChapterRepository.findById(lastHistory.getChapterId());
            if (chapter != null && chapter.getBookId() != null && chapter.getBookId().equals(bookId)) {
                return chapter;
            }
        }
        List<BookChapter> firstChapter = bookChapterRepository.findByBookId(bookId, 1);
        return firstChapter.isEmpty() ? null : firstChapter.get(0);
    }

    public int calculateInitialProgress(UserHistory lastHistory, Long chapterId) {
        if (lastHistory != null
                && lastHistory.getChapterId() != null
                && lastHistory.getChapterId().equals(chapterId)
                && lastHistory.getProgress() != null) {
            return lastHistory.getProgress();
        }
        return 0;
    }

    public void updateReadingStats(Long userId, Integer durationSeconds) {
        int duration = (durationSeconds != null) ? durationSeconds : 0;
        LocalDate today = LocalDate.now();
        ReadingStats stats = readingStatsRepository.findByUserIdAndDate(userId, today);

        if (stats == null) {
            stats = new ReadingStats();
            stats.setUserId(userId);
            stats.setStatDate(today);
            stats.setBooksRead(0);
            stats.setChaptersRead(1);
            stats.setMinutesRead(duration / 60);
            readingStatsRepository.save(stats);
        } else {
            stats.setChaptersRead(stats.getChaptersReadOrDefault() + 1);
            stats.setMinutesRead(stats.getMinutesReadOrDefault() + (duration / 60));
            readingStatsRepository.updateById(stats);
        }
    }
}
