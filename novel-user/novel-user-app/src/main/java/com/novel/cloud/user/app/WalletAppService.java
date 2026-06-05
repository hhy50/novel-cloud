package com.novel.cloud.user.app;

import cn.dev33.satoken.stp.StpUtil;
import com.novel.cloud.user.domain.entity.*;
import com.novel.cloud.user.domain.repository.*;
import com.novel.cloud.user.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Wallet and rewards application service
 */
@Service
@RequiredArgsConstructor
public class WalletAppService {

    private final UserWalletRepository userWalletRepository;
    private final CoinRecordRepository coinRecordRepository;
    private final DailyCheckinRepository dailyCheckinRepository;
    private final DailyTaskRepository dailyTaskRepository;
    private final UserTaskRecordRepository userTaskRecordRepository;

    public Mono<UserProfileVo> getUserProfile() {
        return Mono.fromCallable(() -> {
            Long userId = StpUtil.getLoginIdAsLong();
            UserWallet wallet = getOrCreateWallet(userId);

            UserProfileVo profileVo = new UserProfileVo();
            profileVo.setUserId(userId);
            profileVo.setCoins(wallet.getCoins());
            profileVo.setPoints(wallet.getPoints());
            profileVo.setVip(false); // TODO: implement VIP logic
            return profileVo;
        });
    }

    public Mono<WalletInfoVo> getWalletInfo() {
        return Mono.fromCallable(() -> {
            Long userId = StpUtil.getLoginIdAsLong();
            UserWallet wallet = getOrCreateWallet(userId);

            WalletInfoVo walletVo = new WalletInfoVo();
            walletVo.setCoins(wallet.getCoins());
            walletVo.setPoints(wallet.getPoints());
            walletVo.setTotalCoins(wallet.getTotalCoins());
            walletVo.setTotalPoints(wallet.getTotalPoints());
            return walletVo;
        });
    }

    public Mono<CoinRecordsVo> getCoinRecords(CoinRecordsQueryDto params) {
        return Mono.fromCallable(() -> {
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
        });
    }

    public Mono<CheckinStatusVo> getCheckinStatus() {
        return Mono.fromCallable(() -> {
            Long userId = StpUtil.getLoginIdAsLong();
            LocalDate today = LocalDate.now();

            DailyCheckin todayCheckin = dailyCheckinRepository.findByUserIdAndDate(userId, today);
            DailyCheckin latestCheckin = dailyCheckinRepository.findLatestByUserId(userId);

            CheckinStatusVo statusVo = new CheckinStatusVo();
            statusVo.setCheckedToday(todayCheckin != null);

            int consecutiveDays = latestCheckin != null ? latestCheckin.getConsecutiveDays() : 0;
            statusVo.setConsecutiveDays(consecutiveDays);

            // Calculate next reward based on consecutive days
            int nextRewardCoins = calculateCheckinReward(consecutiveDays + 1);
            statusVo.setNextRewardCoins(nextRewardCoins);
            statusVo.setNextRewardPoints(nextRewardCoins / 2);

            return statusVo;
        });
    }

    @Transactional
    public Mono<CheckinVo> performCheckin() {
        return Mono.fromCallable(() -> {
            Long userId = StpUtil.getLoginIdAsLong();
            LocalDate today = LocalDate.now();

            // Check if already checked in today
            DailyCheckin todayCheckin = dailyCheckinRepository.findByUserIdAndDate(userId, today);
            if (todayCheckin != null) {
                CheckinVo vo = new CheckinVo();
                vo.setSuccess(false);
                vo.setMessage("Already checked in today");
                return vo;
            }

            // Get latest check-in to calculate consecutive days
            DailyCheckin latestCheckin = dailyCheckinRepository.findLatestByUserId(userId);
            int consecutiveDays = 1;

            if (latestCheckin != null) {
                LocalDate yesterday = today.minusDays(1);
                if (latestCheckin.getCheckinDate().equals(yesterday)) {
                    consecutiveDays = latestCheckin.getConsecutiveDays() + 1;
                }
            }

            // Calculate rewards
            int rewardCoins = calculateCheckinReward(consecutiveDays);
            int rewardPoints = rewardCoins / 2;

            // Save check-in record
            DailyCheckin checkin = new DailyCheckin();
            checkin.setUserId(userId);
            checkin.setCheckinDate(today);
            checkin.setConsecutiveDays(consecutiveDays);
            checkin.setRewardCoins(rewardCoins);
            checkin.setRewardPoints(rewardPoints);
            dailyCheckinRepository.save(checkin);

            // Update wallet
            addCoins(userId, rewardCoins, "CHECKIN", "Daily check-in reward");
            addPoints(userId, rewardPoints, "CHECKIN", "Daily check-in reward");

            CheckinVo vo = new CheckinVo();
            vo.setSuccess(true);
            vo.setConsecutiveDays(consecutiveDays);
            vo.setRewardCoins(rewardCoins);
            vo.setRewardPoints(rewardPoints);
            vo.setMessage("Check-in successful!");
            return vo;
        });
    }

