package com.novel.payment.domain.entity;

import java.time.LocalDateTime;
import lombok.Data;

/**
 * 支付订单领域实体
 */
@Data
public class PaymentOrder {

    /** 支付状态：待支付 */
    public static final int STATUS_PENDING = 0;
    /** 支付状态：已支付 */
    public static final int STATUS_PAID = 1;
    /** 支付状态：失败 */
    public static final int STATUS_FAILED = 2;
    /** 支付状态：已退款 */
    public static final int STATUS_REFUNDED = 3;

    private Long id;

    /** 订单号 */
    private String orderNo;

    /** 用户ID */
    private Long userId;

    /** 金额（分） */
    private Long amount;

    /** 支付渠道（alipay / wechat） */
    private String payChannel;

    /** 支付状态：0-待支付 1-已支付 2-失败 3-已退款 */
    private Integer payStatus;

    /** 关联订阅记录ID */
    private Long subscribeId;

    /** 第三方交易流水号 */
    private String tradeNo;

    /** 回调时间 */
    private LocalDateTime callbackTime;

    /** 回调原始数据 */
    private String callbackData;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    /**
     * 是否为待支付状态
     */
    public boolean isPending() {
        return payStatus != null && payStatus == STATUS_PENDING;
    }
}
