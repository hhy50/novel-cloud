package com.novel.cloud.book.adapter.web;

import com.novel.cloud.book.app.BookAppService;
import com.novel.cloud.book.dto.*;
import com.novel.cloud.common.domain.R;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    public R<BookDetailVo> getBookDetail(@Valid @RequestBody BookDetailQueryDto params) {
        return R.ok(bookAppService.getBookDetail(params));
    }

    @PostMapping("/chapters")
    public R<BookChapterListVo> getBookChapterList(@Valid @RequestBody BookChapterListQueryDto params) {
        return R.ok(bookAppService.getBookChapterList(params));
    }

    @PostMapping("/chapter/content")
    public R<BookChapterVo> getBookChapterContent(@Valid @RequestBody BookChapterContentQueryDto params) {
        return R.ok(bookAppService.getBookChapterContent(params));
    }

    @PostMapping("/bookstore")
    public R<BookstoreVo> getBookstore(@RequestBody(required = false) BookstoreQueryDto params) {
        return R.ok(bookAppService.getBookstore(params));
    }
}