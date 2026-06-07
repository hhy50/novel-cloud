package com.novel.cloud.book.app;

import cn.dev33.satoken.stp.StpUtil;
import com.novel.cloud.book.domain.entity.BookChapter;
import com.novel.cloud.book.domain.entity.BookInfo;
import com.novel.cloud.book.domain.entity.ReadingHistory;
import com.novel.cloud.book.domain.entity.ReadingStats;
import com.novel.cloud.book.domain.entity.UserHistory;
import com.novel.cloud.book.domain.repository.BookChapterRepository;
import com.novel.cloud.book.domain.repository.BookInfoRepository;
import com.novel.cloud.book.domain.repository.ReadingHistoryRepository;
import com.novel.cloud.book.domain.repository.ReadingStatsRepository;
import com.novel.cloud.book.domain.repository.UserBookshelfRepository;
import com.novel.cloud.book.domain.repository.UserHistoryRepository;
import com.novel.cloud.book.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final BookChapterRepository bookChapterRepository;
    private final UserHistoryRepository userHistoryRepository;

    public ReadingHistoryVo getReadingHistory(ReadingHistoryQueryDto params) {
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
                    item.setBookName(bookInfo.getName());
                    item.setAuthorName(bookInfo.getAuthor());
                    item.setCoverUrl(bookInfo.getCover());
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
    }

    @Transactional
    public Boolean recordReading(RecordReadingDto params) {
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
    }

    /**
     * 点击 Start Reading：
     *   1) 解析起始章节 = 入参 chapterId ?: 上次流水的 chapterId ?: 该书第一章
     *   2) 隐式加入/刷新书架（upsert t_user_bookshelf）
     *   3) 往流水表 t_user_history 追加一条记录（每读一次必插一行）
     *   4) 返回前端进入阅读器所需的章节定位 + 进度
     *
     * 注意：仅 "开始阅读" 这个动作的记账；正文获取走 /api/book/chapter/content，
     * 阅读时长/翻页进度仍由 /api/book/reading/record 上报。
     */
    @Transactional
    public StartReadingVo startReading(StartReadingDto params) {
        Long userId = StpUtil.getLoginIdAsLong();
        Long bookId = params.getBookId();

        // --- 1. 上次流水（用来决定 chapter + 继承 progress + 判定首读） ---
        UserHistory lastHistory = userHistoryRepository.findLatestByUserIdAndBookId(userId, bookId);
        boolean isFirstRead = (lastHistory == null);

        // --- 2. 解析章节 ---
        BookChapter chapter = resolveStartChapter(bookId, params.getChapterId(), lastHistory);
        if (chapter == null) {
            // 书不存在或无任何章节 —— 直接抛业务异常更友好，这里返回 null 让 controller 决策
            return null;
        }

        // --- 3. 初始进度：仅当 "继续读上次的同一章" 时才继承 progress，否则归零 ---
        int progress = 0;
        if (lastHistory != null
                && lastHistory.getChapterId() != null
                && lastHistory.getChapterId().equals(chapter.getId())
                && lastHistory.getProgress() != null) {
            progress = lastHistory.getProgress();
        }

        // --- 4. 隐式加入书架（已存在则刷新最近阅读） ---
        userBookshelfRepository.addOrUpdate(userId, bookId, chapter.getId());

        // --- 5. 追加流水 ---
        UserHistory historyToSave = new UserHistory();
        historyToSave.setUserId(userId);
        historyToSave.setBookId(bookId);
        historyToSave.setChapterId(chapter.getId());
        historyToSave.setProgress(progress);
        userHistoryRepository.save(historyToSave);

        // --- 6. 组装返回 ---
        BookInfo bookInfo = bookInfoRepository.findById(bookId);

        StartReadingVo vo = new StartReadingVo();
        vo.setBookId(bookId);
        vo.setChapterId(chapter.getId());
        vo.setChapterTitle(chapter.getTitle());
        vo.setChapterOrder(chapter.getNumber());
        vo.setProgress(progress);
        vo.setIsVip(chapter.getIsVip() != null && chapter.getIsVip() == 1);
        vo.setIsFirstRead(isFirstRead);
        vo.setTotalChapters(bookInfo != null ? bookInfo.getTotalChapters() : null);
        return vo;
    }

    /**
     * 优先级：显式传入 > 上次流水 > 该书第一章。
     * 任一环节查不到章节就返回 null（书不存在 / 章节被下架）。
     */
    private BookChapter resolveStartChapter(Long bookId, Long explicitChapterId, UserHistory lastHistory) {
        if (explicitChapterId != null) {
            BookChapter chapter = bookChapterRepository.findById(explicitChapterId);
            // 防越权：传入的 chapterId 必须属于本 book，否则忽略走兜底
            if (chapter != null && bookId.equals(chapter.getBookId())) {
                return chapter;
            }
        }
        if (lastHistory != null && lastHistory.getChapterId() != null) {
            BookChapter chapter = bookChapterRepository.findById(lastHistory.getChapterId());
            if (chapter != null && bookId.equals(chapter.getBookId())) {
                return chapter;
            }
            // 上次的章节查不到（下架/删除），fall through 到第一章
        }
        // 兜底：按 number ASC 的第一章
        List<BookChapter> firstChapter = bookChapterRepository.findByBookId(bookId, 1);
        return firstChapter.isEmpty() ? null : firstChapter.get(0);
    }

    public ReadingStatsVo getReadingStats() {
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
