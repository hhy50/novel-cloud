package com.novel.payment.domain.service;

import com.novel.payment.domain.entity.PaymentOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 支付领域服务
 */
@Service
@RequiredArgsConstructor
public class PaymentDomainService {

    /**
     * 创建待支付订单
     */
    public PaymentOrder createPendingOrder(Long userId, Long amount, String payChannel, Long subscribeId) {
        PaymentOrder order = new PaymentOrder();
        order.setOrderNo(generateOrderNo());
        order.setUserId(userId);
        order.setAmount(amount);
        order.setPayChannel(payChannel);
        order.setPayStatus(PaymentOrder.STATUS_PENDING);
        order.setSubscribeId(subscribeId);
        return order;
    }

    /**
     * 处理支付回调：更新订单状态
     */
    public void handleCallback(PaymentOrder order, Integer payStatus, String tradeNo, String callbackData) {
        order.setPayStatus(payStatus);
        order.setTradeNo(tradeNo);
        order.setCallbackData(callbackData);
        order.setCallbackTime(LocalDateTime.now());
    }

    /**
     * 生成订单号: PAY + 时间戳 + 随机数
     */
    private String generateOrderNo() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        int random = ThreadLocalRandom.current().nextInt(100000, 999999);
        return "PAY" + timestamp + random;
    }
}
