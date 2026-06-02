package com.novel.book.service;

import com.novel.book.dto.BookDetailQueryDto;
import com.novel.book.dto.BookDetailVo;
import com.novel.book.dto.BookstoreQueryDto;
import com.novel.book.dto.BookstoreVo;
import reactor.core.publisher.Mono;

public interface BookService {

    Mono<BookDetailVo> getBookDetail(BookDetailQueryDto params);

    Mono<BookstoreVo> getBookstore(BookstoreQueryDto params);
}
