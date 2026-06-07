package com.novel.cloud.payment.app;

import com.novel.cloud.common.exception.BusinessException;
import com.novel.cloud.common.domain.R;
import com.novel.cloud.payment.domain.entity.PaymentOrder;
import com.novel.cloud.payment.domain.repository.PaymentOrderRepository;
import com.novel.cloud.payment.domain.service.PaymentDomainService;
import com.novel.cloud.payment.dto.PaymentCreateDto;
import com.novel.cloud.payment.dto.PaymentNotifyDto;
import com.novel.cloud.payment.dto.PaymentResultVo;
import com.novel.cloud.subscribe.api.SubscribeOpenFeignApi;
import com.novel.cloud.subscribe.dto.SubscribeActivateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 支付应用服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentAppService {

    private final PaymentOrderRepository paymentOrderRepository;
    private final PaymentDomainService paymentDomainService;
    private final SubscribeOpenFeignApi subscribeOpenFeignApi;

    public PaymentResultVo createPayment(PaymentCreateDto dto) {
        PaymentOrder order = paymentDomainService.createPendingOrder(
                dto.getUserId(), dto.getAmount(), dto.getPayChannel(), dto.getSubscribeId());
        paymentOrderRepository.save(order);
        return toResultVo(order);
    }

    public PaymentResultVo handlePaymentNotify(PaymentNotifyDto dto) {
        PaymentOrder order = paymentOrderRepository.findByOrderNo(dto.getOrderNo());
        if (order == null) {
            throw new BusinessException("支付订单不存在");
        }

        if (!order.isPending()) {
            log.warn("订单 {} 状态非待支付，忽略回调, 当前状态: {}", dto.getOrderNo(), order.getPayStatus());
            return toResultVo(order);
        }

        // 领域服务处理回调
        paymentDomainService.handleCallback(order, dto.getPayStatus(), dto.getTradeNo(), dto.getCallbackData());

        paymentOrderRepository.updateById(order);

        // 支付成功时，回调订阅模块激活订阅
        if (dto.getPayStatus() == PaymentOrder.STATUS_PAID) {
            SubscribeActivateDto activateDto = new SubscribeActivateDto();
            activateDto.setSubscribeId(order.getSubscribeId());
            activateDto.setOrderNo(order.getOrderNo());

            try {
                R<?> result = subscribeOpenFeignApi.activateSubscribe(activateDto);
                if (result.getCode() != 0) {
                    log.error("激活订阅失败, subscribeId: {}, orderNo: {}, msg: {}",
                            order.getSubscribeId(), order.getOrderNo(), result.getMessage());
                }
            } catch (Exception e) {
                log.error("回调订阅模块异常", e);
            }
        }

        return toResultVo(order);
    }

    public PaymentResultVo getPaymentByOrderNo(String orderNo) {
        PaymentOrder order = paymentOrderRepository.findByOrderNo(orderNo);
        return toResultVo(order);
    }

    public List<PaymentResultVo> listPaymentsByUserId(Long userId) {
        List<PaymentOrder> orders = paymentOrderRepository.findByUserId(userId);
        return orders.stream()
                .map(this::toResultVo)
                .collect(Collectors.toList());
    }

    private PaymentResultVo toResultVo(PaymentOrder order) {
        PaymentResultVo vo = new PaymentResultVo();
        BeanUtils.copyProperties(order, vo);
        return vo;
    }
}
