package com.novel.cloud.book.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户阅读流水 - 领域实体（对齐 t_user_history 表）。
 * <p>与 {@link ReadingHistory} 的区别：
 * <ul>
 *   <li>{@code ReadingHistory}（t_reading_history）：一书一行，仅维护最新进度；</li>
 *   <li>{@code UserHistory}（t_user_history）：流水表，每次读取章节都新增一行，
 *       用于行为分析、最近 N 次阅读列表等场景。</li>
 * </ul>
 */
@Data
public class UserHistory {

    private Long id;
    private Long userId;
    private Long bookId;
    private Long chapterId;
    /** 章节内阅读进度 0-100；start 时若是继续上次则继承，否则为 0 */
    private Integer progress;
    private LocalDateTime createTime;
}
