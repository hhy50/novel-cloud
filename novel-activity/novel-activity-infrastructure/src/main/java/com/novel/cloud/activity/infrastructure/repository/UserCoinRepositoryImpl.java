package com.novel.cloud.activity.infrastructure.repository;

import com.novel.cloud.activity.domain.repository.UserCoinRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 用户金币仓储实现（暂时模拟）
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserCoinRepositoryImpl implements UserCoinRepository {

    // TODO: 后续接入user服务的OpenFeign客户端
    // private final UserOpenFeignApi userOpenFeignApi;

    private static final ThreadLocal<Integer> MOCK_COINS = ThreadLocal.withInitial(() -> 0);

    @Override
    public Integer getUserCoins(Long userId) {
        // TODO: 真实调用user服务
        log.info("获取用户金币余额: userId={}", userId);
        return MOCK_COINS.get();
    }

    @Override
    public Integer addCoins(Long userId, Integer amount, String type, String description) {
        // TODO: 真实调用user服务
        log.info("增加用户金币: userId={}, amount={}, type={}, description={}", userId, amount, type, description);
        Integer current = MOCK_COINS.get();
        Integer newCoins = current + amount;
        MOCK_COINS.set(newCoins);
        return newCoins;
    }
}
