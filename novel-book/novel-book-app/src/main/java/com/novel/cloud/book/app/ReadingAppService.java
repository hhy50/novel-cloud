package com.novel.cloud.book.app;

import cn.dev33.satoken.stp.StpUtil;
import com.novel.cloud.book.domain.entity.BookChapter;
import com.novel.cloud.book.domain.entity.BookInfo;
import com.novel.cloud.book.domain.entity.ReadingHistory;
import com.novel.cloud.book.domain.entity.ReadingStats;
import com.novel.cloud.book.domain.repository.BookChapterRepository;
import com.novel.cloud.book.domain.repository.BookInfoRepository;
import com.novel.cloud.book.domain.repository.ReadingHistoryRepository;
import com.novel.cloud.book.domain.repository.ReadingStatsRepository;
import com.novel.cloud.book.domain.service.ReadingDomainService;
import com.novel.cloud.book.dto.request.ReadingHistoryQueryReq;
import com.novel.cloud.book.dto.request.RecordReadingReq;
import com.novel.cloud.book.dto.request.StartReadingReq;
import com.novel.cloud.book.dto.response.ReadingHistoryResp;
import com.novel.cloud.book.dto.response.ReadingStatsResp;
import com.novel.cloud.book.dto.response.StartReadingResp;
import com.novel.cloud.book.dto.vo.BookChapterVo;
import com.novel.cloud.book.dto.vo.BookDetailVo;
import com.novel.cloud.book.dto.vo.ReadingHistoryItemVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReadingAppService {

    private final ReadingHistoryRepository readingHistoryRepository;
    private final ReadingStatsRepository readingStatsRepository;
    private final BookInfoRepository bookInfoRepository;
    private final BookChapterRepository bookChapterRepository;
    private final ReadingDomainService readingDomainService;

    public ReadingHistoryResp getReadingHistory(ReadingHistoryQueryReq params) {
        Long userId = StpUtil.getLoginIdAsLong();
        List<ReadingHistory> historyList = readingHistoryRepository.findByUserId(
                userId, params.getPage(), params.getPageSize());

        int total = readingHistoryRepository.countByUserId(userId);

        List<ReadingHistoryItemVo> items = historyList.stream()
                .map(this::toReadingHistoryItemVo)
                .filter(item -> item != null)
                .collect(Collectors.toList());

        ReadingHistoryResp result = new ReadingHistoryResp();
        result.setRecords(items);
        result.setTotal(total);
        return result;
    }

    @Transactional
    public Boolean recordReading(RecordReadingReq params) {
        Long userId = StpUtil.getLoginIdAsLong();
        readingDomainService.recordReadingProgress(
                userId, params.getBookId(), params.getChapterId(),
                params.getProgress(), params.getDuration());
        return true;
    }

    @Transactional
    public StartReadingResp startReading(StartReadingReq params) {
        Long userId = StpUtil.getLoginIdAsLong();
        ReadingDomainService.StartReadingResult result = readingDomainService.startReading(
                userId, params.getBookId(), params.getChapterId());

        if (result == null) {
            return null;
        }

        return toStartReadingResp(result);
    }

    public ReadingStatsResp getReadingStats() {
        Long userId = StpUtil.getLoginIdAsLong();
        ReadingStats stats = readingStatsRepository.findSummaryByUserId(userId);

        ReadingStatsResp result = new ReadingStatsResp();
        if (stats != null) {
            result.setBooksRead(stats.getBooksReadOrDefault());
            result.setChaptersRead(stats.getChaptersReadOrDefault());
            result.setMinutesRead(stats.getMinutesReadOrDefault());
            result.setDaysActive(stats.getBooksReadOrDefault());
        } else {
            result.setBooksRead(0);
            result.setChaptersRead(0);
            result.setMinutesRead(0);
            result.setDaysActive(0);
        }
        return result;
    }

    private ReadingHistoryItemVo toReadingHistoryItemVo(ReadingHistory history) {
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
        item.setChapterTitle("Chapter " + history.getChapterId());
        item.setProgress(history.getProgress());
        item.setDuration(history.getDuration());
        item.setCreateTime(history.getCreateTime());
        return item;
    }

    private StartReadingResp toStartReadingResp(ReadingDomainService.StartReadingResult result) {
        BookInfo bookInfo = result.getBookInfo();
        BookChapter chapter = result.getStartChapter();

        StartReadingResp vo = new StartReadingResp();
        vo.setBookId(bookInfo.getId());
        vo.setChapterId(chapter.getId());
        vo.setChapterTitle(chapter.getTitle());
        vo.setChapterOrder(chapter.getNumber());
        vo.setProgress(result.getProgress());
        vo.setIsVip(chapter.isVipChapter());
        vo.setIsFirstRead(result.isFirstRead());
        vo.setTotalChapters(bookInfo.getTotalChapters());

        BookChapterVo chapterVo = new BookChapterVo();
        chapterVo.setChapterId(chapter.getId());
        chapterVo.setChapterTitle(chapter.getTitle());
        chapterVo.setChapterTitle(chapter.getTitle());
//        chapterVo.setChapterOrder(chapter.getNumber());
//        chapterVo.setIsVip(chapter.isVipChapter());
//        chapterVo.setIsFree(chapter.isFree());
        chapterVo.setWordsCount(chapter.getWordsCount());
        chapterVo.setUpdateTime(chapter.getUpdateTime());
        vo.setLastedReadChapter(chapterVo);

        BookDetailVo bookDetailVo = new BookDetailVo();
        bookDetailVo.setBookId(bookInfo.getId());
        bookDetailVo.setTitle(bookInfo.getName());
        bookDetailVo.setAuthor(bookInfo.getAuthor());
        bookDetailVo.setDescription(bookInfo.getDescription());
        bookDetailVo.setCoverUrl(bookInfo.getCover());
        bookDetailVo.setFinished(bookInfo.isFinished());
        bookDetailVo.setScore(bookInfo.getScore());
        bookDetailVo.setLikes(bookInfo.getTotalFavors());
        bookDetailVo.setViews(bookInfo.getTotalViews());
        bookDetailVo.setTotalChapters(bookInfo.getTotalChapters());

        BookChapter maxChapter = bookChapterRepository.findMaxChapterByBookId(bookInfo.getId());
        if (maxChapter != null) {
            bookDetailVo.setMaxChapterOrder(maxChapter.getNumber());
        }
        vo.setBookInfo(bookDetailVo);

        return vo;
    }
}
