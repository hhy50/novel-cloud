package com.novel.cloud.activity.app;

import cn.dev33.satoken.stp.StpUtil;
import com.novel.cloud.activity.domain.entity.*;
import com.novel.cloud.activity.domain.repository.*;
import com.novel.cloud.activity.domain.service.CheckinDomainService;
import com.novel.cloud.activity.dto.*;
import com.novel.cloud.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 签到应用服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CheckinAppService {

    private final CheckinConfigRepository checkinConfigRepository;
    private final UserCheckinRecordRepository userCheckinRecordRepository;
    private final UserCheckinWeekStatusRepository userCheckinWeekStatusRepository;
    private final UserCoinRepository userCoinRepository;
    private final CheckinDomainService checkinDomainService;

    /**
     * 获取签到状态
     */
    public CheckinStatusVo getCheckinStatus() {
        Long userId = StpUtil.getLoginIdAsLong();
        LocalDate today = LocalDate.now();

        // 获取配置
        CheckinConfig config = checkinConfigRepository.findActiveConfig();
        if (config == null) {
            config = getDefaultConfig();
        }

        // 计算周信息
        int weekOfYear = checkinDomainService.calculateWeekOfYear(today);
        LocalDate weekStart = checkinDomainService.getWeekStartDate(today);
        LocalDate weekEnd = checkinDomainService.getWeekEndDate(today);

        // 获取今天签到记录
        UserCheckinRecord todayRecord = userCheckinRecordRepository.findByUserIdAndDate(userId, today);
        boolean checkedToday = todayRecord != null;

        // 获取周签到状态
        UserCheckinWeekStatus weekStatus = userCheckinWeekStatusRepository.findByUserIdAndWeek(userId, weekOfYear);

        // 解析连续奖励配置
        List<ContinuousBonusItem> bonusItems = checkinDomainService.parseContinuousBonus(config.getContinuousBonusJson());

        // 构建本周签到状态
        List<DayCheckinStatusVo> weekCheckinStatus = buildWeekCheckinStatus(
                weekStart, weekEnd, today, weekStatus, config
        );

        // 计算连续签到天数
        int continuousDays = calculateCurrentContinuousDays(userId, today, weekStatus);

        // 构建连续奖励配置VO
        List<ContinuousBonusConfigVo> continuousBonusConfig = buildContinuousBonusConfig(
                bonusItems, continuousDays, weekStatus
        );

        // 构建返回
        CheckinStatusVo vo = new CheckinStatusVo();
        vo.setCheckedToday(checkedToday);
        vo.setWeekOfYear(weekOfYear);
        vo.setWeekStartDate(weekStart);
        vo.setWeekEndDate(weekEnd);
        vo.setContinuousDays(continuousDays);
        vo.setTotalCheckinDays(weekStatus != null ? weekStatus.getCheckinCount() : 0);
        vo.setNextReward(calculateNextReward(continuousDays, today, config));
        vo.setWeekCheckinStatus(weekCheckinStatus);
        vo.setContinuousBonusConfig(continuousBonusConfig);

        return vo;
    }

    /**
     * 执行签到
     */
    @Transactional(rollbackFor = Exception.class)
    public CheckinResultVo performCheckin() {
        Long userId = StpUtil.getLoginIdAsLong();
        LocalDate today = LocalDate.now();
        LocalDateTime now = LocalDateTime.now();

        // 检查是否已签到
        UserCheckinRecord todayRecord = userCheckinRecordRepository.findByUserIdAndDate(userId, today);
        if (todayRecord != null) {
            throw new BusinessException(2001, "今天已经签到过了");
        }

        // 获取配置
        CheckinConfig config = checkinConfigRepository.findActiveConfig();
        if (config == null) {
            config = getDefaultConfig();
        }

        // 计算周信息
        int weekOfYear = checkinDomainService.calculateWeekOfYear(today);
        LocalDate weekStart = checkinDomainService.getWeekStartDate(today);
        LocalDate weekEnd = checkinDomainService.getWeekEndDate(today);
        int dayOfWeek = today.getDayOfWeek().getValue();
        int dayIndex = dayOfWeek - 1;

        // 获取或创建周签到状态
        UserCheckinWeekStatus weekStatus = userCheckinWeekStatusRepository.findByUserIdAndWeek(userId, weekOfYear);
        if (weekStatus == null) {
            weekStatus = new UserCheckinWeekStatus();
            weekStatus.setUserId(userId);
            weekStatus.setWeekOfYear(weekOfYear);
            weekStatus.setWeekStartDate(weekStart);
            weekStatus.setWeekEndDate(weekEnd);
            weekStatus.setCheckinDays(0);
            weekStatus.setCheckinCount(0);
            weekStatus.setContinuousDays(0);
            weekStatus.setClaimedBonusMask(0);
            userCheckinWeekStatusRepository.save(weekStatus);
        }

        // 计算连续签到天数
        int continuousDays = calculateContinuousDaysForCheckin(userId, today, weekStatus);

        // 计算每日奖励
        int dailyReward = config.getRewardByDayOfWeek(dayOfWeek);

        // 解析连续奖励配置
        List<ContinuousBonusItem> bonusItems = checkinDomainService.parseContinuousBonus(config.getContinuousBonusJson());

        // 计算可领取的连续奖励
        List<CheckinDomainService.BonusWithIndex> eligibleBonus = checkinDomainService.calculateEligibleBonus(
                continuousDays, bonusItems, weekStatus.getClaimedBonusMask()
        );

        // 计算总奖励
        int totalBonus = eligibleBonus.stream()
                .mapToInt(b -> b.item().getBonus())
                .sum();
        int totalReward = dailyReward + totalBonus;

        // 发放金币
        Integer totalCoins = userCoinRepository.addCoins(
                userId, totalReward, "CHECKIN", "签到奖励"
        );

        // 更新周签到状态
        weekStatus.markDayChecked(dayIndex);
        weekStatus.setContinuousDays(continuousDays);
        for (CheckinDomainService.BonusWithIndex bonus : eligibleBonus) {
            weekStatus.markBonusClaimed(bonus.index());
        }
        userCheckinWeekStatusRepository.updateById(weekStatus);

        // 创建签到记录
        UserCheckinRecord record = new UserCheckinRecord();
        record.setUserId(userId);
        record.setCheckinDate(today);
        record.setCheckinTime(now);
        record.setWeekOfYear(weekOfYear);
        record.setDayOfWeek(dayOfWeek);
        record.setDailyReward(dailyReward);
        record.setTotalBonus(totalBonus);
        record.setTotalCoins(totalCoins);
        record.setContinuousDays(continuousDays);
        if (!eligibleBonus.isEmpty()) {
            List<ContinuousBonusItem> claimedBonus = eligibleBonus.stream()
                    .map(CheckinDomainService.BonusWithIndex::item)
                    .toList();
            record.setBonusDetailsJson(checkinDomainService.serializeContinuousBonus(claimedBonus));
        }
        userCheckinRecordRepository.save(record);

        // 构建返回
        List<BonusRewardVo> bonusList = eligibleBonus.stream()
                .map(b -> {
                    BonusRewardVo vo = new BonusRewardVo();
                    vo.setDays(b.item().getDays());
                    vo.setBonus(b.item().getBonus());
                    vo.setName(b.item().getName());
                    return vo;
                })
                .toList();

        // 重新获取最新的周签到状态
        weekStatus = userCheckinWeekStatusRepository.findByUserIdAndWeek(userId, weekOfYear);
        List<DayCheckinStatusVo> weekCheckinStatus = buildWeekCheckinStatus(
                weekStart, weekEnd, today, weekStatus, config
        );

        CheckinResultVo result = new CheckinResultVo();
        result.setDailyReward(dailyReward);
        result.setBonusList(bonusList);
        result.setTotalBonus(totalBonus);
        result.setTotalReward(totalReward);
        result.setContinuousDays(continuousDays);
        result.setTotalCoins(totalCoins);
        result.setWeekCheckinStatus(weekCheckinStatus);

        log.info("用户签到成功: userId={}, reward={}, continuousDays={}", userId, totalReward, continuousDays);

        return result;
    }

    /**
     * 构建本周签到状态
     */
    private List<DayCheckinStatusVo> buildWeekCheckinStatus(
            LocalDate weekStart,
            LocalDate weekEnd,
            LocalDate today,
            UserCheckinWeekStatus weekStatus,
            CheckinConfig config
    ) {
        List<DayCheckinStatusVo> result = new ArrayList<>();
        LocalDate date = weekStart;

        for (int i = 0; i < 7; i++) {
            DayCheckinStatusVo vo = new DayCheckinStatusVo();
            vo.setDate(date);
            vo.setDayOfWeek(date.getDayOfWeek().getValue());
            vo.setReward(config.getRewardByDayOfWeek(date.getDayOfWeek().getValue()));
            vo.setIsToday(date.equals(today));

            // 计算状态
            if (date.isAfter(today)) {
                vo.setStatus("locked");
            } else if (date.isBefore(today)) {
                if (weekStatus != null && weekStatus.isDayChecked(i)) {
                    vo.setStatus("done");
                } else {
                    vo.setStatus("missed");
                }
            } else {
                if (weekStatus != null && weekStatus.isDayChecked(i)) {
                    vo.setStatus("done");
                } else {
                    vo.setStatus("available");
                }
            }

            result.add(vo);
            date = date.plusDays(1);
        }

        return result;
    }

    /**
     * 计算当前连续签到天数
     */
    private int calculateCurrentContinuousDays(Long userId, LocalDate today, UserCheckinWeekStatus weekStatus) {
        if (weekStatus == null) {
            // 查看上周是否有签到
            int lastWeekOfYear = checkinDomainService.calculateWeekOfYear(today.minusDays(7));
            UserCheckinWeekStatus lastWeekStatus = userCheckinWeekStatusRepository.findByUserIdAndWeek(userId, lastWeekOfYear);
            if (lastWeekStatus != null && lastWeekStatus.isDayChecked(6)) {
                return lastWeekStatus.getContinuousDays();
            }
            return 0;
        }

        int continuousDays = 0;
        LocalDate checkDate = today;

        for (int i = 0; i < 14; i++) {
            LocalDate currentCheckDate = checkDate.minusDays(i);
            int currentWeekOfYear = checkinDomainService.calculateWeekOfYear(currentCheckDate);
            UserCheckinWeekStatus currentWeekStatus = currentWeekOfYear == weekStatus.getWeekOfYear()
                    ? weekStatus
                    : userCheckinWeekStatusRepository.findByUserIdAndWeek(userId, currentWeekOfYear);

            if (currentWeekStatus == null) {
                break;
            }

            int dayIndex = currentCheckDate.getDayOfWeek().getValue() - 1;
            if (currentWeekStatus.isDayChecked(dayIndex)) {
                continuousDays++;
            } else if (i > 0) {
                break;
            }
        }

        return continuousDays;
    }

    /**
     * 计算签到时的连续签到天数
     */
    private int calculateContinuousDaysForCheckin(Long userId, LocalDate today, UserCheckinWeekStatus weekStatus) {
        LocalDate yesterday = today.minusDays(1);

        int yesterdayWeekOfYear = checkinDomainService.calculateWeekOfYear(yesterday);
        UserCheckinWeekStatus yesterdayWeekStatus = yesterdayWeekOfYear == weekStatus.getWeekOfYear()
                ? weekStatus
                : userCheckinWeekStatusRepository.findByUserIdAndWeek(userId, yesterdayWeekOfYear);

        if (yesterdayWeekStatus == null) {
            return 1;
        }

        int yesterdayIndex = yesterday.getDayOfWeek().getValue() - 1;
        if (!yesterdayWeekStatus.isDayChecked(yesterdayIndex)) {
            return 1;
        }

        return yesterdayWeekStatus.getContinuousDays() + 1;
    }

    /**
     * 构建连续奖励配置VO
     */
    private List<ContinuousBonusConfigVo> buildContinuousBonusConfig(
            List<ContinuousBonusItem> bonusItems,
            int continuousDays,
            UserCheckinWeekStatus weekStatus
    ) {
        List<ContinuousBonusConfigVo> result = new ArrayList<>();

        for (int i = 0; i < bonusItems.size(); i++) {
            ContinuousBonusItem item = bonusItems.get(i);
            ContinuousBonusConfigVo vo = new ContinuousBonusConfigVo();
            vo.setDays(item.getDays());
            vo.setBonus(item.getBonus());
            vo.setName(item.getName());
            vo.setAchieved(continuousDays >= item.getDays());
            vo.setClaimed(weekStatus != null && weekStatus.isBonusClaimed(i));
            result.add(vo);
        }

        return result;
    }

    /**
     * 计算明天的签到奖励
     */
    private int calculateNextReward(int continuousDays, LocalDate today, CheckinConfig config) {
        LocalDate tomorrow = today.plusDays(1);
        return config.getRewardByDayOfWeek(tomorrow.getDayOfWeek().getValue());
    }

    /**
     * 获取默认配置
     */
    private CheckinConfig getDefaultConfig() {
        CheckinConfig config = new CheckinConfig();
        config.setConfigName("默认配置");
        config.setConfigCode("DEFAULT");
        config.setDailyRewardDefault(10);
        config.setMondayReward(10);
        config.setTuesdayReward(10);
        config.setWednesdayReward(10);
        config.setThursdayReward(10);
        config.setFridayReward(10);
        config.setSaturdayReward(15);
        config.setSundayReward(15);
        config.setContinuousBonusJson("""
            [
                {"days":3,"bonus":30,"name":"连续3天奖励"},
                {"days":5,"bonus":50,"name":"连续5天奖励"},
                {"days":7,"bonus":100,"name":"完美周奖励"}
            ]
            """);
        config.setStatus(1);
        return config;
    }
}
