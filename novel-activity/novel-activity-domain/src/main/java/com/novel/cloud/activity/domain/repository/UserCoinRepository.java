package com.novel.cloud.activity.domain.repository;

/**
 * 用户金币仓储接口（用于调用user服务）
 */
public interface UserCoinRepository {

    /**
     * 获取用户金币余额
     */
    Integer getUserCoins(Long userId);

    /**
     * 增加用户金币
     * @param userId 用户ID
     * @param amount 增加数量
     * @param type 类型
     * @param description 描述
     * @return 增加后的总金币数
     */
    Integer addCoins(Long userId, Integer amount, String type, String description);
}
