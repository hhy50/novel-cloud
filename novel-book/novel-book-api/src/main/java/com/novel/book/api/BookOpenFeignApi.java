package com.novel.book.api;

import com.novel.book.dto.BookDetailQueryDto;
import com.novel.book.dto.BookDetailVo;
import com.novel.common.core.domain.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "novel-book-server", contextId = "bookOpenFeignApi", path = "/api/open/book")
public interface BookOpenFeignApi {

    @PostMapping("/detail")
    R<BookDetailVo> getBookDetail(@RequestBody BookDetailQueryDto params);
}
