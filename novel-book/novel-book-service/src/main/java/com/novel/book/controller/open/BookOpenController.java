package com.novel.book.controller.open;

import com.novel.book.dto.BookDetailQueryDto;
import com.novel.book.dto.BookDetailVo;
import com.novel.book.service.BookService;
import com.novel.common.core.domain.R;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/open/book")
public class BookOpenController {

    private final BookService bookService;

    @PostMapping("/detail")
    public R<BookDetailVo> getBookDetail(@Valid @RequestBody BookDetailQueryDto params) {
        return R.ok(bookService.getBookDetail(params).block());
    }
}
