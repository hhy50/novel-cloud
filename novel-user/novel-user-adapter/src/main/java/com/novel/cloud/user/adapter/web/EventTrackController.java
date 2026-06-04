package com.novel.cloud.user.adapter.web;

import cn.dev33.satoken.stp.StpUtil;
import com.novel.cloud.common.domain.R;
import com.novel.cloud.user.app.EventTrackAppService;
import com.novel.cloud.user.dto.EventReportDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class EventTrackController {

    private final EventTrackAppService eventTrackAppService;

    @PostMapping("/event/report")
    public Mono<R<Void>> reportEvent(@Valid @RequestBody EventReportDto params) {
        Long userId = null;
        try {
            userId = StpUtil.getLoginIdAsLong();
        } catch (Exception ignored) {
            // 未登录用户也允许上报事件
        }
        return eventTrackAppService.reportEvent(userId, params.getDeviceId(), params.getAppVersion(), params)
                .thenReturn(R.ok());
    }
}
