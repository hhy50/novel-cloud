package com.novel.cloud.user.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.novel.cloud.user.domain.entity.UserWallet;
import com.novel.cloud.user.domain.repository.UserWalletRepository;
import com.novel.cloud.user.infrastructure.dataobject.UserWalletDO;
import com.novel.cloud.user.infrastructure.mapper.UserWalletMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserWalletRepositoryImpl implements UserWalletRepository {

    private final UserWalletMapper userWalletMapper;

    @Override
    public UserWallet findByUserId(Long userId) {
        UserWalletDO walletDO = userWalletMapper.selectOne(
                new LambdaQueryWrapper<UserWalletDO>()
                        .eq(UserWalletDO::getUserId, userId)
                        .last("limit 1")
        );
        if (walletDO == null) {
            return null;
        }
        UserWallet wallet = new UserWallet();
        BeanUtils.copyProperties(walletDO, wallet);
        return wallet;
    }

    @Override
    public void save(UserWallet wallet) {
        UserWalletDO walletDO = new UserWalletDO();
        BeanUtils.copyProperties(wallet, walletDO);
        userWalletMapper.insert(walletDO);
    }

    @Override
    public void updateById(UserWallet wallet) {
        UserWalletDO walletDO = new UserWalletDO();
        BeanUtils.copyProperties(wallet, walletDO);
        userWalletMapper.updateById(walletDO);
    }

    @Override
    public void updateCoins(Long userId, Long coins, Long totalCoins) {
        userWalletMapper.updateCoins(userId, coins, totalCoins);
    }

    @Override
    public void updatePoints(Long userId, Long points, Long totalPoints) {
        userWalletMapper.updatePoints(userId, points, totalPoints);
    }
}
