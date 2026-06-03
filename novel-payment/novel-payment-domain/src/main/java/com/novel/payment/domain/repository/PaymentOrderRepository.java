package com.novel.payment.domain.repository;

import com.novel.payment.domain.entity.PaymentOrder;
import java.util.List;

/**
 * 支付订单仓储接口
 */
public interface PaymentOrderRepository {

    void save(PaymentOrder paymentOrder);

    void updateById(PaymentOrder paymentOrder);

    PaymentOrder findByOrderNo(String orderNo);

    List<PaymentOrder> findByUserId(Long userId);
}
