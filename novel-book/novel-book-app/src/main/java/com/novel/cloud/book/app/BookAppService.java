package com.novel.cloud.book.app;

import com.novel.cloud.book.domain.entity.BookCategory;
import com.novel.cloud.book.domain.entity.BookChapter;
import com.novel.cloud.book.domain.entity.BookInfo;
import com.novel.cloud.book.domain.entity.ChapterContent;
import com.novel.cloud.book.domain.entity.ReadingHistory;
import com.novel.cloud.book.domain.entity.StoreCategoryStyle;
import com.novel.cloud.book.domain.entity.UserBookshelf;
import com.novel.cloud.book.domain.repository.BookCategoryRepository;
import com.novel.cloud.book.domain.repository.BookChapterRepository;
import com.novel.cloud.book.domain.repository.BookInfoRepository;
import com.novel.cloud.book.domain.repository.ChapterContentRepository;
import com.novel.cloud.book.domain.repository.ReadingHistoryRepository;
import com.novel.cloud.book.domain.repository.StoreCategoryStyleRepository;
import com.novel.cloud.book.domain.repository.UserBookshelfRepository;
import com.novel.cloud.book.dto.*;
import com.novel.cloud.common.util.MetadataContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 书籍应用服务
 * 职责：用例编排、事务控制，不写核心业务规则
 */
@Service
@RequiredArgsConstructor
public class BookAppService {

    private final BookInfoRepository bookInfoRepository;
    private final StoreCategoryStyleRepository storeCategoryStyleRepository;
    private final BookCategoryRepository bookCategoryRepository;
    private final BookChapterRepository bookChapterRepository;
    private final ChapterContentRepository chapterContentRepository;
    private final ReadingHistoryRepository readingHistoryRepository;
    private final UserBookshelfRepository userBookshelfRepository;

    // ── 书城首页 ──────────────────────────────────────────────────

    /**
     * 书城首页，从 t_store_category_style 驱动分类和样式，
     * 通过 t_book_category 关联查询每个分类下的书籍。
     * <p>
     * 返回格式: {"sections": [{"style": 1, "title": "编辑推荐", "tags": [...], "books": [...]}]}
     */
    public BookstoreVo getBookstore(BookstoreQueryDto params) {
        String appCode = MetadataContext.getAppCode();
        if (appCode == null || appCode.isBlank()) {
            appCode = "A1";
        }
        String language = MetadataContext.getLanguage();
        if (language == null || language.isBlank()) {
            language = "zh";
        }

        // 1. 查询顶层分类
        List<StoreCategoryStyle> rootCategories = storeCategoryStyleRepository
                .findRootCategories(appCode, language);

        List<BookstoreSectionVo> sections = new ArrayList<>();

        for (StoreCategoryStyle category : rootCategories) {
            BookstoreSectionVo section = new BookstoreSectionVo();
            section.setStyle(category.getStyleCode());
            section.setTitle(category.getName());

            // 2. 通过 t_book_category 查出该分类下的 bookId 列表
            List<Long> bookIds = bookCategoryRepository.findByCategoryId(category.getId())
                    .stream()
                    .map(BookCategory::getBookId)
                    .collect(Collectors.toList());

            // 3. 批量查询书籍信息
            List<BookInfo> books = bookCategoryRepository.findBooksByIds(bookIds);

            // 4. 转换成前端需要的 VO
            List<BookstoreBookVo> bookVOs = books.stream()
                    .map(this::toBookstoreBookVo)
                    .collect(Collectors.toList());

            section.setBooks(bookVOs);
            sections.add(section);
        }

        BookstoreVo result = new BookstoreVo();
        result.setSections(sections);
        return result;
    }

    private BookstoreBookVo toBookstoreBookVo(BookInfo book) {
        BookstoreBookVo vo = new BookstoreBookVo();
        vo.setBookId(book.getId());
        vo.setTitle(book.getName());
        vo.setAuthor(book.getAuthor());
        vo.setDescription(book.getDescription());
        vo.setCoverUrl(book.getCover());
        vo.setIsHot(book.getIsHot());
        vo.setIsNew(book.getIsNew());
        vo.setIsLimitedFree(book.getIsLimitedFree());
        vo.setIsBaoyue(book.getIsBaoyue());
        vo.setScore(book.getScore());
        vo.setTotalChapters(book.getTotalChapters());
        return vo;
    }

    // ── 书籍详情 ──────────────────────────────────────────────────

