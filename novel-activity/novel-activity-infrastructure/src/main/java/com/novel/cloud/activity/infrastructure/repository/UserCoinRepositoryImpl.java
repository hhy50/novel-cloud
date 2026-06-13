package com.novel.cloud.activity.infrastructure.repository;

import com.novel.cloud.activity.domain.repository.UserCoinRepository;
import com.novel.cloud.common.domain.R;
import com.novel.cloud.common.exception.BusinessException;
import com.novel.cloud.user.api.UserOpenFeignApi;
import com.novel.cloud.user.dto.UserRewardAddDto;
import com.novel.cloud.user.dto.UserRewardAddVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 用户金币仓储实现。
 */
@Component
@RequiredArgsConstructor
public class UserCoinRepositoryImpl implements UserCoinRepository {

    private final UserOpenFeignApi userOpenFeignApi;

    @Override
    public Integer addCoins(Long userId, Integer amount, String type, String description) {
        UserRewardAddVo vo = requestAddReward(userId, amount, 0, type, description);
        return vo.getCoins() == null ? 0 : vo.getCoins().intValue();
    }

    @Override
    public void addReward(Long userId, Integer coins, Integer points, String type, String description) {
        requestAddReward(userId, coins, points, type, description);
    }

    private UserRewardAddVo requestAddReward(Long userId, Integer coins, Integer points, String type, String description) {
        UserRewardAddDto dto = new UserRewardAddDto();
        dto.setUserId(userId);
        dto.setCoins(coins);
        dto.setPoints(points);
        dto.setType(type);
        dto.setDescription(description);

        R<UserRewardAddVo> response = userOpenFeignApi.addReward(dto);
        if (response == null || response.getCode() == null || response.getCode() != 0) {
            String message = response == null ? "用户奖励发放失败" : response.getMessage();
            throw new BusinessException(message);
        }
        return response.getData();
    }
}
