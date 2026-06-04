package com.novel.book.domain.repository;

import com.novel.book.domain.entity.ChapterPurchase;

/**
 * Chapter purchase repository interface
 */
public interface ChapterPurchaseRepository {

    ChapterPurchase findByUserIdAndChapterId(Long userId, Long chapterId);

    void save(ChapterPurchase purchase);
}
