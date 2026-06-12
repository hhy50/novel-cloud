package com.novel.cloud.book.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户书籍阅读记录 - 领域实体（对齐 t_user_history 表）。
 * <p>用户 + 书级别，记录每本书的最后阅读位置，唯一约束：(user_id, book_id)</p>
 */
@Data
public class UserHistory {

    private Long id;
    private Long userId;
    private Long bookId;
    private Long lastReadChapterId;
    private LocalDateTime lastReadTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
