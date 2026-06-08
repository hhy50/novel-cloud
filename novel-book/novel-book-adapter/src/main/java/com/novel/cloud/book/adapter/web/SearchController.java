package com.novel.cloud.book.adapter.web;

import com.novel.cloud.book.app.SearchAppService;
import com.novel.cloud.book.dto.request.SearchBooksReq;
import com.novel.cloud.book.dto.response.HotSearchResp;
import com.novel.cloud.book.dto.response.RecommendBooksResp;
import com.novel.cloud.book.dto.response.SearchBooksResp;
import com.novel.cloud.book.dto.response.SearchHistoryResp;
import com.novel.cloud.common.domain.R;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 搜索 Controller —— 搜书、热搜、搜索历史、推荐位。
 * <p>"推荐" 历史上挂在搜索 AppService 下，故一并归入搜索 Controller，保持职责一致。</p>
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/book")
public class SearchController {

    private final SearchAppService searchAppService;

    @PostMapping("/search")
    public R<SearchBooksResp> searchBooks(@Valid @RequestBody SearchBooksReq params) {
        return R.ok(searchAppService.searchBooks(params));
    }

    @GetMapping("/search/history")
    public R<SearchHistoryResp> getSearchHistory() {
        return R.ok(searchAppService.getSearchHistory());
    }

    @PostMapping("/search/history/clear")
    public R<Boolean> clearSearchHistory() {
        return R.ok(searchAppService.clearSearchHistory());
    }

    @GetMapping("/search/hot")
    public R<HotSearchResp> getHotSearch(@RequestParam(required = false) Integer limit) {
        return R.ok(searchAppService.getHotSearch(limit));
    }

    @GetMapping("/recommendations")
    public R<RecommendBooksResp> getRecommendations() {
        return R.ok(searchAppService.getRecommendations());
    }
}