package com.novel.user.adapter.web;

import com.novel.common.core.domain.R;
import com.novel.user.app.FeedbackAppService;
import com.novel.user.app.UserAppService;
import com.novel.user.app.WalletAppService;
import com.novel.user.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserAppService userAppService;
    private final WalletAppService walletAppService;
    private final FeedbackAppService feedbackAppService;

    @PostMapping("/login")
    public Mono<R<UserLoginVo>> login(@Valid @RequestBody UserLoginDto params) {
        return userAppService.login(params).map(R::ok);
    }

    @GetMapping("/profile")
    public Mono<R<UserProfileVo>> getProfile() {
        return walletAppService.getUserProfile().map(R::ok);
    }

    @GetMapping("/wallet")
    public Mono<R<WalletInfoVo>> getWallet() {
        return walletAppService.getWalletInfo().map(R::ok);
    }

    @PostMapping("/coins/records")
    public Mono<R<CoinRecordsVo>> getCoinRecords(@Valid @RequestBody CoinRecordsQueryDto params) {
        return walletAppService.getCoinRecords(params).map(R::ok);
    }

    @GetMapping("/checkin/status")
    public Mono<R<CheckinStatusVo>> getCheckinStatus() {
        return walletAppService.getCheckinStatus().map(R::ok);
    }

    @PostMapping("/checkin")
    public Mono<R<CheckinVo>> checkin() {
        return walletAppService.performCheckin().map(R::ok);
    }

    @GetMapping("/tasks/daily")
    public Mono<R<DailyTasksVo>> getDailyTasks() {
        return walletAppService.getDailyTasks().map(R::ok);
    }

    @PostMapping("/tasks/claim")
    public Mono<R<ClaimTaskRewardVo>> claimTaskReward(@Valid @RequestBody ClaimTaskRewardDto params) {
        return walletAppService.claimTaskReward(params).map(R::ok);
    }

    @PostMapping("/feedback")
    public Mono<R<SubmitFeedbackVo>> submitFeedback(@Valid @RequestBody SubmitFeedbackDto params) {
        return feedbackAppService.submitFeedback(params).map(R::ok);
    }
}
