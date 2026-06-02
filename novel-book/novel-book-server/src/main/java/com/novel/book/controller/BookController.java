package com.novel.book.controller;

import com.novel.book.dto.BookDetailQueryDto;
import com.novel.book.dto.BookDetailVo;
import com.novel.book.dto.BookstoreQueryDto;
import com.novel.book.dto.BookstoreVo;
import com.novel.book.service.BookService;
import com.novel.common.core.domain.R;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/book")
public class BookController {

    private final BookService bookService;

    @PostMapping("/detail")
    public Mono<R<BookDetailVo>> getBookDetail(@Valid @RequestBody BookDetailQueryDto params) {
        return bookService.getBookDetail(params).map(R::ok);
    }

    @PostMapping("/bookstore")
    public Mono<R<BookstoreVo>> getBookstore(@RequestBody(required = false) BookstoreQueryDto params) {
        return bookService.getBookstore(params).map(R::ok);
    }
}
