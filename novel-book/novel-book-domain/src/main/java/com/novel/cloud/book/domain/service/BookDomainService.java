package com.novel.cloud.book.domain.service;

import com.novel.cloud.book.domain.entity.*;
import com.novel.cloud.book.domain.repository.*;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static cn.hutool.core.util.NumberUtil.min;

@Service
@RequiredArgsConstructor
public class BookDomainService {

    private final BookInfoRepository bookInfoRepository;
    private final BookChapterRepository bookChapterRepository;
    private final ChapterContentRepository chapterContentRepository;
    private final StoreCategoryStyleRepository storeCategoryStyleRepository;
    private final BookCategoryRepository bookCategoryRepository;

    @Data
    @Builder
    public static class BookstoreSection {
        private Integer style;
        private String title;
        private List<BookInfo> books;
    }

    @Data
    @Builder
    public static class ChapterRangeResult {
        private List<BookChapter> chapters;
        private Integer totalChapters;
    }

    public List<BookstoreSection> getBookstore(String appCode, String language) {
        String effectiveAppCode = (appCode == null || appCode.isBlank()) ? "A1" : appCode;
        String effectiveLanguage = (language == null || language.isBlank()) ? "zh" : language;

        List<StoreCategoryStyle> rootCategories = storeCategoryStyleRepository
                .findRootCategories(effectiveAppCode, effectiveLanguage);

        return rootCategories.stream()
                .map(category -> {
                    List<Long> bookIds = bookCategoryRepository.findByCategoryId(category.getId())
                            .stream()
                            .map(BookCategory::getBookId)
                            .collect(Collectors.toList());

                    List<BookInfo> books = bookCategoryRepository.findBooksByIds(bookIds.subList(0, min(10, bookIds.size())));

                    return BookstoreSection.builder()
                            .style(category.getStyleCode())
                            .title(category.getName())
                            .books(books)
                            .build();
                })
                .collect(Collectors.toList());
    }

    public BookInfo getBookDetail(Long bookId) {
        return bookInfoRepository.findById(bookId);
    }

    public List<BookChapter> getBookChapterList(Long bookId, Integer limit) {
        List<BookChapter> chapters = bookChapterRepository.findByBookId(bookId, limit);
        for (BookChapter chapter : chapters) {
            chapter.setUnlockStatus(chapter.getIsVip() == 1 ? 0 : 1);
        }
        return chapters;
    }

    public BookChapter getChapterInfo(Long bookId, Long chapterId) {
        BookChapter chapter = bookChapterRepository.findById(chapterId);
        chapter.setUnlockStatus(chapter.getIsVip() == 1 ? 0 : 1);
        return chapter;
    }

    public ChapterContent getChapterContent(Long bookId, Long chapterId) {
        ChapterContent cc = chapterContentRepository.findByBookIdAndChapterId(bookId, chapterId);
        return cc;
    }

    public ChapterRangeResult getChapterRange(Long bookId, Long chapterId, Integer rangeSize) {
        int effectiveRangeSize = (rangeSize != null && rangeSize >= 0) ? rangeSize : 20;

        // 查询指定章节以确认存在且属于该书
        BookChapter currentChapter = bookChapterRepository.findById(chapterId);
        if (currentChapter == null || !currentChapter.getBookId().equals(bookId)) {
            return null;
        }

        // 计算查询范围（当前章节的上下n条）
        Integer currentNumber = currentChapter.getNumber();
        int startNumber = Math.max(1, currentNumber - effectiveRangeSize);
        int endNumber = currentNumber + effectiveRangeSize;

        // 直接查询范围内的所有章节（已按序号排序）
        List<BookChapter> chapters = bookChapterRepository.findByNumberRange(bookId, startNumber, endNumber);

        // 设置解锁状态
        for (BookChapter chapter : chapters) {
            chapter.setUnlockStatus(chapter.getIsVip() == 1 ? 0 : 1);
        }

        // 获取总章节数
        BookInfo bookInfo = bookInfoRepository.findById(bookId);
        Integer totalChapters = (bookInfo != null) ? bookInfo.getTotalChapters() : 0;

        return ChapterRangeResult.builder()
                .chapters(chapters)
                .totalChapters(totalChapters)
                .build();
    }
}
