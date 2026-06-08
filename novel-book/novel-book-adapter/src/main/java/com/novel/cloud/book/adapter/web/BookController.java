package com.novel.cloud.book.adapter.web;

import com.novel.cloud.book.app.BookAppService;
import com.novel.cloud.book.dto.request.*;
import com.novel.cloud.book.dto.response.*;
import com.novel.cloud.common.domain.R;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 书城 & 章节 Controller（书籍详情、章节列表、章节正文、书城首页）。
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/book")
public class BookController {

    private final BookAppService bookAppService;

    @PostMapping("/detail")
    public R<BookDetailResp> getBookDetail(@Valid @RequestBody BookDetailQueryReq params) {
        return R.ok(bookAppService.getBookDetail(params));
    }

    @PostMapping("/chapters")
    public R<BookChapterListResp> getBookChapterList(@Valid @RequestBody BookChapterListQueryReq params) {
        return R.ok(bookAppService.getBookChapterList(params));
    }

    @PostMapping("/chapter/content")
    public R<ChapterContentResp> getBookChapterContent(@Valid @RequestBody BookChapterContentQueryReq params) {
        return R.ok(bookAppService.getBookChapterContent(params));
    }

    @PostMapping("/chapter/range")
    public R<ChapterRangeResp> getChapterRange(@Valid @RequestBody ChapterRangeQueryReq params) {
        return R.ok(bookAppService.getChapterRange(params));
    }

    @PostMapping("/bookstore")
    public R<BookstoreResp> getBookstore(@RequestBody(required = false) BookstoreQueryReq params) {
        return R.ok(bookAppService.getBookstore(params));
    }
}