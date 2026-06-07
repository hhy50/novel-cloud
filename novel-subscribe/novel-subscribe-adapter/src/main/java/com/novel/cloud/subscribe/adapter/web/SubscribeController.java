package com.novel.cloud.subscribe.adapter.web;

import com.novel.cloud.common.domain.R;
import com.novel.cloud.subscribe.app.SubscribeAppService;
import com.novel.cloud.subscribe.domain.entity.SubscribePlan;
import com.novel.cloud.subscribe.dto.SubscribeCreateDto;
import com.novel.cloud.subscribe.dto.UserSubscribeVo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 订阅 Controller（App 对外接口）
 * Adapter层：只做参数接收、入参校验、结果组装，无业务逻辑
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/subscribe")
public class SubscribeController {

    private final SubscribeAppService subscribeAppService;

    @GetMapping("/plans")
    public R<List<SubscribePlan>> listPlans() {
        return R.ok(subscribeAppService.listPlans());
    }

    @PostMapping("/create")
    public R<UserSubscribeVo> createSubscribe(@Valid @RequestBody SubscribeCreateDto dto) {
        return R.ok(subscribeAppService.createSubscribe(dto));
    }

    @GetMapping("/active")
    public R<UserSubscribeVo> getActiveSubscribe(@NotNull @RequestParam Long userId) {
        return R.ok(subscribeAppService.getUserActiveSubscribe(userId));
    }

    @GetMapping("/history")
    public R<List<UserSubscribeVo>> getSubscribeHistory(@NotNull @RequestParam Long userId) {
        return R.ok(subscribeAppService.getUserSubscribeHistory(userId));
    }
}
