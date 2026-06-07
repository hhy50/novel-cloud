package com.novel.cloud.book.adapter.web;

import com.novel.cloud.book.app.ChapterPurchaseAppService;
import com.novel.cloud.book.dto.PurchaseChapterDto;
import com.novel.cloud.book.dto.PurchaseChapterVo;
import com.novel.cloud.common.domain.R;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 章节购买 Controller。
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/book/chapter")
public class ChapterPurchaseController {

    private final ChapterPurchaseAppService chapterPurchaseAppService;

    @PostMapping("/purchase")
    public R<PurchaseChapterVo> purchaseChapter(@Valid @RequestBody PurchaseChapterDto params) {
        return R.ok(chapterPurchaseAppService.purchaseChapter(params));
    }
}