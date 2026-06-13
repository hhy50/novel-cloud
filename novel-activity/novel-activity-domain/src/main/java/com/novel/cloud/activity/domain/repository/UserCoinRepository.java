package com.novel.cloud.activity.domain.repository;

/**
 * 用户金币仓储接口（用于调用user服务）
 */
public interface UserCoinRepository {

    /**
     * 增加用户金币
     * @param userId 用户ID
     * @param amount 增加数量
     * @param type 类型
     * @param description 描述
     * @return 增加后的总金币数
     */
    Integer addCoins(Long userId, Integer amount, String type, String description);

    /**
     * 增加用户奖励
     * @param userId 用户ID
     * @param coins 金币数量
     * @param points 积分数量
     * @param type 类型
     * @param description 描述
     */
    void addReward(Long userId, Integer coins, Integer points, String type, String description);
}
