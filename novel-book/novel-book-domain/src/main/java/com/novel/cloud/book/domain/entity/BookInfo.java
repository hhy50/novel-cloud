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

    /**
     * 判断书籍是否已完结
     */
    public boolean isFinished() {
        return status != null && status == 2;
    }

    /**
     * 判断是否限时免费
     */
    public boolean isLimitedFree() {
        return isLimitedFree != null && isLimitedFree == 1;
    }

    /**
     * 判断是否是包月书籍
     */
    public boolean isBaoyueBook() {
        return isBaoyue != null && isBaoyue == 1;
    }

    /**
     * 判断某章节是否是VIP章节
     */
    public boolean isVipChapter(Integer chapterNumber) {
        if (chapterNumber == null || freeChapters == null) {
            return false;
        }
        return chapterNumber > freeChapters;
    }

    /**
     * 计算某章节的价格
     */
    public Integer calculateChapterPrice(Integer chapterNumber) {
        if (!isVipChapter(chapterNumber)) {
            return 0;
        }
        return chapterPrice != null ? chapterPrice : 0;
    }

    /**
     * 获取评分（0-5分）
     */
    public Double getRating() {
        if (score == null) {
            return 0.0;
        }
        return score / 2.0;
    }

    /**
     * 判断是否有封面
     */
    public boolean hasCover() {
        return cover != null && !cover.isBlank();
    }

    /**
     * 判断书籍是否有效（未删除）
     */
    public boolean isValid() {
        return deletedAt == null || deletedAt == 0;
    }

    /**
     * 判断是否有章节
     */
    public boolean hasChapters() {
        return totalChapters != null && totalChapters > 0;
    }
}
