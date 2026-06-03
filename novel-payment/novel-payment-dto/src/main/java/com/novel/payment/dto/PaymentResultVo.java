package com.novel.payment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 支付订单结果 VO
 */
@Data
@Accessors(chain = true)
public class PaymentResultVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 支付订单ID
     */
    private Long id;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 金额（分）
     */
    private Long amount;

    /**
     * 支付渠道
     */
    private String payChannel;

    /**
     * 支付状态：0-待支付 1-已支付 2-失败 3-已退款
     */
    private Integer payStatus;

    /**
     * 关联订阅ID
     */
    private Long subscribeId;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
