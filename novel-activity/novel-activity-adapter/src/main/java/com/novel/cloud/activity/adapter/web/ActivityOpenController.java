package com.novel.cloud.activity.adapter.web;

import com.novel.cloud.activity.app.CheckinAppService;
import com.novel.cloud.activity.dto.CheckinResultVo;
import com.novel.cloud.activity.dto.CheckinStatusVo;
import com.novel.cloud.common.domain.R;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 活动服务Open接口（服务间调用）
 */
@RestController
@RequestMapping("/api/open/activity")
@RequiredArgsConstructor
public class ActivityOpenController {

    private final CheckinAppService checkinAppService;

    /**
     * 获取签到状态
     */
    @GetMapping("/checkin/status")
    public R<CheckinStatusVo> getCheckinStatus() {
        try {
            return R.ok(checkinAppService.getCheckinStatus());
        } catch (Exception e) {
            return R.fail(e.getMessage());
        }
    }

    /**
     * 执行签到
     */
    @PostMapping("/checkin/perform")
    public R<CheckinResultVo> performCheckin() {
        try {
            return R.ok(checkinAppService.performCheckin());
        } catch (Exception e) {
            return R.fail(e.getMessage());
        }
    }
}
