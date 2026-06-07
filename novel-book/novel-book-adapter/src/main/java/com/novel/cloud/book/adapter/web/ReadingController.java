package com.novel.cloud.book.adapter.web;

import com.novel.cloud.book.app.ReadingAppService;
import com.novel.cloud.book.dto.ReadingHistoryQueryDto;
import com.novel.cloud.book.dto.ReadingHistoryVo;
import com.novel.cloud.book.dto.ReadingStatsVo;
import com.novel.cloud.book.dto.RecordReadingDto;
import com.novel.cloud.book.dto.StartReadingDto;
import com.novel.cloud.book.dto.StartReadingVo;
import com.novel.cloud.common.domain.R;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 阅读 Controller —— 开始阅读、上报进度、阅读历史、统计。
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/book/reading")
public class ReadingController {

    private final ReadingAppService readingAppService;

    @PostMapping("/start")
    public R<StartReadingVo> startReading(@Valid @RequestBody StartReadingDto params) {
        return R.ok(readingAppService.startReading(params));
    }

    @PostMapping("/record")
    public R<Boolean> recordReading(@Valid @RequestBody RecordReadingDto params) {
        return R.ok(readingAppService.recordReading(params));
    }

    @PostMapping("/history")
    public R<ReadingHistoryVo> getReadingHistory(@Valid @RequestBody ReadingHistoryQueryDto params) {
        return R.ok(readingAppService.getReadingHistory(params));
    }

    @GetMapping("/stats")
    public R<ReadingStatsVo> getReadingStats() {
        return R.ok(readingAppService.getReadingStats());
    }
}