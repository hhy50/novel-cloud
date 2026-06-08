package com.novel.cloud.book.api;

import com.novel.cloud.book.dto.request.BookDetailQueryReq;
import com.novel.cloud.book.dto.vo.BookDetailVo;
import com.novel.cloud.common.domain.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "novel-book-service", contextId = "bookOpenFeignApi", path = "/api/open/book")
public interface BookOpenFeignApi {

    @PostMapping("/detail")
    R<BookDetailVo> getBookDetail(@RequestBody BookDetailQueryReq params);
}
