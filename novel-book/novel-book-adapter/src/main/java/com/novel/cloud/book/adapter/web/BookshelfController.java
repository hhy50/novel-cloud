package com.novel.cloud.book.adapter.web;

import com.novel.cloud.book.app.BookshelfAppService;
import com.novel.cloud.book.dto.request.AddBookshelfReq;
import com.novel.cloud.book.dto.request.RemoveBookshelfReq;
import com.novel.cloud.book.dto.response.BookshelfResp;
import com.novel.cloud.common.domain.R;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 书架 Controller —— 查询书架、添加、移除。
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/book/bookshelf")
public class BookshelfController {

    private final BookshelfAppService bookshelfAppService;

    @PostMapping
    public R<BookshelfResp> listBookShelf() {
        return R.ok(bookshelfAppService.getBookshelf());
    }

    @PostMapping("/add")
    public R<Boolean> addToBookshelf(@Valid @RequestBody AddBookshelfReq params) {
        return R.ok(bookshelfAppService.addToBookshelf(params));
    }

    @PostMapping("/remove")
    public R<Boolean> removeFromBookshelf(@Valid @RequestBody RemoveBookshelfReq params) {
        return R.ok(bookshelfAppService.removeFromBookshelf(params));
    }
}