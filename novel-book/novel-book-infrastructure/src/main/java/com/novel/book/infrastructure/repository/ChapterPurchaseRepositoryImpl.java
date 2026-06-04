package com.novel.book.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.novel.book.domain.entity.ChapterPurchase;
import com.novel.book.domain.repository.ChapterPurchaseRepository;
import com.novel.book.infrastructure.dataobject.ChapterPurchaseDO;
import com.novel.book.infrastructure.mapper.ChapterPurchaseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChapterPurchaseRepositoryImpl implements ChapterPurchaseRepository {

    private final ChapterPurchaseMapper chapterPurchaseMapper;

    @Override
    public ChapterPurchase findByUserIdAndChapterId(Long userId, Long chapterId) {
        ChapterPurchaseDO purchaseDO = chapterPurchaseMapper.selectOne(
                new LambdaQueryWrapper<ChapterPurchaseDO>()
                        .eq(ChapterPurchaseDO::getUserId, userId)
                        .eq(ChapterPurchaseDO::getChapterId, chapterId)
                        .last("limit 1")
        );
        if (purchaseDO == null) {
            return null;
        }
        ChapterPurchase purchase = new ChapterPurchase();
        BeanUtils.copyProperties(purchaseDO, purchase);
        return purchase;
    }

    @Override
    public void save(ChapterPurchase purchase) {
        ChapterPurchaseDO purchaseDO = new ChapterPurchaseDO();
        BeanUtils.copyProperties(purchase, purchaseDO);
        chapterPurchaseMapper.insert(purchaseDO);
    }
}
