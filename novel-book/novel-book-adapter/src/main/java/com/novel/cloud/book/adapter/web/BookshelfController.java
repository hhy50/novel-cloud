package com.novel.cloud.book.adapter.web;

import com.novel.cloud.book.app.BookshelfAppService;
import com.novel.cloud.book.dto.AddBookshelfDto;
import com.novel.cloud.book.dto.BookshelfVo;
import com.novel.cloud.book.dto.RemoveBookshelfDto;
import com.novel.cloud.common.domain.R;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
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
    public R<BookshelfVo> listBookShelf() {
        return R.ok(bookshelfAppService.getBookshelf());
    }

    @PostMapping("/add")
    public R<Boolean> addToBookshelf(@Valid @RequestBody AddBookshelfDto params) {
        return R.ok(bookshelfAppService.addToBookshelf(params));
    }

    @PostMapping("/remove")
    public R<Boolean> removeFromBookshelf(@Valid @RequestBody RemoveBookshelfDto params) {
        return R.ok(bookshelfAppService.removeFromBookshelf(params));
    }
}