package com.novel.cloud.payment.adapter.web;

import com.novel.cloud.common.domain.R;
import com.novel.cloud.payment.app.PaymentAppService;
import com.novel.cloud.payment.dto.PaymentResultVo;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 支付 Controller（App 对外接口）
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment")
public class PaymentController {

    private final PaymentAppService paymentAppService;

    /**
     * 查询支付订单
     */
    @GetMapping("/query")
    public Mono<R<PaymentResultVo>> queryPayment(@NotNull @RequestParam String orderNo) {
        return paymentAppService.getPaymentByOrderNo(orderNo).map(R::ok);
    }

    /**
     * 查询用户支付订单列表
     */
    @GetMapping("/list")
    public Mono<R<List<PaymentResultVo>>> listPayments(@NotNull @RequestParam Long userId) {
        return paymentAppService.listPaymentsByUserId(userId).map(R::ok);
    }
}
