package com.novel.subscribe.adapter.web;

import com.novel.common.core.domain.R;
import com.novel.subscribe.app.SubscribeAppService;
import com.novel.subscribe.domain.entity.SubscribePlan;
import com.novel.subscribe.dto.SubscribeCreateDto;
import com.novel.subscribe.dto.UserSubscribeVo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

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
    public Mono<R<List<SubscribePlan>>> listPlans() {
        return subscribeAppService.listPlans().map(R::ok);
    }

    @PostMapping("/create")
    public Mono<R<UserSubscribeVo>> createSubscribe(@Valid @RequestBody SubscribeCreateDto dto) {
        return subscribeAppService.createSubscribe(dto).map(R::ok);
    }

    @GetMapping("/active")
    public Mono<R<UserSubscribeVo>> getActiveSubscribe(@NotNull @RequestParam Long userId) {
        return subscribeAppService.getUserActiveSubscribe(userId).map(R::ok);
    }

    @GetMapping("/history")
    public Mono<R<List<UserSubscribeVo>>> getSubscribeHistory(@NotNull @RequestParam Long userId) {
        return subscribeAppService.getUserSubscribeHistory(userId).collectList().map(R::ok);
    }
}
