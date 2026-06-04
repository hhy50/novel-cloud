package com.novel.cloud.user.domain.repository;

import com.novel.cloud.user.domain.entity.UserWallet;

/**
 * User wallet repository interface
 */
public interface UserWalletRepository {

    UserWallet findByUserId(Long userId);

    void save(UserWallet wallet);

    void updateById(UserWallet wallet);

    void updateCoins(Long userId, Long coins, Long totalCoins);

    void updatePoints(Long userId, Long points, Long totalPoints);
}
