package com.novel.cloud.subscribe.app;

import com.novel.cloud.common.exception.BusinessException;
import com.novel.cloud.payment.api.PaymentOpenFeignApi;
import com.novel.cloud.payment.dto.PaymentCreateDto;
import com.novel.cloud.payment.dto.PaymentResultVo;
import com.novel.cloud.subscribe.domain.entity.SubscribePlan;
import com.novel.cloud.subscribe.domain.entity.UserSubscribe;
import com.novel.cloud.subscribe.domain.repository.SubscribePlanRepository;
import com.novel.cloud.subscribe.domain.repository.UserSubscribeRepository;
import com.novel.cloud.subscribe.domain.service.SubscribeDomainService;
import com.novel.cloud.subscribe.dto.SubscribeActivateDto;
import com.novel.cloud.subscribe.dto.SubscribeCreateDto;
import com.novel.cloud.subscribe.dto.UserSubscribeVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
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

    public List<SubscribePlan> listPlans() {
        return subscribePlanRepository.listOnShelfPlans();
    }

    @Transactional(rollbackFor = Exception.class)
    public UserSubscribeVo createSubscribe(SubscribeCreateDto dto) {
        SubscribePlan plan = subscribePlanRepository.findById(dto.getPlanId());
        if (plan == null) {
            throw new BusinessException("订阅计划不存在");
        }
        UserSubscribe subscribe = subscribeDomainService.initPendingSubscribe(dto.getUserId(), plan.getId());
        UserSubscribe saved = userSubscribeRepository.save(subscribe);
        return createPaymentOrder(dto, plan, saved);
    }

    @Transactional(rollbackFor = Exception.class)
    public UserSubscribeVo activateSubscribe(SubscribeActivateDto dto) {
        UserSubscribe subscribe = userSubscribeRepository.findById(dto.getSubscribeId());
        if (subscribe == null) {
            throw new BusinessException("订阅记录不存在");
        }
        SubscribePlan plan = subscribePlanRepository.findById(subscribe.getPlanId());
        if (plan == null) {
            throw new BusinessException("订阅计划不存在");
        }
        UserSubscribe activated = subscribeDomainService.activate(subscribe, plan, dto.getOrderNo());
        UserSubscribe updated = userSubscribeRepository.update(activated);
        return toSubscribeVo(updated, plan);
    }

    public UserSubscribeVo getUserActiveSubscribe(Long userId) {
        UserSubscribe subscribe = userSubscribeRepository.findActiveByUserId(userId);
        if (subscribe == null) {
            return null;
        }
        boolean expired = subscribeDomainService.checkAndMarkExpired(subscribe) != null && subscribe.getStatus() == 2;
        UserSubscribe current = expired ? userSubscribeRepository.update(subscribe) : subscribe;
        SubscribePlan plan = subscribePlanRepository.findById(current.getPlanId());
        return toSubscribeVo(current, plan);
    }

    public List<UserSubscribeVo> getUserSubscribeHistory(Long userId) {
        List<UserSubscribe> subscribes = userSubscribeRepository.listByUserId(userId);
        List<Long> planIds = subscribes.stream()
                .map(UserSubscribe::getPlanId)
                .distinct()
                .collect(Collectors.toList());
        if (planIds.isEmpty()) {
            return subscribes.stream().map(s -> toSubscribeVo(s, null)).collect(Collectors.toList());
        }
        List<SubscribePlan> plans = subscribePlanRepository.findByIds(planIds);
        Map<Long, SubscribePlan> planMap = plans.stream().collect(Collectors.toMap(SubscribePlan::getId, p -> p));
        return subscribes.stream()
                .map(s -> toSubscribeVo(s, planMap.get(s.getPlanId())))
                .collect(Collectors.toList());
    }

    private UserSubscribeVo createPaymentOrder(SubscribeCreateDto dto, SubscribePlan plan, UserSubscribe saved) {
        PaymentCreateDto paymentDto = new PaymentCreateDto();
        paymentDto.setUserId(dto.getUserId());
        paymentDto.setAmount(plan.getPrice());
        paymentDto.setPayChannel(dto.getPayChannel());
        paymentDto.setSubscribeId(saved.getId());
        com.novel.cloud.common.domain.R<PaymentResultVo> paymentR = paymentOpenFeignApi.createPayment(paymentDto);
        if (paymentR == null || paymentR.getCode() != 0 || paymentR.getData() == null) {
            throw new BusinessException(paymentR == null ? "创建支付订单失败" : "创建支付订单失败: " + paymentR.getMessage());
        }
        PaymentResultVo paymentResult = paymentR.getData();
        UserSubscribe subscribed = subscribeDomainService.bindOrderNo(saved, paymentResult.getOrderNo());
        userSubscribeRepository.update(subscribed);
        return toSubscribeVo(subscribed, plan);
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