    public BookDetailVo getBookDetail(BookDetailQueryDto params) {
        BookInfo bookInfo = bookInfoRepository.findById(params.getBookId());
        BookDetailVo detailVo = new BookDetailVo();
        if (bookInfo == null) {
            return null;
        }
        detailVo.setBookId(bookInfo.getId());
        detailVo.setTitle(bookInfo.getName());
        detailVo.setAuthor(bookInfo.getAuthor());
        detailVo.setCoverUrl(bookInfo.getCover());
        detailVo.setDescription(bookInfo.getDescription());
        detailVo.setFinished(bookInfo.getStatus() != null && bookInfo.getStatus() == 2);
        detailVo.setIsHot(bookInfo.getIsHot());
        detailVo.setIsNew(bookInfo.getIsNew());
        detailVo.setIsLimitedFree(bookInfo.getIsLimitedFree());
        detailVo.setIsBaoyue(bookInfo.getIsBaoyue());
        detailVo.setScore(bookInfo.getScore());
        detailVo.setLikes(bookInfo.getTotalFavors());
        detailVo.setViews(bookInfo.getTotalViews());
        detailVo.setRating(bookInfo.getScore() != null ? bookInfo.getScore() / 2.0 : 0.0);
        detailVo.setTotalChapters(bookInfo.getTotalChapters());

        // 查询最后阅读章节
        Long userId = MetadataContext.getUserId();
        ReadingHistory lastRead = readingHistoryRepository.findLastReadByUserIdAndBookId(userId, params.getBookId());
        if (lastRead != null) {
            detailVo.setLastReadChapterId(lastRead.getChapterId());
            // 查询章节标题
            BookChapter lastChapter = bookChapterRepository.findById(lastRead.getChapterId());
            if (lastChapter != null) {
                detailVo.setLastReadChapterTitle(lastChapter.getTitle());
            }
        }

        // 查询是否在书架中
        UserBookshelf bookshelf = userBookshelfRepository.findByUserIdAndBookId(userId, params.getBookId());
        detailVo.setInBookshelf(bookshelf != null);

        List<BookChapter> chapters = bookChapterRepository.findByBookId(params.getBookId(), 7);
        detailVo.setChapters(chapters.stream()
                .map(this::toChapterVo)
                .toList());
        ;
        return detailVo;
    }


    public BookChapterListVo getBookChapterList(BookChapterListQueryDto params) {
        // 从数据库查询章节列表
        List<BookChapter> chapters = bookChapterRepository.findByBookId(params.getBookId(), params.getLength());

        BookChapterListVo chapterListVo = new BookChapterListVo();
        chapterListVo.setBookId(params.getBookId());
        chapterListVo.setChapters(chapters.stream()
                .map(this::toChapterVo)
                .toList());
        return chapterListVo;
    }

    public BookChapterVo getBookChapterContent(BookChapterContentQueryDto params) {
        // 1) 元数据：n_book_chapter（全表，无分片）
        BookChapter chapter = bookChapterRepository.findById(params.getChapterId());
        if (chapter == null) {
            return null;
        }
        // 路由完整性校验：DTO 传入的 bookId 必须与章节归属一致，
        // 否则按错 bookId 路由会去 wrong shard 查空数据，索性提前阻断更直观。
        if (chapter.getBookId() != null && !chapter.getBookId().equals(params.getBookId())) {
            return null;
        }

        // 2) 正文：n_chapter_content_{bookId % 10}（分表）
        ChapterContent content = chapterContentRepository
                .findByBookIdAndChapterId(params.getBookId(), params.getChapterId());

        return toChapterVoWithContent(chapter, content);
    }

    // ── 辅助方法 ──────────────────────────────────────────────────

    /**
     * 将 BookChapter 实体转换为 BookChapterVo (不含内容,用于章节列表)
     */
    private BookChapterVo toChapterVo(BookChapter chapter) {
        BookChapterVo vo = new BookChapterVo();
        vo.setChapterId(chapter.getId());
        vo.setChapterTitle(chapter.getTitle());
        vo.setIsVip(chapter.getIsVip() != null && chapter.getIsVip() == 1);
        // 章节列表不返回内容
        return vo;
    }

    /**
     * 将 BookChapter 元数据 + ChapterContent 正文组装为 BookChapterVo（含 content）。
     * 正文为 null 时（章节未上线 / 数据缺失）返回空字符串，保持响应结构稳定。
     */
    private BookChapterVo toChapterVoWithContent(BookChapter chapter, ChapterContent content) {
        BookChapterVo vo = new BookChapterVo();
        vo.setChapterId(chapter.getId());
        vo.setChapterTitle(chapter.getTitle());
        vo.setChapterName(chapter.getTitle());
        vo.setIsVip(chapter.getIsVip() != null && chapter.getIsVip() == 1);
        vo.setChapterOrder(chapter.getNumber());
        vo.setUpdateTime(content != null ? content.getUpdateTime() : chapter.getUpdateTime());

        if (content != null) {
            vo.setContent(content.getContent());
            vo.setWordCount(content.getWordscount() != null ? content.getWordscount() : chapter.getWordscount());
        } else {
            vo.setContent("");
            vo.setWordCount(chapter.getWordscount());
        }
        return vo;
    }
}
