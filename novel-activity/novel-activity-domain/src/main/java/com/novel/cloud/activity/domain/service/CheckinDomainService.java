package com.novel.cloud.activity.domain.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.novel.cloud.activity.domain.entity.ContinuousBonusItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 签到领域服务
 */
@Slf4j
@Service
public class CheckinDomainService {

    /**
     * 计算年份第几周（格式：202623）
     */
    public int calculateWeekOfYear(LocalDate date) {
        WeekFields weekFields = WeekFields.of(DayOfWeek.MONDAY, 4);
        int year = date.getYear();
        int week = date.get(weekFields.weekOfWeekBasedYear());

        // 处理跨年情况（12月底可能算下一年的第一周）
        int weekBasedYear = date.get(weekFields.weekBasedYear());
        return weekBasedYear * 100 + week;
    }

    /**
     * 获取周一开始日期
     */
    public LocalDate getWeekStartDate(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        int daysToSubtract = dayOfWeek.getValue() - 1;
        return date.minusDays(daysToSubtract);
    }

    /**
     * 获取周结束日期
     */
    public LocalDate getWeekEndDate(LocalDate date) {
        return getWeekStartDate(date).plusDays(6);
    }

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 解析连续签到奖励配置JSON
     */
    public List<ContinuousBonusItem> parseContinuousBonus(String json) {
        if (json == null || json.isEmpty()) {
            return List.of();
        }

        try {
            List<Map<String, Object>> list = MAPPER.readValue(json, new TypeReference<>() {});
            List<ContinuousBonusItem> items = new ArrayList<>();
            for (Map<String, Object> map : list) {
                items.add(ContinuousBonusItem.builder()
                        .days(toInt(map.get("days")))
                        .bonus(toInt(map.get("bonus")))
                        .name((String) map.get("name"))
                        .build());
            }
            return items;
        } catch (JsonProcessingException e) {
            log.error("解析连续签到奖励配置失败: {}", json, e);
            return List.of();
        }
    }

    private int toInt(Object value) {
        if (value instanceof Number n) {
            return n.intValue();
        }
        return 0;
    }

    /**
     * 序列化连续签到奖励配置
     */
    public String serializeContinuousBonus(List<ContinuousBonusItem> items) {
        if (items == null || items.isEmpty()) {
            return "[]";
        }
        try {
            return MAPPER.writeValueAsString(items);
        } catch (JsonProcessingException e) {
            log.error("序列化连续签到奖励配置失败", e);
            return "[]";
        }
    }

    /**
     * 计算连续签到天数
     * @param yesterdayChecked 昨天是否签到
     * @param yesterdayContinuousDays 昨天的连续签到天数
     * @return 今天的连续签到天数
     */
    public int calculateContinuousDays(boolean yesterdayChecked, Integer yesterdayContinuousDays) {
        if (yesterdayChecked && yesterdayContinuousDays != null) {
            return yesterdayContinuousDays + 1;
        }
        return 1;
    }

    /**
     * 计算可领取的连续签到奖励
     * @param continuousDays 当前连续签到天数
     * @param bonusList 连续奖励配置列表
     * @param claimedMask 已领取奖励掩码
     * @return 可领取的奖励列表（包含索引）
     */
    public List<BonusWithIndex> calculateEligibleBonus(
            int continuousDays,
            List<ContinuousBonusItem> bonusList,
            Integer claimedMask
    ) {
        List<BonusWithIndex> result = new ArrayList<>();
        int mask = claimedMask != null ? claimedMask : 0;

        for (int i = 0; i < bonusList.size(); i++) {
            ContinuousBonusItem item = bonusList.get(i);
            if (continuousDays >= item.getDays() && (mask & (1 << i)) == 0) {
                result.add(new BonusWithIndex(i, item));
            }
        }
        return result;
    }

    /**
     * 带索引的连续奖励
     */
    public record BonusWithIndex(int index, ContinuousBonusItem item) {
    }
}
