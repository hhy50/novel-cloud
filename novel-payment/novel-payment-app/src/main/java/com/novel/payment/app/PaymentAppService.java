package com.novel.payment.app;

import com.novel.common.core.domain.R;
import com.novel.common.core.exception.BusinessException;
import com.novel.payment.domain.entity.PaymentOrder;
import com.novel.payment.domain.repository.PaymentOrderRepository;
import com.novel.payment.domain.service.PaymentDomainService;
import com.novel.payment.dto.PaymentCreateDto;
import com.novel.payment.dto.PaymentNotifyDto;
import com.novel.payment.dto.PaymentResultVo;
import com.novel.subscribe.api.SubscribeOpenFeignApi;
import com.novel.subscribe.dto.SubscribeActivateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

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

    public Mono<PaymentResultVo> createPayment(PaymentCreateDto dto) {
        return Mono.fromCallable(() -> {
                    PaymentOrder order = paymentDomainService.createPendingOrder(
                            dto.getUserId(), dto.getAmount(), dto.getPayChannel(), dto.getSubscribeId());
                    paymentOrderRepository.save(order);
                    return order;
                })
                .subscribeOn(Schedulers.boundedElastic())
                .map(this::toResultVo);
    }

    public Mono<PaymentResultVo> handlePaymentNotify(PaymentNotifyDto dto) {
        return Mono.fromCallable(() -> paymentOrderRepository.findByOrderNo(dto.getOrderNo()))
                .subscribeOn(Schedulers.boundedElastic())
                .switchIfEmpty(Mono.error(new BusinessException("支付订单不存在")))
                .flatMap(order -> {
                    if (!order.isPending()) {
                        log.warn("订单 {} 状态非待支付，忽略回调, 当前状态: {}", dto.getOrderNo(), order.getPayStatus());
                        return Mono.just(order);
                    }

                    // 领域服务处理回调
                    paymentDomainService.handleCallback(order, dto.getPayStatus(), dto.getTradeNo(), dto.getCallbackData());

                    return Mono.fromCallable(() -> {
                                paymentOrderRepository.updateById(order);
                                return order;
                            })
                            .subscribeOn(Schedulers.boundedElastic())
                            .flatMap(saved -> {
                                // 支付成功时，回调订阅模块激活订阅
                                if (dto.getPayStatus() == PaymentOrder.STATUS_PAID) {
                                    SubscribeActivateDto activateDto = new SubscribeActivateDto();
                                    activateDto.setSubscribeId(saved.getSubscribeId());
                                    activateDto.setOrderNo(saved.getOrderNo());

                                    return Mono.fromCallable(() -> {
                                                R<?> result = subscribeOpenFeignApi.activateSubscribe(activateDto);
                                                if (result.getCode() != 0) {
                                                    log.error("激活订阅失败, subscribeId: {}, orderNo: {}, msg: {}",
                                                            saved.getSubscribeId(), saved.getOrderNo(), result.getMessage());
                                                }
                                                return saved;
                                            })
                                            .subscribeOn(Schedulers.boundedElastic())
                                            .onErrorResume(e -> {
                                                log.error("回调订阅模块异常", e);
                                                return Mono.just(saved);
                                            });
                                }
                                return Mono.just(saved);
                            });
                })
                .map(this::toResultVo);
    }

    public Mono<PaymentResultVo> getPaymentByOrderNo(String orderNo) {
        return Mono.fromCallable(() -> paymentOrderRepository.findByOrderNo(orderNo))
                .subscribeOn(Schedulers.boundedElastic())
                .map(this::toResultVo);
    }

    public Mono<List<PaymentResultVo>> listPaymentsByUserId(Long userId) {
        return Mono.fromCallable(() -> paymentOrderRepository.findByUserId(userId))
                .subscribeOn(Schedulers.boundedElastic())
                .map(orders -> orders.stream()
                        .map(this::toResultVo)
                        .collect(Collectors.toList())
                );
    }

    private PaymentResultVo toResultVo(PaymentOrder order) {
        PaymentResultVo vo = new PaymentResultVo();
        BeanUtils.copyProperties(order, vo);
        return vo;
    }
}
