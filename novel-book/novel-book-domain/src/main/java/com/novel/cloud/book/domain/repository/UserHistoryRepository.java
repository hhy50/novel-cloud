package com.novel.cloud.book.domain.repository;

import com.novel.cloud.book.domain.entity.UserHistory;

/**
 * 用户阅读流水仓储。每次读取章节都新增一行，不做 upsert。
 */
public interface UserHistoryRepository {

    /** 追加一条流水记录 */
    void save(UserHistory history);

    /**
     * 查询用户对某书最近一次的流水记录（按 create_time DESC limit 1），
     * 用于 "继续上次阅读" 场景；无记录返回 null。
     */
    UserHistory findLatestByUserIdAndBookId(Long userId, Long bookId);
}
