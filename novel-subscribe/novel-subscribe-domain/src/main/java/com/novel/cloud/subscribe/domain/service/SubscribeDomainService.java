package com.novel.cloud.subscribe.domain.service;

import com.novel.cloud.common.exception.BusinessException;
import com.novel.cloud.subscribe.domain.entity.SubscribePlan;
import com.novel.cloud.subscribe.domain.entity.UserSubscribe;
import com.novel.cloud.subscribe.domain.repository.UserSubscribeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 订阅领域服务
 */
@Service
@RequiredArgsConstructor
public class SubscribeDomainService {

    private final UserSubscribeRepository userSubscribeRepository;

    public UserSubscribe initPendingSubscribe(Long userId, Long planId) {
        UserSubscribe subscribe = new UserSubscribe();
        subscribe.setUserId(userId);
        subscribe.setPlanId(planId);
        subscribe.setStatus(UserSubscribe.STATUS_PENDING);
        subscribe.setAutoRenew(Boolean.FALSE);
        return subscribe;
    }

    public UserSubscribe bindOrderNo(UserSubscribe subscribe, String orderNo) {
        subscribe.setOrderNo(orderNo);
        return subscribe;
    }

    public LocalDateTime calculateStartTime(Long userId) {
        UserSubscribe activeSub = userSubscribeRepository.findActiveByUserId(userId);
        LocalDateTime now = LocalDateTime.now();
        if (activeSub != null && activeSub.getEndTime() != null && activeSub.getEndTime().isAfter(now)) {
            return activeSub.getEndTime();
        }
        return now;
    }

    public UserSubscribe activate(UserSubscribe subscribe, SubscribePlan plan, String orderNo) {
        if (!subscribe.isPending()) {
            throw new BusinessException("订阅状态异常，无法激活");
        }
        LocalDateTime startTime = calculateStartTime(subscribe.getUserId());
        subscribe.setStartTime(startTime);
        subscribe.setEndTime(startTime.plusDays(plan.getDurationDays()));
        subscribe.setStatus(UserSubscribe.STATUS_ACTIVE);
        subscribe.setOrderNo(orderNo);
        return subscribe;
    }

    public UserSubscribe checkAndMarkExpired(UserSubscribe subscribe) {
        if (subscribe != null && subscribe.isActive() && subscribe.isExpiredByTime()) {
            subscribe.setStatus(UserSubscribe.STATUS_EXPIRED);
        }
        return subscribe;
    }
}
