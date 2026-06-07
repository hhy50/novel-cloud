package com.novel.cloud.activity.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 签到结果响应VO
 */
@Data
public class CheckinResultVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 每日签到奖励金币数
     */
    private Integer dailyReward;

    /**
     * 本次获得的额外奖励列表
     */
    private List<BonusRewardVo> bonusList;

    /**
     * 额外奖励总和
     */
    private Integer totalBonus;

    /**
     * 本次签到总奖励（daily+bonus）
     */
    private Integer totalReward;

    /**
     * 连续签到天数
     */
    private Integer continuousDays;

    /**
     * 当前总金币数
     */
    private Integer totalCoins;

    /**
     * 更新后的本周签到状态
     */
    private List<DayCheckinStatusVo> weekCheckinStatus;
}
