-- ============================================================
-- novel_subscribe 数据库
-- ============================================================
CREATE DATABASE IF NOT EXISTS `novel_subscribe` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `novel_subscribe`;

-- 订阅计划表
CREATE TABLE IF NOT EXISTS `t_subscribe_plan` (
    `id`             BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `plan_name`      VARCHAR(64)  NOT NULL                COMMENT '计划名称（如：月度VIP、年度VIP）',
    `plan_code`      VARCHAR(32)  NOT NULL                COMMENT '计划编码（如：MONTHLY、YEARLY）',
    `duration_days`  INT          NOT NULL                COMMENT '时长（天）',
    `price`         BIGINT       NOT NULL                COMMENT '价格（分）',
    `original_price` BIGINT      NOT NULL                COMMENT '原价（分）',
    `sort_order`     INT          NOT NULL DEFAULT 0      COMMENT '排序',
    `status`         TINYINT      NOT NULL DEFAULT 1      COMMENT '状态：0-下架 1-上架',
    `create_time`    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_plan_code` (`plan_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订阅计划表';

-- 用户订阅记录表
CREATE TABLE IF NOT EXISTS `t_user_subscribe` (
    `id`           BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id`      BIGINT       NOT NULL                COMMENT '用户ID',
    `plan_id`      BIGINT       NOT NULL                COMMENT '订阅计划ID',
    `order_no`     VARCHAR(64)  DEFAULT NULL            COMMENT '支付订单号',
    `start_time`   DATETIME     DEFAULT NULL            COMMENT '生效时间',
    `end_time`     DATETIME     DEFAULT NULL            COMMENT '到期时间',
    `status`       TINYINT      NOT NULL DEFAULT 0      COMMENT '订阅状态：0-待支付 1-生效中 2-已过期 3-已取消',
    `auto_renew`   TINYINT(1)   NOT NULL DEFAULT 0      COMMENT '是否自动续订',
    `create_time`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_user_status` (`user_id`, `status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户订阅记录表';

-- 初始化订阅计划演示数据
INSERT INTO `t_subscribe_plan` (`plan_name`, `plan_code`, `duration_days`, `price`, `original_price`, `sort_order`, `status`) VALUES
('月度VIP', 'MONTHLY', 30, 1500, 2500, 1, 1),
('季度VIP', 'QUARTERLY', 90, 4000, 7500, 2, 1),
('年度VIP', 'YEARLY', 365, 12800, 30000, 3, 1);


-- ============================================================
-- novel_payment 数据库
-- ============================================================
CREATE DATABASE IF NOT EXISTS `novel_payment` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `novel_payment`;

-- 支付订单表
CREATE TABLE IF NOT EXISTS `t_payment_order` (
    `id`             BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `order_no`       VARCHAR(64)  NOT NULL                COMMENT '订单号',
    `user_id`        BIGINT       NOT NULL                COMMENT '用户ID',
    `amount`         BIGINT       NOT NULL                COMMENT '金额（分）',
    `pay_channel`    VARCHAR(32)  NOT NULL                COMMENT '支付渠道（alipay / wechat）',
    `pay_status`     TINYINT      NOT NULL DEFAULT 0      COMMENT '支付状态：0-待支付 1-已支付 2-失败 3-已退款',
    `subscribe_id`   BIGINT       NOT NULL                COMMENT '关联订阅记录ID',
    `trade_no`       VARCHAR(128) DEFAULT NULL            COMMENT '第三方交易流水号',
    `callback_time`  DATETIME     DEFAULT NULL            COMMENT '回调时间',
    `callback_data`  TEXT         DEFAULT NULL            COMMENT '回调原始数据',
    `create_time`    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_no` (`order_no`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_subscribe_id` (`subscribe_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='支付订单表';
