package com.novel.cloud.payment.adapter.open;

import com.novel.cloud.common.domain.R;
import com.novel.cloud.payment.app.PaymentAppService;
import com.novel.cloud.payment.dto.PaymentCreateDto;
import com.novel.cloud.payment.dto.PaymentNotifyDto;
import com.novel.cloud.payment.dto.PaymentResultVo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 支付 Open API Controller（服务间调用接口）
 * 供订阅模块创建支付订单 & 第三方支付平台回调
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/open/payment")
public class PaymentOpenController {

    private final PaymentAppService paymentAppService;

    /**
     * 创建支付订单（供订阅模块调用）
     */
    @PostMapping("/create")
    public R<PaymentResultVo> createPayment(@Valid @RequestBody PaymentCreateDto dto) {
        return R.ok(paymentAppService.createPayment(dto));
    }

    /**
     * 支付回调（第三方支付平台回调）
     */
    @PostMapping("/notify")
    public R<PaymentResultVo> paymentNotify(@Valid @RequestBody PaymentNotifyDto dto) {
        return R.ok(paymentAppService.handlePaymentNotify(dto));
    }
}
