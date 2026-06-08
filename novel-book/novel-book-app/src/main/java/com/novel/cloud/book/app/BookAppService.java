package com.novel.cloud.book.app;

import com.novel.cloud.book.domain.entity.BookChapter;
import com.novel.cloud.book.domain.entity.BookInfo;
import com.novel.cloud.book.domain.entity.ChapterContent;
import com.novel.cloud.book.domain.service.BookDomainService;
import com.novel.cloud.book.dto.request.*;
import com.novel.cloud.book.dto.response.*;
import com.novel.cloud.book.dto.vo.BookChapterVo;
import com.novel.cloud.book.dto.vo.BookstoreBookVo;
import com.novel.cloud.book.dto.vo.BookstoreSectionVo;
import com.novel.cloud.common.util.MetadataContext;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookAppService {

    private final BookDomainService bookDomainService;

    public BookstoreResp getBookstore(BookstoreQueryReq params) {
        List<BookDomainService.BookstoreSection> sections = bookDomainService.getBookstore(
                MetadataContext.getAppCode(),
                MetadataContext.getLanguage());

        List<BookstoreSectionVo> sectionVos = sections.stream()
                .map(this::toBookstoreSectionVo)
                .collect(Collectors.toList());

        BookstoreResp result = new BookstoreResp();
        result.setSections(sectionVos);
        return result;
    }

    public BookDetailResp getBookDetail(BookDetailQueryReq params) {
        Long userId = MetadataContext.getUserId();
        BookDomainService.BookDetailResult result = bookDomainService.getBookDetail(
                params.getBookId(), userId);

        if (result == null) {
            return null;
        }

        return toBookDetailResp(result);
    }

    public BookChapterListResp getBookChapterList(BookChapterListQueryReq params) {
        List<BookChapter> chapters = bookDomainService.getBookChapterList(params.getBookId(), params.getLength());

        BookChapterListResp resp = new BookChapterListResp();
        resp.setBookId(params.getBookId());
        resp.setChapters(chapters.stream()
                .map(this::toChapterVo)
                .toList());
        return resp;
    }

    public ChapterRangeResp getChapterRange(ChapterRangeQueryReq params) {
        BookDomainService.ChapterRangeResult result = bookDomainService.getChapterRange(params.getBookId(), params.getChapterId(), params.getRangeSize());
        if (result == null) {
            return null;
        }

        return toChapterRangeResp(result);
    }

    private BookstoreSectionVo toBookstoreSectionVo(BookDomainService.BookstoreSection section) {
        BookstoreSectionVo vo = new BookstoreSectionVo();
        vo.setStyle(section.getStyle());
        vo.setTitle(section.getTitle());
        vo.setBooks(section.getBooks().stream()
                .map(this::toBookstoreBookVo)
                .collect(Collectors.toList()));
        return vo;
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

    private BookDetailResp toBookDetailResp(BookDomainService.BookDetailResult result) {
        BookInfo bookInfo = result.getBookInfo();
        BookDetailResp detailVo = new BookDetailResp();
        detailVo.setBookId(bookInfo.getId());
        detailVo.setTitle(bookInfo.getName());
        detailVo.setAuthor(bookInfo.getAuthor());
        detailVo.setCoverUrl(bookInfo.getCover());
        detailVo.setDescription(bookInfo.getDescription());
        detailVo.setFinished(bookInfo.isFinished());
        detailVo.setIsHot(bookInfo.getIsHot());
        detailVo.setIsNew(bookInfo.getIsNew());
        detailVo.setIsLimitedFree(bookInfo.getIsLimitedFree());
        detailVo.setIsBaoyue(bookInfo.getIsBaoyue());
        detailVo.setScore(bookInfo.getScore());
        detailVo.setLikes(bookInfo.getTotalFavors());
        detailVo.setViews(bookInfo.getTotalViews());
        detailVo.setRating(bookInfo.getRating());
        detailVo.setTotalChapters(bookInfo.getTotalChapters());
        detailVo.setInBookshelf(result.isInBookshelf());

        if (result.getLastReadChapter() != null) {
            detailVo.setLastReadChapterId(result.getLastReadChapter().getId());
            detailVo.setLastReadChapterTitle(result.getLastReadChapter().getTitle());
        }

        detailVo.setChapters(result.getChapters().stream()
                .map(this::toChapterVo)
                .toList());
        return detailVo;
    }

    private ChapterRangeResp toChapterRangeResp(BookDomainService.ChapterRangeResult result) {
        ChapterRangeResp resp = new ChapterRangeResp();
        resp.setCurrentChapter(toChapterVo(result.getCurrentChapter()));
        resp.setPreviousChapters(result.getPreviousChapters().stream()
                .map(this::toChapterVo)
                .toList());
        resp.setNextChapters(result.getNextChapters().stream()
                .map(this::toChapterVo)
                .toList());
        resp.setTotalChapters(result.getTotalChapters());
        return resp;
    }

    private BookChapterVo toChapterVo(BookChapter chapter) {
        BookChapterVo vo = new BookChapterVo();
        vo.setChapterId(chapter.getId());
        vo.setChapterTitle(chapter.getTitle());
        vo.setWordsCount(chapter.getWordsCount());
        vo.setUnlockStatus(chapter.getUnlockStatus());
        return vo;
    }

    public ChapterContentResp getBookChapterContent(@Valid BookChapterContentQueryReq params) {
        BookChapter chapter = bookDomainService.getChapterInfo(params.getBookId(), params.getChapterId());
        if (chapter == null) {
            return null;
        }
        ChapterContent content = bookDomainService.getChapterContent(params.getBookId(), params.getChapterId());

        ChapterContentResp vo = new ChapterContentResp();
        vo.setChapterId(chapter.getId());
        vo.setChapterTitle(chapter.getTitle());
        vo.setWordsCount(chapter.getWordsCount());
        vo.setUnlockStatus(chapter.getUnlockStatus());
        vo.setContent(content.getContent());
        return vo;
    }
}
