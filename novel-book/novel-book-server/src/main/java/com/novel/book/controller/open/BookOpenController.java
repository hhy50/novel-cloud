package com.novel.book.controller.open;

import com.novel.book.api.BookOpenFeignApi;
import com.novel.book.dto.BookDetailQueryDto;
import com.novel.book.dto.BookDetailVo;
import com.novel.book.service.BookService;
import com.novel.common.core.domain.R;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
public class BookOpenController implements BookOpenFeignApi {

    private final BookService bookService;

    @Override
    public R<BookDetailVo> getBookDetail(@Valid @RequestBody BookDetailQueryDto params) {
        return R.ok(bookService.getBookDetail(params).block());
    }
}
