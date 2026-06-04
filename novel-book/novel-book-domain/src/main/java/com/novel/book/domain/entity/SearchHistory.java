package com.novel.book.domain.entity;

import java.time.LocalDateTime;
import lombok.Data;

/**
 * Search history domain entity
 */
@Data
public class SearchHistory {

    private Long id;
    private Long userId;
    private String keyword;
    private Integer searchCount;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
