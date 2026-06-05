package com.novel.cloud.payment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 支付回调 DTO（第三方支付平台回调）
 */
@Data
public class PaymentNotifyDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 订单号
     */
    @NotBlank(message = "订单号不能为空")
    private String orderNo;

    /**
     * 支付状态：1-成功 2-失败
     */
    @NotNull(message = "支付状态不能为空")
    private Integer payStatus;

    /**
     * 第三方交易流水号
     */
    private String tradeNo;

    /**
     * 回调原始数据
     */
    private String callbackData;
}
