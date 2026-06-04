package com.novel.cloud.subscribe.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户订阅记录 VO
 */
@Data
@Accessors(chain = true)
public class UserSubscribeVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 订阅记录ID
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 订阅计划名称
     */
    private String planName;

    /**
     * 订阅计划编码
     */
    private String planCode;

    /**
     * 订阅时长（天）
     */
    private Integer durationDays;

    /**
     * 生效时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    /**
     * 到期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    /**
     * 订阅状态：0-待支付 1-生效中 2-已过期 3-已取消
     */
    private Integer status;

    /**
     * 是否自动续订
     */
    private Boolean autoRenew;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
