package com.novel.cloud.activity.adapter.web;

import com.novel.cloud.activity.app.CheckinAppService;
import com.novel.cloud.activity.dto.CheckinResultVo;
import com.novel.cloud.activity.dto.CheckinStatusVo;
import com.novel.cloud.common.domain.R;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 签到控制器（C 端）
 * <p>业务异常统一抛 {@link com.novel.cloud.common.exception.BusinessException}，
 * 由全局异常处理器映射为 {@code R.fail(code, message)}。</p>
 */
@RestController
@RequestMapping("/api/activity/checkin")
@RequiredArgsConstructor
public class CheckinController {

    private final CheckinAppService checkinAppService;

    /**
     * 获取签到状态
     */
    @GetMapping("/status")
    public R<CheckinStatusVo> getCheckinStatus() {
        return R.ok(checkinAppService.getCheckinStatus());
    }

    /**
     * 执行签到
     */
    @PostMapping("/perform")
    public R<CheckinResultVo> performCheckin() {
        return R.ok(checkinAppService.performCheckin());
    }
}
