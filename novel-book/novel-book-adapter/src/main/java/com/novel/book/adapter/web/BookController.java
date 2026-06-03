package com.novel.book.adapter.web;

import com.novel.book.app.BookAppService;
import com.novel.book.dto.BookChapterContentQueryDto;
import com.novel.book.dto.BookChapterListQueryDto;
import com.novel.book.dto.BookChapterListVo;
import com.novel.book.dto.BookChapterVo;
import com.novel.book.dto.BookDetailQueryDto;
import com.novel.book.dto.BookDetailVo;
import com.novel.book.dto.BookstoreQueryDto;
import com.novel.book.dto.BookstoreVo;
import com.novel.common.core.domain.R;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}
