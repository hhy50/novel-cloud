package com.novel.cloud.book.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 书籍分类 - 领域实体
 */
@Data
public class Category {

    private Long id;
    private String name;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private LocalDateTime deleteTime;

    /**
     * 判断是否有效
     */
    public boolean isValid() {
        return deleteTime == null;
    }

    /**
     * 判断是否有名称
     */
    public boolean hasName() {
        return name != null && !name.isBlank();
    }
}
