package com.novel.cloud.book.domain.service;

import com.novel.cloud.book.domain.entity.BookChapter;
import com.novel.cloud.book.domain.entity.ChapterPurchase;
import com.novel.cloud.book.domain.repository.BookChapterRepository;
import com.novel.cloud.book.domain.repository.ChapterPurchaseRepository;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChapterPurchaseDomainService {

    private final ChapterPurchaseRepository chapterPurchaseRepository;
    private final BookChapterRepository bookChapterRepository;

    @Data
    @Builder
    public static class PurchaseResult {
        private boolean success;
        private boolean alreadyPurchased;
        private Integer costCoins;
        private String message;
    }

    public PurchaseResult purchaseChapter(Long userId, Long bookId, Long chapterId) {
        ChapterPurchase existing = chapterPurchaseRepository.findByUserIdAndChapterId(userId, chapterId);
        if (existing != null) {
            return PurchaseResult.builder()
                    .success(true)
                    .alreadyPurchased(true)
                    .costCoins(0)
                    .message("Chapter already purchased")
                    .build();
        }

        int chapterPrice = getChapterPrice(chapterId);

        ChapterPurchase purchase = new ChapterPurchase();
        purchase.setUserId(userId);
        purchase.setBookId(bookId);
        purchase.setChapterId(chapterId);
        purchase.setCostCoins(chapterPrice);
        chapterPurchaseRepository.save(purchase);

        return PurchaseResult.builder()
                .success(true)
                .alreadyPurchased(false)
                .costCoins(chapterPrice)
                .message("Chapter purchased successfully")
                .build();
    }

    public boolean hasChapterAccess(Long userId, Long chapterId) {
        ChapterPurchase purchase = chapterPurchaseRepository.findByUserIdAndChapterId(userId, chapterId);
        return purchase != null;
    }

    private int getChapterPrice(Long chapterId) {
        BookChapter chapter = bookChapterRepository.findById(chapterId);
        if (chapter != null) {
            return chapter.getPriceValue();
        }
        return 10;
    }
}
