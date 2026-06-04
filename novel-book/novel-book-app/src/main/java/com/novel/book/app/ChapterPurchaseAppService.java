package com.novel.book.app;

import cn.dev33.satoken.stp.StpUtil;
import com.novel.book.domain.entity.ChapterPurchase;
import com.novel.book.domain.repository.ChapterPurchaseRepository;
import com.novel.book.dto.PurchaseChapterDto;
import com.novel.book.dto.PurchaseChapterVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

/**
 * Chapter purchase application service
 */
@Service
@RequiredArgsConstructor
public class ChapterPurchaseAppService {

    private final ChapterPurchaseRepository chapterPurchaseRepository;
    // TODO: inject UserWalletAppService when cross-module communication is set up

    @Transactional
    public Mono<PurchaseChapterVo> purchaseChapter(PurchaseChapterDto params) {
        return Mono.fromCallable(() -> {
            Long userId = StpUtil.getLoginIdAsLong();

            // Check if already purchased
            ChapterPurchase existing = chapterPurchaseRepository.findByUserIdAndChapterId(
                    userId, params.getChapterId());

            if (existing != null) {
                PurchaseChapterVo vo = new PurchaseChapterVo();
                vo.setSuccess(true);
                vo.setCostCoins(0);
                vo.setMessage("Chapter already purchased");
                return vo;
            }

            // TODO: Get chapter price from chapter repository
            int chapterPrice = 10; // Default price

            // TODO: Deduct coins from user wallet via cross-module call
            // For now, just save the purchase record
            ChapterPurchase purchase = new ChapterPurchase();
            purchase.setUserId(userId);
            purchase.setBookId(params.getBookId());
            purchase.setChapterId(params.getChapterId());
            purchase.setCostCoins(chapterPrice);
            chapterPurchaseRepository.save(purchase);

            PurchaseChapterVo vo = new PurchaseChapterVo();
            vo.setSuccess(true);
            vo.setCostCoins(chapterPrice);
            vo.setRemainingCoins(0L); // TODO: get from wallet
            vo.setMessage("Chapter purchased successfully");
            return vo;
        });
    }

    public Mono<Boolean> hasChapterAccess(Long userId, Long chapterId) {
        return Mono.fromCallable(() -> {
            ChapterPurchase purchase = chapterPurchaseRepository.findByUserIdAndChapterId(
                    userId, chapterId);
            return purchase != null;
        });
    }
}
