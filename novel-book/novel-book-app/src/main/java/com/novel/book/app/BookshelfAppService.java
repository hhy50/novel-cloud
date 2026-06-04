package com.novel.book.app;

import cn.dev33.satoken.stp.StpUtil;
import com.novel.book.domain.entity.BookInfo;
import com.novel.book.domain.entity.UserBookshelf;
import com.novel.book.domain.repository.BookInfoRepository;
import com.novel.book.domain.repository.UserBookshelfRepository;
import com.novel.book.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

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

    public Mono<BookshelfVo> getBookshelf() {
        return Mono.fromCallable(() -> {
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
                        item.setBookName(bookInfo.getBookName());
                        item.setAuthorName(bookInfo.getAuthorName());
                        item.setCoverUrl(bookInfo.getCoverUrl());
                        item.setLastChapterId(bookshelf.getLastChapterId());
                        // TODO: get chapter title from chapter repository
                        item.setLastChapterTitle("Chapter " + (bookshelf.getLastChapterId() != null ? bookshelf.getLastChapterId() : ""));
                        item.setLastReadTime(bookshelf.getLastReadTime());
                        return item;
                    })
                    .filter(item -> item != null)
                    .collect(Collectors.toList());

            BookshelfVo result = new BookshelfVo();
            result.setBooks(items);
            result.setTotal(items.size());
            return result;
        });
    }

    @Transactional
    public Mono<Boolean> addToBookshelf(AddBookshelfDto params) {
        return Mono.fromCallable(() -> {
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
        });
    }

    @Transactional
    public Mono<Boolean> removeFromBookshelf(RemoveBookshelfDto params) {
        return Mono.fromCallable(() -> {
            Long userId = StpUtil.getLoginIdAsLong();
            userBookshelfRepository.deleteByUserIdAndBookId(userId, params.getBookId());
            return true;
        });
    }
}
