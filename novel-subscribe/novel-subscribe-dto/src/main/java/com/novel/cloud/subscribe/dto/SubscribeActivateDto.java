package com.novel.cloud.subscribe.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 支付回调 DTO（支付模块回调订阅模块使用）
 */
@Data
public class SubscribeActivateDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 订阅记录ID
     */
    @NotNull(message = "订阅记录ID不能为空")
    private Long subscribeId;

    /**
     * 支付订单号
     */
    @NotNull(message = "支付订单号不能为空")
    private String orderNo;
}
