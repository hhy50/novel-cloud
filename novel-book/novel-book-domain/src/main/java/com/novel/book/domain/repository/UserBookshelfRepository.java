package com.novel.book.domain.repository;

import com.novel.book.domain.entity.UserBookshelf;
import java.util.List;

/**
 * User bookshelf repository interface
 */
public interface UserBookshelfRepository {

    List<UserBookshelf> findByUserId(Long userId);

    UserBookshelf findByUserIdAndBookId(Long userId, Long bookId);

    void save(UserBookshelf bookshelf);

    void deleteByUserIdAndBookId(Long userId, Long bookId);

    void updateLastRead(Long userId, Long bookId, Long chapterId);
}
