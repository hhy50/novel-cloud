package com.novel.book.service.impl;

import com.novel.book.dto.BookDetailQueryDto;
import com.novel.book.dto.BookDetailVo;
import com.novel.book.service.BookService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class BookServiceImpl implements BookService {

    @Override
    public Mono<BookDetailVo> getBookDetail(BookDetailQueryDto params) {
        BookDetailVo detailVo = new BookDetailVo();
        detailVo.setBookId(params.getBookId());
        detailVo.setBookName("诡秘之主");
        detailVo.setAuthorName("爱潜水的乌贼");
        detailVo.setCategoryName("玄幻");
        detailVo.setCoverUrl("https://static.example.com/book/cover/default.png");
        detailVo.setDescription("示例书籍详情，后续可替换为数据库真实数据。");
        detailVo.setFinished(Boolean.TRUE);
        return Mono.just(detailVo);
    }
}
