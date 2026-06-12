package com.novel.cloud.book.adapter.web;

import com.novel.cloud.book.app.ReadingAppService;
import com.novel.cloud.book.dto.request.RecordReadingReq;
import com.novel.cloud.book.dto.request.StartReadingReq;
import com.novel.cloud.book.dto.response.StartReadingResp;
import com.novel.cloud.common.domain.R;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
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

    @PostMapping
    public R<StartReadingResp> startReading(@Valid @RequestBody StartReadingReq params) {
        return R.ok(readingAppService.startReading(params));
    }

    @PostMapping("/record")
    public R<Boolean> record(@Valid @RequestBody RecordReadingReq params) {
        return R.ok(readingAppService.record(params));
    }
}