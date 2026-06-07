package com.novel.cloud.book.app;

import cn.dev33.satoken.stp.StpUtil;
import com.novel.cloud.book.domain.entity.BookInfo;
import com.novel.cloud.book.domain.entity.UserBookshelf;
import com.novel.cloud.book.domain.repository.BookInfoRepository;
import com.novel.cloud.book.domain.repository.UserBookshelfRepository;
import com.novel.cloud.book.dto.AddBookshelfDto;
import com.novel.cloud.book.dto.BookshelfItemVo;
import com.novel.cloud.book.dto.BookshelfVo;
import com.novel.cloud.book.dto.RemoveBookshelfDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Bookshelf application service
 */
@Service
@RequiredArgsConstructor
public class BookshelfAppService {

    private final UserBookshelfRepository userBookshelfRepository;
    private final BookInfoRepository bookInfoRepository;

    public BookshelfVo getBookshelf() {
        Long userId = StpUtil.getLoginIdAsLong();
        List<UserBookshelf> bookshelfList = userBookshelfRepository.findByUserId(userId);

        List<BookshelfItemVo> items = bookshelfList.stream()
                .map(bookshelf -> {
                    BookInfo bookInfo = bookInfoRepository.findById(bookshelf.getBookId());
                    if (bookInfo == null) {
                        return null;
                    }
                    BookshelfItemVo item = new BookshelfItemVo();
                    item.setBookId(bookInfo.getId());
                    item.setBookName(bookInfo.getName());
                    item.setAuthorName(bookInfo.getAuthor());
                    item.setCoverUrl(bookInfo.getCover());
                    item.setDescription(bookInfo.getDescription());
                    item.setFinished(bookInfo.getStatus() != null && bookInfo.getStatus() == 2);
                    item.setStatus(bookInfo.getStatus());
                    item.setTotalWords(bookInfo.getTotalWords());
                    item.setTotalChapters(bookInfo.getTotalChapters());
                    item.setScore(bookInfo.getScore());
                    item.setLastChapterId(bookshelf.getLastChapterId());
                    // TODO: get chapter title from chapter repository
                    item.setLastChapterTitle("Chapter " + (bookshelf.getLastChapterId() != null ? bookshelf.getLastChapterId() : ""));
                    item.setLastReadTime(bookshelf.getLastReadTime());
                    item.setLatestChapterId(bookInfo.getLastChapterId());
                    return item;
                })
                .filter(item -> item != null)
                .collect(Collectors.toList());

        BookshelfVo result = new BookshelfVo();
        result.setBooks(items);
        result.setTotal(items.size());
        return result;
    }

    @Transactional
    public Boolean addToBookshelf(AddBookshelfDto params) {
        Long userId = StpUtil.getLoginIdAsLong();
        // Check if already exists
        UserBookshelf existing = userBookshelfRepository.findByUserIdAndBookId(userId, params.getBookId());
        if (existing != null) {
            return true;
        }

        // Verify book exists
        BookInfo bookInfo = bookInfoRepository.findById(params.getBookId());
        if (bookInfo == null) {
            throw new RuntimeException("Book not found");
        }

        UserBookshelf bookshelf = new UserBookshelf();
        bookshelf.setUserId(userId);
        bookshelf.setBookId(params.getBookId());
        userBookshelfRepository.save(bookshelf);

        return true;
    }

    @Transactional
    public Boolean removeFromBookshelf(RemoveBookshelfDto params) {
        Long userId = StpUtil.getLoginIdAsLong();
        userBookshelfRepository.deleteByUserIdAndBookId(userId, params.getBookId());
        return true;
    }
}
