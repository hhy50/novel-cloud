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
    private final ReadingHistoryRepository readingHistoryRepository;
    private final UserBookshelfRepository userBookshelfRepository;

    @Data
    @Builder
    public static class BookstoreSection {
        private Integer style;
        private String title;
        private List<BookInfo> books;
    }

    @Data
    @Builder
    public static class BookDetailResult {
        private BookInfo bookInfo;
        private BookChapter lastReadChapter;
        private boolean inBookshelf;
        private List<BookChapter> chapters;
    }

    @Data
    @Builder
    public static class ChapterRangeResult {
        private BookChapter currentChapter;
        private List<BookChapter> previousChapters;
        private List<BookChapter> nextChapters;
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

    public BookDetailResult getBookDetail(Long bookId, Long userId) {
        BookInfo bookInfo = bookInfoRepository.findById(bookId);
        if (bookInfo == null) {
            return null;
        }

        BookChapter lastReadChapter = null;
        if (userId != null) {
            ReadingHistory lastRead = readingHistoryRepository.findLastReadByUserIdAndBookId(userId, bookId);
            if (lastRead != null) {
                lastReadChapter = bookChapterRepository.findById(lastRead.getChapterId());
            }
        }

        boolean inBookshelf = false;
        if (userId != null) {
            UserBookshelf bookshelf = userBookshelfRepository.findByUserIdAndBookId(userId, bookId);
            inBookshelf = (bookshelf != null);
        }

        List<BookChapter> chapters = bookChapterRepository.findByBookId(bookId, 7);

        return BookDetailResult.builder()
                .bookInfo(bookInfo)
                .lastReadChapter(lastReadChapter)
                .inBookshelf(inBookshelf)
                .chapters(chapters)
                .build();
    }

    public List<BookChapter> getBookChapterList(Long bookId, Integer limit) {
        List<BookChapter> chapters = bookChapterRepository.findByBookId(bookId, limit);
        for (BookChapter chapter : chapters) {
            chapter.setUnlockStatus(chapter.getIsVip());
        }
        return chapters;
    }

    public BookChapter getChapterInfo(Long bookId, Long chapterId) {
        BookChapter chapter = bookChapterRepository.findById(chapterId);
        chapter.setUnlockStatus(chapter.getIsVip());
        return chapter;
    }

    public ChapterContent getChapterContent(Long bookId, Long chapterId) {
        ChapterContent cc = chapterContentRepository.findByBookIdAndChapterId(bookId, chapterId);
        return cc;
    }

    public ChapterRangeResult getChapterRange(Long bookId, Long chapterId, Integer rangeSize) {
        int effectiveRangeSize = (rangeSize != null && rangeSize >= 0) ? rangeSize : 20;

        BookChapter currentChapter = bookChapterRepository.findById(chapterId);
        if (currentChapter == null) {
            return null;
        }

        if (currentChapter.getBookId() != null && !currentChapter.getBookId().equals(bookId)) {
            return null;
        }

        Integer currentNumber = currentChapter.getNumber();
        int startNumber = Math.max(1, currentNumber - effectiveRangeSize);
        int endNumber = currentNumber + effectiveRangeSize;

        List<BookChapter> rangeChapters = bookChapterRepository.findByNumberRange(bookId, startNumber, endNumber);

        List<BookChapter> previousChapters = rangeChapters.stream()
                .filter(chapter -> chapter.getNumber() < currentNumber)
                .collect(Collectors.toList());

        List<BookChapter> nextChapters = rangeChapters.stream()
                .filter(chapter -> chapter.getNumber() > currentNumber)
                .collect(Collectors.toList());

        BookInfo bookInfo = bookInfoRepository.findById(bookId);
        Integer totalChapters = (bookInfo != null) ? bookInfo.getTotalChapters() : 0;

        return ChapterRangeResult.builder()
                .currentChapter(currentChapter)
                .previousChapters(previousChapters)
                .nextChapters(nextChapters)
                .totalChapters(totalChapters)
                .build();
    }
}
