package com.novel.cloud.book.app;

import cn.dev33.satoken.stp.StpUtil;
import com.novel.cloud.book.domain.entity.BookInfo;
import com.novel.cloud.book.domain.service.BookshelfDomainService;
import com.novel.cloud.book.dto.request.AddBookshelfReq;
import com.novel.cloud.book.dto.request.RemoveBookshelfReq;
import com.novel.cloud.book.dto.response.BookshelfResp;
import com.novel.cloud.book.dto.vo.BookshelfItemVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookshelfAppService {

    private final BookshelfDomainService bookshelfDomainService;

    public BookshelfResp getBookshelf() {
        Long userId = StpUtil.getLoginIdAsLong();
        List<BookshelfDomainService.BookshelfItemResult> items = bookshelfDomainService.getBookshelf(userId);

        List<BookshelfItemVo> itemVos = items.stream()
                .map(this::toBookshelfItemVo)
                .collect(Collectors.toList());

        BookshelfResp result = new BookshelfResp();
        result.setBooks(itemVos);
        result.setTotal(itemVos.size());
        return result;
    }

    @Transactional
    public Boolean addToBookshelf(AddBookshelfReq params) {
        Long userId = StpUtil.getLoginIdAsLong();
        boolean success = bookshelfDomainService.addToBookshelf(userId, params.getBookId());
        if (!success) {
            throw new RuntimeException("Book not found");
        }
        return true;
    }

    @Transactional
    public Boolean removeFromBookshelf(RemoveBookshelfReq params) {
        Long userId = StpUtil.getLoginIdAsLong();
        bookshelfDomainService.removeFromBookshelf(userId, params.getBookId());
        return true;
    }

    private BookshelfItemVo toBookshelfItemVo(BookshelfDomainService.BookshelfItemResult item) {
        BookInfo bookInfo = item.getBookInfo();
        BookshelfItemVo vo = new BookshelfItemVo();
        vo.setBookId(bookInfo.getId());
        vo.setBookName(bookInfo.getName());
        vo.setAuthorName(bookInfo.getAuthor());
        vo.setCoverUrl(bookInfo.getCover());
        vo.setDescription(bookInfo.getDescription());
        vo.setFinished(bookInfo.isFinished());
        vo.setStatus(bookInfo.getStatus());
        vo.setTotalWords(bookInfo.getTotalWords());
        vo.setTotalChapters(bookInfo.getTotalChapters());
        vo.setScore(bookInfo.getScore());
        vo.setLastChapterId(item.getBookshelf().getLastChapterId());
        vo.setLastChapterTitle("Chapter " + (item.getBookshelf().getLastChapterId() != null ? item.getBookshelf().getLastChapterId() : ""));
        vo.setLastReadTime(item.getBookshelf().getLastReadTime());
        vo.setLatestChapterId(bookInfo.getLastChapterId());
        return vo;
    }
}
