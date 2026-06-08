package com.novel.cloud.book.app;

import cn.dev33.satoken.stp.StpUtil;
import com.novel.cloud.book.domain.service.ChapterPurchaseDomainService;
import com.novel.cloud.book.dto.request.PurchaseChapterReq;
import com.novel.cloud.book.dto.response.PurchaseChapterResp;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChapterPurchaseAppService {

    private final ChapterPurchaseDomainService chapterPurchaseDomainService;

    @Transactional
    public PurchaseChapterResp purchaseChapter(PurchaseChapterReq params) {
        Long userId = StpUtil.getLoginIdAsLong();
        ChapterPurchaseDomainService.PurchaseResult result = chapterPurchaseDomainService.purchaseChapter(
                userId, params.getBookId(), params.getChapterId());

        PurchaseChapterResp vo = new PurchaseChapterResp();
        vo.setSuccess(result.isSuccess());
        vo.setCostCoins(result.getCostCoins());
        vo.setMessage(result.getMessage());
        vo.setRemainingCoins(0L);
        return vo;
    }

    public Boolean hasChapterAccess(Long userId, Long chapterId) {
        return chapterPurchaseDomainService.hasChapterAccess(userId, chapterId);
    }
}
