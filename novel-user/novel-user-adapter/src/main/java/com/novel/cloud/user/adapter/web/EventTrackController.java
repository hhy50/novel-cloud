package com.novel.cloud.user.adapter.web;

import cn.dev33.satoken.stp.StpUtil;
import com.novel.cloud.common.domain.R;
import com.novel.cloud.user.app.EventTrackAppService;
import com.novel.cloud.user.dto.EventReportDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class EventTrackController {

    private final EventTrackAppService eventTrackAppService;

    /**
     * 上报用户事件
     *
     * @param deviceId 设备ID（从Header获取，优先级高于body中的deviceId）
     * @param params 事件报告参数
     * @return 成功响应
     */
    @PostMapping("/event/report")
    public R<Void> reportEvent(
            @RequestHeader(value = "deviceId", required = false) String deviceId,
            @Valid @RequestBody EventReportDto params) {
        Long userId = null;
        try {
            userId = StpUtil.getLoginIdAsLong();
        } catch (Exception ignored) {
            // 未登录用户也允许上报事件
        }

        // 优先使用Header中的deviceId，如果Header没有则使用body中的deviceId
        String finalDeviceId = StringUtils.hasText(deviceId) ? deviceId : params.getDeviceId();

        eventTrackAppService.reportEvent(userId, finalDeviceId, params.getAppVersion(), params);
        return R.ok();
    }
}
