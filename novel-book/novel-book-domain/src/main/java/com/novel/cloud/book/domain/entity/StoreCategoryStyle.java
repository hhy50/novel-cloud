package com.novel.cloud.book.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 书城分类样式
 */
@Data
public class StoreCategoryStyle {

    private Long id;
    private Long pid;
    private String appCode;
    private String language;
    private String name;
    private Integer styleCode;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private LocalDateTime deleteTime;
}
