package com.novel.cloud.book.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 书籍分类
 */
@Data
public class Category {

    private Long id;
    private String name;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private LocalDateTime deleteTime;
}
