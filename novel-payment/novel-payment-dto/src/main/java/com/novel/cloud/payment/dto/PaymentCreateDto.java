package com.novel.cloud.payment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 创建支付订单 DTO
 */
@Data
public class PaymentCreateDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    /**
     * 金额（分）
     */
    @NotNull(message = "金额不能为空")
    private Long amount;

    /**
     * 支付渠道（alipay / wechat）
     */
    @NotBlank(message = "支付渠道不能为空")
    private String payChannel;

    /**
     * 关联的订阅记录ID
     */
    @NotNull(message = "订阅记录ID不能为空")
    private Long subscribeId;
}
