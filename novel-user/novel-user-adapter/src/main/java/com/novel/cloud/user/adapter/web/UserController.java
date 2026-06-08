package com.novel.cloud.user.adapter.web;

import com.novel.cloud.common.domain.R;
import com.novel.cloud.user.app.FeedbackAppService;
import com.novel.cloud.user.app.UserAppService;
import com.novel.cloud.user.app.WalletAppService;
import com.novel.cloud.user.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户/钱包/任务 控制器。
 *
 * <p>签到接口已迁移至 {@code novel-activity} 模块：
 * {@code GET/POST /api/activity/checkin/{status,perform}}，本控制器不再提供签到端点。</p>
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserAppService userAppService;
    private final WalletAppService walletAppService;
    private final FeedbackAppService feedbackAppService;

    @PostMapping("/login")
    public R<UserLoginVo> login(@Valid @RequestBody UserLoginDto params) {
        return R.ok(userAppService.login(params));
    }

    @GetMapping("/profile")
    public R<UserProfileVo> getProfile() {
        return R.ok(walletAppService.getUserProfile());
    }

    @GetMapping("/wallet")
    public R<WalletInfoVo> getWallet() {
        return R.ok(walletAppService.getWalletInfo());
    }

    @PostMapping("/coins/records")
    public R<CoinRecordsVo> getCoinRecords(@Valid @RequestBody CoinRecordsQueryDto params) {
        return R.ok(walletAppService.getCoinRecords(params));
    }

    @GetMapping("/tasks/daily")
    public R<DailyTasksVo> getDailyTasks() {
        return R.ok(walletAppService.getDailyTasks());
    }

    @PostMapping("/tasks/claim")
    public R<ClaimTaskRewardVo> claimTaskReward(@Valid @RequestBody ClaimTaskRewardDto params) {
        return R.ok(walletAppService.claimTaskReward(params));
    }

    @PostMapping("/feedback")
    public R<SubmitFeedbackVo> submitFeedback(@Valid @RequestBody SubmitFeedbackDto params) {
        return R.ok(feedbackAppService.submitFeedback(params));
    }
}
