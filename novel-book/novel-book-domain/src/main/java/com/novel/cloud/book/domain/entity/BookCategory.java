package com.novel.cloud.book.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 书籍-分类关联 - 领域实体
 */
@Data
public class BookCategory {

    private Long id;
    private Long categoryId;
    private Long bookId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private LocalDateTime deleteTime;

    /**
     * 判断是否有效
     */
    public boolean isValid() {
        return deleteTime == null;
    }
}
