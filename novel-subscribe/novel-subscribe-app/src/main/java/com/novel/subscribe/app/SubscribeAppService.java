package com.novel.subscribe.app;

import com.novel.common.core.exception.BusinessException;
import com.novel.payment.api.PaymentOpenFeignApi;
import com.novel.payment.dto.PaymentCreateDto;
import com.novel.payment.dto.PaymentResultVo;
import com.novel.subscribe.domain.entity.SubscribePlan;
import com.novel.subscribe.domain.entity.UserSubscribe;
import com.novel.subscribe.domain.repository.SubscribePlanRepository;
import com.novel.subscribe.domain.repository.UserSubscribeRepository;
import com.novel.subscribe.domain.service.SubscribeDomainService;
import com.novel.subscribe.dto.SubscribeActivateDto;
import com.novel.subscribe.dto.SubscribeCreateDto;
import com.novel.subscribe.dto.UserSubscribeVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 订阅应用服务
 * 职责：用例编排、事务控制，不写核心业务规则
 */
@Service
@RequiredArgsConstructor
public class SubscribeAppService {

    private final SubscribePlanRepository subscribePlanRepository;
    private final UserSubscribeRepository userSubscribeRepository;
    private final SubscribeDomainService subscribeDomainService;
    private final PaymentOpenFeignApi paymentOpenFeignApi;

    public Mono<List<SubscribePlan>> listPlans() {
        return Mono.fromCallable(subscribePlanRepository::listOnShelfPlans);
    }

    @Transactional(rollbackFor = Exception.class)
    public Mono<UserSubscribeVo> createSubscribe(SubscribeCreateDto dto) {
        return Mono.fromCallable(() -> subscribePlanRepository.findById(dto.getPlanId()))
                .flatMap(plan -> {
                    if (plan == null) {
                        return Mono.error(new BusinessException("订阅计划不存在"));
                    }
                    UserSubscribe subscribe = subscribeDomainService.initPendingSubscribe(dto.getUserId(), plan.getId());
                    return Mono.fromCallable(() -> userSubscribeRepository.save(subscribe))
                            .flatMap(saved -> createPaymentOrder(dto, plan, saved));
                });
    }

    @Transactional(rollbackFor = Exception.class)
    public Mono<UserSubscribeVo> activateSubscribe(SubscribeActivateDto dto) {
        return Mono.fromCallable(() -> userSubscribeRepository.findById(dto.getSubscribeId()))
                .flatMap(subscribe -> {
                    if (subscribe == null) {
                        return Mono.error(new BusinessException("订阅记录不存在"));
                    }
                    return Mono.fromCallable(() -> subscribePlanRepository.findById(subscribe.getPlanId()))
                            .flatMap(plan -> {
                                if (plan == null) {
                                    return Mono.error(new BusinessException("订阅计划不存在"));
                                }
                                UserSubscribe activated = subscribeDomainService.activate(subscribe, plan, dto.getOrderNo());
                                return Mono.fromCallable(() -> userSubscribeRepository.update(activated))
                                        .map(updated -> toSubscribeVo(updated, plan));
                            });
                });
    }

    public Mono<UserSubscribeVo> getUserActiveSubscribe(Long userId) {
        return Mono.fromCallable(() -> userSubscribeRepository.findActiveByUserId(userId))
                .flatMap(subscribe -> {
                    if (subscribe == null) {
                        return Mono.empty();
                    }
                    boolean expired = subscribeDomainService.checkAndMarkExpired(subscribe) != null && subscribe.getStatus() == 2;
                    Mono<UserSubscribe> subscribeMono = expired
                            ? Mono.fromCallable(() -> userSubscribeRepository.update(subscribe))
                            : Mono.just(subscribe);
                    return subscribeMono.flatMap(current -> Mono.fromCallable(() -> subscribePlanRepository.findById(current.getPlanId()))
                            .map(plan -> toSubscribeVo(current, plan)));
                });
    }

    public Flux<UserSubscribeVo> getUserSubscribeHistory(Long userId) {
        return Mono.fromCallable(() -> userSubscribeRepository.listByUserId(userId))
                .flatMapMany(subscribes -> {
                    List<Long> planIds = subscribes.stream()
                            .map(UserSubscribe::getPlanId)
                            .distinct()
                            .collect(Collectors.toList());
                    if (planIds.isEmpty()) {
                        return Flux.fromIterable(subscribes.stream().map(s -> toSubscribeVo(s, null)).toList());
                    }
                    return Mono.fromCallable(() -> subscribePlanRepository.findByIds(planIds))
                            .flatMapMany(plans -> {
                                var planMap = plans.stream().collect(Collectors.toMap(SubscribePlan::getId, p -> p));
                                return Flux.fromIterable(subscribes.stream()
                                        .map(s -> toSubscribeVo(s, planMap.get(s.getPlanId())))
                                        .toList());
                            });
                });
    }

    private Mono<UserSubscribeVo> createPaymentOrder(SubscribeCreateDto dto, SubscribePlan plan, UserSubscribe saved) {
        PaymentCreateDto paymentDto = new PaymentCreateDto();
        paymentDto.setUserId(dto.getUserId());
        paymentDto.setAmount(plan.getPrice());
        paymentDto.setPayChannel(dto.getPayChannel());
        paymentDto.setSubscribeId(saved.getId());
        return Mono.fromCallable(() -> paymentOpenFeignApi.createPayment(paymentDto))
                .map(paymentR -> {
                    if (paymentR == null || paymentR.getCode() != 0 || paymentR.getData() == null) {
                        throw new BusinessException(paymentR == null ? "创建支付订单失败" : "创建支付订单失败: " + paymentR.getMessage());
                    }
                    PaymentResultVo paymentResult = paymentR.getData();
                    UserSubscribe subscribed = subscribeDomainService.bindOrderNo(saved, paymentResult.getOrderNo());
                    userSubscribeRepository.update(subscribed);
                    return toSubscribeVo(subscribed, plan);
                });
    }

    private UserSubscribeVo toSubscribeVo(UserSubscribe subscribe, SubscribePlan plan) {
        UserSubscribeVo vo = new UserSubscribeVo();
        vo.setId(subscribe.getId())
                .setUserId(subscribe.getUserId())
                .setPlanName(plan != null ? plan.getPlanName() : null)
                .setPlanCode(plan != null ? plan.getPlanCode() : null)
                .setDurationDays(plan != null ? plan.getDurationDays() : null)
                .setStartTime(subscribe.getStartTime())
                .setEndTime(subscribe.getEndTime())
                .setStatus(subscribe.getStatus())
                .setAutoRenew(subscribe.getAutoRenew())
                .setCreateTime(subscribe.getCreateTime());
        return vo;
    }
}
