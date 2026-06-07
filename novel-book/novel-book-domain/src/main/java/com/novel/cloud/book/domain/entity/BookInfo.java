package com.novel.cloud.book.domain.entity;

import lombok.Data;

/**
 * 书籍信息 - 领域实体（对齐 t_book_info DDL）
 */
@Data
public class BookInfo {

    private Long id;
    private String language;
    private String name;
    private String author;
    private String description;
    private Integer totalPrice;
    private Integer wordsPrice;
    private Integer chapterPrice;
    private Integer freeChapters;
    private String cover;
    private Integer status;
    private Integer onlineStatus;
    private Integer isBaoyue;
    private Integer isProductFreeLimit;
    private Integer isHot;
    private Integer isNew;
    private Integer isLimitedFree;
    private Integer isInkecho;
    private Integer isYy;
    private Integer isGreatest;
    private Integer isGod;
    private Integer totalWords;
    private Integer totalChapters;
    private Integer totalViews;
    private Integer totalFavors;
    private Integer score;
    private Integer lastChapterId;
    private Integer lastChapterTime;
    private Integer createdAt;
    private Integer updatedAt;
    private Integer deletedAt;
}
