package com.novel.cloud.book.domain.service;

import com.novel.cloud.book.domain.entity.BookInfo;
import com.novel.cloud.book.domain.entity.UserBookshelf;
import com.novel.cloud.book.domain.repository.BookInfoRepository;
import com.novel.cloud.book.domain.repository.UserBookshelfRepository;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookshelfDomainService {

    private final UserBookshelfRepository userBookshelfRepository;

    private final BookInfoRepository bookInfoRepository;

    public boolean inShelf(Long userId, Long bookId) {
        return userBookshelfRepository.findByUserIdAndBookId(userId, bookId) != null;
    }

    @Data
    @Builder
    public static class BookshelfItemResult {
        private UserBookshelf bookshelf;
        private BookInfo bookInfo;
    }

    public List<BookshelfItemResult> getBookshelf(Long userId) {
        List<UserBookshelf> bookshelfList = userBookshelfRepository.findByUserId(userId);

        return bookshelfList.stream()
                .map(bookshelf -> {
                    BookInfo bookInfo = bookInfoRepository.findById(bookshelf.getBookId());
                    if (bookInfo == null) {
                        return null;
                    }
                    return BookshelfItemResult.builder()
                            .bookshelf(bookshelf)
                            .bookInfo(bookInfo)
                            .build();
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public boolean addToBookshelf(Long userId, Long bookId) {
        UserBookshelf existing = userBookshelfRepository.findByUserIdAndBookId(userId, bookId);
        if (existing != null) {
            return true;
        }

        BookInfo bookInfo = bookInfoRepository.findById(bookId);
        if (bookInfo == null) {
            return false;
        }

        UserBookshelf bookshelf = new UserBookshelf();
        bookshelf.setUserId(userId);
        bookshelf.setBookId(bookId);
        userBookshelfRepository.save(bookshelf);
        return true;
    }

    public void removeFromBookshelf(Long userId, Long bookId) {
        userBookshelfRepository.deleteByUserIdAndBookId(userId, bookId);
    }
}