    public Mono<DailyTasksVo> getDailyTasks() {
        return Mono.fromCallable(() -> {
            Long userId = StpUtil.getLoginIdAsLong();
            LocalDate today = LocalDate.now();

            List<DailyTask> tasks = dailyTaskRepository.findAllActive();
            List<UserTaskRecord> records = userTaskRecordRepository.findByUserIdAndDate(userId, today);

            Map<Long, UserTaskRecord> recordMap = records.stream()
                    .collect(Collectors.toMap(UserTaskRecord::getTaskId, r -> r));

            List<DailyTaskVo> taskVos = tasks.stream()
                    .map(task -> {
                        UserTaskRecord record = recordMap.get(task.getId());
                        DailyTaskVo vo = new DailyTaskVo();
                        vo.setTaskId(task.getId());
                        vo.setTaskCode(task.getTaskCode());
                        vo.setTaskName(task.getTaskName());
                        vo.setTaskDesc(task.getTaskDesc());
                        vo.setRewardCoins(task.getRewardCoins());
                        vo.setRewardPoints(task.getRewardPoints());
                        vo.setTargetCount(task.getTargetCount());

                        if (record != null) {
                            vo.setCurrentCount(record.getCurrentCount());
                            vo.setCompleted(record.getCompleted());
                            vo.setRewardClaimed(record.getRewardClaimed());
                        } else {
                            vo.setCurrentCount(0);
                            vo.setCompleted(false);
                            vo.setRewardClaimed(false);
                        }
                        return vo;
                    })
                    .collect(Collectors.toList());

            int completedCount = (int) taskVos.stream().filter(DailyTaskVo::getRewardClaimed).count();

            DailyTasksVo result = new DailyTasksVo();
            result.setTasks(taskVos);
            result.setCompletedCount(completedCount);
            result.setTotalCount(tasks.size());
            return result;
        });
    }

    @Transactional
    public Mono<ClaimTaskRewardVo> claimTaskReward(ClaimTaskRewardDto params) {
        return Mono.fromCallable(() -> {
            Long userId = StpUtil.getLoginIdAsLong();
            LocalDate today = LocalDate.now();

            DailyTask task = dailyTaskRepository.findById(params.getTaskId());
            if (task == null) {
                ClaimTaskRewardVo vo = new ClaimTaskRewardVo();
                vo.setSuccess(false);
                vo.setMessage("Task not found");
                return vo;
            }

            UserTaskRecord record = userTaskRecordRepository.findByUserIdAndTaskIdAndDate(
                    userId, params.getTaskId(), today);

            if (record == null || !record.getCompleted()) {
                ClaimTaskRewardVo vo = new ClaimTaskRewardVo();
                vo.setSuccess(false);
                vo.setMessage("Task not completed");
                return vo;
            }

            if (record.getRewardClaimed()) {
                ClaimTaskRewardVo vo = new ClaimTaskRewardVo();
                vo.setSuccess(false);
                vo.setMessage("Reward already claimed");
                return vo;
            }

            // Update record
            record.setRewardClaimed(true);
            userTaskRecordRepository.updateById(record);

            // Add rewards
            addCoins(userId, task.getRewardCoins(), "TASK", "Daily task: " + task.getTaskName());
            addPoints(userId, task.getRewardPoints(), "TASK", "Daily task: " + task.getTaskName());

            ClaimTaskRewardVo vo = new ClaimTaskRewardVo();
            vo.setSuccess(true);
            vo.setRewardCoins(task.getRewardCoins());
            vo.setRewardPoints(task.getRewardPoints());
            vo.setMessage("Reward claimed successfully!");
            return vo;
        });
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

    private int calculateCheckinReward(int consecutiveDays) {
        // Base reward + bonus for consecutive days
        int baseReward = 10;
        int bonus = Math.min((consecutiveDays - 1) * 2, 20); // Max bonus: 20 coins
        return baseReward + bonus;
    }

    @Transactional
    public void addCoins(Long userId, Integer amount, String type, String description) {
        UserWallet wallet = getOrCreateWallet(userId);
        Long newBalance = wallet.getCoins() + amount;
        Long newTotal = wallet.getTotalCoins() + amount;

        userWalletRepository.updateCoins(userId, newBalance, newTotal);

        CoinRecord record = new CoinRecord();
        record.setUserId(userId);
        record.setAmount((long) amount);
        record.setBalance(newBalance);
        record.setType(type);
        record.setDescription(description);
        coinRecordRepository.save(record);
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

    private void addPoints(Long userId, Integer amount, String type, String description) {
        UserWallet wallet = getOrCreateWallet(userId);
        Long newBalance = wallet.getPoints() + amount;
        Long newTotal = wallet.getTotalPoints() + amount;
        userWalletRepository.updatePoints(userId, newBalance, newTotal);
    }
}
