package com.novel.cloud.payment.api;

import com.novel.cloud.common.domain.R;
import com.novel.cloud.payment.dto.PaymentCreateDto;
import com.novel.cloud.payment.dto.PaymentResultVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 支付服务 OpenFeign 接口
 * 供订阅模块调用创建支付订单
 */
@FeignClient(name = "novel-payment-service", contextId = "paymentOpenFeignApi", path = "/api/open/payment")
public interface PaymentOpenFeignApi {

    /**
     * 创建支付订单
     */
    @PostMapping("/create")
    R<PaymentResultVo> createPayment(@RequestBody PaymentCreateDto dto);
}
