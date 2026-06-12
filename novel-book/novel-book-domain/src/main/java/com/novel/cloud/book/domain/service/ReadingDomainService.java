package com.novel.cloud.book.domain.service;

import com.novel.cloud.book.domain.entity.UserBookshelf;
import com.novel.cloud.book.domain.repository.BookChapterRepository;
import com.novel.cloud.book.domain.repository.BookInfoRepository;
import com.novel.cloud.book.domain.repository.UserBookshelfRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReadingDomainService {

    private final BookInfoRepository bookInfoRepository;
    private final BookChapterRepository bookChapterRepository;
    private final UserBookshelfRepository userBookshelfRepository;

    /**
     * 开始阅读
     * 1. 确定要阅读的章节（优先使用传入的 chapterId，其次使用历史记录，最后使用第一章）
     * 2. 将书籍添加到书架
     * 3. 更新用户阅读历史
     * 4. 返回最后阅读的章节ID
     */
    public Long startReading(Long userId, Long bookId, Long chapterId) {
        // 添加到书架
        UserBookshelf userBookshelf = userBookshelfRepository.addOrUpdate(userId, bookId);
        return userBookshelf.getLastChapterId();
    }

    /**
     * 记录阅读进度
     */
    public void recordReadingProgress(Long userId, Long bookId, Long lastReadChapterId) {
        userBookshelfRepository.updateLastRead(userId, bookId, lastReadChapterId);
    }
}
