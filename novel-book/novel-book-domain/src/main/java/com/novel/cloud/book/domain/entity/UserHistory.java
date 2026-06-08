package com.novel.cloud.book.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户阅读流水 - 领域实体（对齐 t_user_history 表）。
 * 与 ReadingHistory 的区别：
 * ReadingHistory（t_reading_history）：一书一行，仅维护最新进度；
 * UserHistory（t_user_history）：流水表，每次阅读章节新增一行，用于行为分析、最近N次阅读列表等场景。
 */
@Data
public class UserHistory {

    private Long id;
    private Long userId;
    private Long bookId;
    private Long chapterId;
    private Integer progress;
    private LocalDateTime createTime;

    /**
     * 获取进度值
     */
    public Integer getProgressValue() {
        return progress != null ? progress : 0;
    }

    /**
     * 判断是否继续上次阅读（进度>0）
     */
    public boolean isContinuingPrevious() {
        return progress != null && progress > 0;
    }

    /**
     * 判断是否是首次开始阅读
     */
    public boolean isFirstTime() {
        return progress == null || progress == 0;
    }
}
