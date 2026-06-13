package com.novel.cloud.book.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户书架 - 领域实体
 */
@Data
public class UserBookshelf {

    private Long id;
    private Long userId;
    private Long bookId;
    private Long lastChapterId;
    private Integer showInShelf;
    private LocalDateTime lastReadTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    /**
     * 更新最后阅读章节
     */
    public void updateLastRead(Long chapterId) {
        this.lastChapterId = chapterId;
        this.lastReadTime = LocalDateTime.now();
    }

    /**
     * 更新最后阅读时间
     */
    public void updateLastReadTime() {
        this.lastReadTime = LocalDateTime.now();
    }

    /**
     * 判断是否最近阅读过（7天内）
     */
    public boolean isRecentlyRead() {
        if (lastReadTime == null) {
            return false;
        }
        return lastReadTime.isAfter(LocalDateTime.now().minusDays(7));
    }

    /**
     * 判断是否有阅读记录
     */
    public boolean hasReadHistory() {
        return lastChapterId != null && lastReadTime != null;
    }
}
