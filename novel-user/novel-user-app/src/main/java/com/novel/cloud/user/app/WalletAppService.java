package com.novel.cloud.user.app;

import cn.dev33.satoken.stp.StpUtil;
import com.novel.cloud.user.domain.entity.*;
import com.novel.cloud.user.domain.repository.*;
import com.novel.cloud.user.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Wallet and rewards application service.
 *
 * <p>注：签到逻辑已迁出至 {@code novel-activity} 模块（{@code CheckinAppService}），
 * 本服务不再持有 DailyCheckin 相关字段或方法。</p>
 */
@Service
@RequiredArgsConstructor
public class WalletAppService {

    private final UserWalletRepository userWalletRepository;
    private final CoinRecordRepository coinRecordRepository;

    public UserProfileVo getUserProfile() {
        Long userId = StpUtil.getLoginIdAsLong();
        UserWallet wallet = getOrCreateWallet(userId);

        UserProfileVo profileVo = new UserProfileVo();
        profileVo.setUserId(userId);
        profileVo.setCoins(wallet.getCoins());
        profileVo.setPoints(wallet.getPoints());
        profileVo.setVip(false); // TODO: implement VIP logic
        return profileVo;
    }

    public WalletInfoVo getWalletInfo() {
        Long userId = StpUtil.getLoginIdAsLong();
        UserWallet wallet = getOrCreateWallet(userId);

        WalletInfoVo walletVo = new WalletInfoVo();
        walletVo.setCoins(wallet.getCoins());
        walletVo.setPoints(wallet.getPoints());
        walletVo.setTotalCoins(wallet.getTotalCoins());
        walletVo.setTotalPoints(wallet.getTotalPoints());
        return walletVo;
    }

    public CoinRecordsVo getCoinRecords(CoinRecordsQueryDto params) {
        Long userId = StpUtil.getLoginIdAsLong();
        List<CoinRecord> records = coinRecordRepository.findByUserId(
                userId,
                params.getPage(),
                params.getPageSize(),
                params.getType()
        );

        int total = coinRecordRepository.countByUserId(userId, params.getType());

        List<CoinRecordVo> recordVos = records.stream()
                .map(record -> {
                    CoinRecordVo vo = new CoinRecordVo();
                    vo.setId(record.getId());
                    vo.setAmount(record.getAmount());
                    vo.setBalance(record.getBalance());
                    vo.setType(record.getType());
                    vo.setDescription(record.getDescription());
                    vo.setCreateTime(record.getCreateTime());
                    return vo;
                })
                .collect(Collectors.toList());

        CoinRecordsVo result = new CoinRecordsVo();
        result.setRecords(recordVos);
        result.setTotal(total);
        return result;
    }

    // Helper methods
    private UserWallet getOrCreateWallet(Long userId) {
        UserWallet wallet = userWalletRepository.findByUserId(userId);
        if (wallet == null) {
            wallet = new UserWallet();
            wallet.setUserId(userId);
            wallet.setCoins(0L);
            wallet.setPoints(0L);
            wallet.setTotalCoins(0L);
            wallet.setTotalPoints(0L);
            userWalletRepository.save(wallet);
        }
        return wallet;
    }

    @Transactional
    public void addCoins(Long userId, Integer amount, String type, String description) {
        addReward(userId, amount, 0, type, description);
    }

    @Transactional
    public UserRewardAddVo addReward(UserRewardAddDto dto) {
        return addReward(dto.getUserId(), dto.getCoins(), dto.getPoints(), dto.getType(), dto.getDescription());
    }

    @Transactional
    public UserRewardAddVo addReward(Long userId, Integer coins, Integer points, String type, String description) {
        UserWallet wallet = getOrCreateWallet(userId);
        int coinAmount = coins == null ? 0 : coins;
        int pointAmount = points == null ? 0 : points;

        Long newCoins = wallet.getCoins() + coinAmount;
        Long newTotalCoins = wallet.getTotalCoins() + coinAmount;
        Long newPoints = wallet.getPoints() + pointAmount;
        Long newTotalPoints = wallet.getTotalPoints() + pointAmount;

        if (coinAmount != 0) {
            userWalletRepository.updateCoins(userId, newCoins, newTotalCoins);

            CoinRecord record = new CoinRecord();
            record.setUserId(userId);
            record.setAmount((long) coinAmount);
            record.setBalance(newCoins);
            record.setType(type);
            record.setDescription(description);
            coinRecordRepository.save(record);
        }

        if (pointAmount != 0) {
            userWalletRepository.updatePoints(userId, newPoints, newTotalPoints);
        }

        UserRewardAddVo vo = new UserRewardAddVo();
        vo.setCoins(newCoins);
        vo.setPoints(newPoints);
        vo.setTotalCoins(newTotalCoins);
        vo.setTotalPoints(newTotalPoints);
        return vo;
    }

    @Transactional
    public void deductCoins(Long userId, Integer amount, String type, String description) {
        UserWallet wallet = getOrCreateWallet(userId);
        if (wallet.getCoins() < amount) {
            throw new RuntimeException("Insufficient coins");
        }

        Long newBalance = wallet.getCoins() - amount;
        userWalletRepository.updateCoins(userId, newBalance, wallet.getTotalCoins());

        CoinRecord record = new CoinRecord();
        record.setUserId(userId);
        record.setAmount((long) -amount);
        record.setBalance(newBalance);
        record.setType(type);
        record.setDescription(description);
        coinRecordRepository.save(record);
    }
}
