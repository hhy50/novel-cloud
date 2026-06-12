package com.novel.cloud.book.domain.repository;

import com.novel.cloud.book.domain.entity.UserBookshelf;
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

    /**
     * 添加到书架；若该用户该书已存在则刷新 last_chapter_id / last_read_time
     * （隐式加入书架场景）。
     */
    UserBookshelf addOrUpdate(Long userId, Long bookId);
}
