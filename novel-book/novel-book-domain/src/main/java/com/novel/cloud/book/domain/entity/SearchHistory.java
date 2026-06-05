package com.novel.cloud.book.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

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
