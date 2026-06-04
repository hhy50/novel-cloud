package com.novel.cloud.subscribe.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 创建订阅 DTO
 */
@Data
public class SubscribeCreateDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    /**
     * 订阅计划ID
     */
    @NotNull(message = "订阅计划ID不能为空")
    private Long planId;

    /**
     * 支付渠道（alipay / wechat）
     */
    @NotNull(message = "支付渠道不能为空")
    private String payChannel;
}
