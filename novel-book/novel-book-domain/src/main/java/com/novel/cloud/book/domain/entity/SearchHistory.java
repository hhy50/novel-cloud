package com.novel.cloud.book.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 搜索历史 - 领域实体
 */
@Data
public class SearchHistory {

    private Long id;
    private Long userId;
    private String keyword;
    private Integer searchCount;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    /**
     * 增加搜索次数
     */
    public void incrementSearchCount() {
        if (searchCount == null) {
            searchCount = 0;
        }
        searchCount++;
    }

    /**
     * 获取搜索次数（默认0）
     */
    public Integer getSearchCountOrDefault() {
        return searchCount != null ? searchCount : 0;
    }
}
