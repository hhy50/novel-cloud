package com.novel.cloud.activity.adapter.web;

import com.novel.cloud.activity.app.CheckinAdminAppService;
import com.novel.cloud.activity.dto.AdminUpdateCheckinConfigDto;
import com.novel.cloud.activity.dto.CheckinConfigVo;
import com.novel.cloud.common.domain.R;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 签到配置管理（admin）控制器
 * <p>路径已在 SaTokenConfigure 中放行，不走 C 端登录态。
 * 后续 admin 鉴权由 admin 网关 / 单独 filter 接管。</p>
 */
@Validated
@RestController
@RequestMapping("/api/activity/admin/checkin")
@RequiredArgsConstructor
public class CheckinAdminController {

    private final CheckinAdminAppService checkinAdminAppService;

    /**
     * 获取当前生效的签到配置
     */
    @GetMapping("/config")
    public R<CheckinConfigVo> getActiveConfig() {
        return R.ok(checkinAdminAppService.getActiveConfig());
    }

    /**
     * 新建 / 更新签到配置（id 为空表示新建）
     */
    @PostMapping("/config")
    public R<CheckinConfigVo> saveConfig(@Valid @RequestBody AdminUpdateCheckinConfigDto dto) {
        return R.ok(checkinAdminAppService.saveConfig(dto));
    }
}
