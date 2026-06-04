package com.novel.book.adapter.web;

import com.novel.book.app.*;
import com.novel.book.dto.*;
import com.novel.common.core.domain.R;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * 书籍 Controller
 * Adapter层：只做参数接收、入参校验、结果组装，无业务逻辑
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/book")
public class BookController {

    private final BookAppService bookAppService;
    private final BookshelfAppService bookshelfAppService;
    private final SearchAppService searchAppService;
    private final ReadingAppService readingAppService;
    private final ChapterPurchaseAppService chapterPurchaseAppService;

    @PostMapping("/detail")
    public Mono<R<BookDetailVo>> getBookDetail(@Valid @RequestBody BookDetailQueryDto params) {
        return bookAppService.getBookDetail(params).map(R::ok);
    }

    @PostMapping("/chapters")
    public Mono<R<BookChapterListVo>> getBookChapterList(@Valid @RequestBody BookChapterListQueryDto params) {
        return bookAppService.getBookChapterList(params).map(R::ok);
    }

    @PostMapping("/chapter/content")
    public Mono<R<BookChapterVo>> getBookChapterContent(@Valid @RequestBody BookChapterContentQueryDto params) {
        return bookAppService.getBookChapterContent(params).map(R::ok);
    }

    @PostMapping("/bookstore")
    public Mono<R<BookstoreVo>> getBookstore(@RequestBody(required = false) BookstoreQueryDto params) {
        return bookAppService.getBookstore(params).map(R::ok);
    }

    @GetMapping("/bookshelf")
    public Mono<R<BookshelfVo>> getBookshelf() {
        return bookshelfAppService.getBookshelf().map(R::ok);
    }

    @PostMapping("/bookshelf/add")
    public Mono<R<Boolean>> addToBookshelf(@Valid @RequestBody AddBookshelfDto params) {
        return bookshelfAppService.addToBookshelf(params).map(R::ok);
    }

    @PostMapping("/bookshelf/remove")
    public Mono<R<Boolean>> removeFromBookshelf(@Valid @RequestBody RemoveBookshelfDto params) {
        return bookshelfAppService.removeFromBookshelf(params).map(R::ok);
    }

    @PostMapping("/search")
    public Mono<R<SearchBooksVo>> searchBooks(@Valid @RequestBody SearchBooksDto params) {
        return searchAppService.searchBooks(params).map(R::ok);
    }

    @GetMapping("/search/history")
    public Mono<R<SearchHistoryVo>> getSearchHistory() {
        return searchAppService.getSearchHistory().map(R::ok);
    }

    @PostMapping("/search/history/clear")
    public Mono<R<Boolean>> clearSearchHistory() {
        return searchAppService.clearSearchHistory().map(R::ok);
    }

    @GetMapping("/search/hot")
    public Mono<R<HotSearchVo>> getHotSearch(@RequestParam(required = false) Integer limit) {
        return searchAppService.getHotSearch(limit).map(R::ok);
    }

    @GetMapping("/recommendations")
    public Mono<R<RecommendBooksVo>> getRecommendations() {
        return searchAppService.getRecommendations().map(R::ok);
    }

    @PostMapping("/reading/history")
    public Mono<R<ReadingHistoryVo>> getReadingHistory(@Valid @RequestBody ReadingHistoryQueryDto params) {
        return readingAppService.getReadingHistory(params).map(R::ok);
    }

    @PostMapping("/reading/record")
    public Mono<R<Boolean>> recordReading(@Valid @RequestBody RecordReadingDto params) {
        return readingAppService.recordReading(params).map(R::ok);
    }

    @GetMapping("/reading/stats")
    public Mono<R<ReadingStatsVo>> getReadingStats() {
        return readingAppService.getReadingStats().map(R::ok);
    }

    @PostMapping("/chapter/purchase")
    public Mono<R<PurchaseChapterVo>> purchaseChapter(@Valid @RequestBody PurchaseChapterDto params) {
        return chapterPurchaseAppService.purchaseChapter(params).map(R::ok);
    }
}
