package com.novel.book.service;

import com.novel.book.dto.BookDetailQueryDto;
import com.novel.book.dto.BookDetailVo;
import reactor.core.publisher.Mono;

public interface BookService {

    Mono<BookDetailVo> getBookDetail(BookDetailQueryDto params);
}
