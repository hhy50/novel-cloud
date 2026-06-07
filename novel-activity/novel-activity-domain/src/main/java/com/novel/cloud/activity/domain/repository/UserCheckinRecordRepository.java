package com.novel.cloud.activity.domain.repository;

import com.novel.cloud.activity.domain.entity.UserCheckinRecord;

import java.time.LocalDate;

/**
 * 用户签到记录仓储接口
 */
public interface UserCheckinRecordRepository {

    /**
     * 查找用户某天的签到记录
     */
    UserCheckinRecord findByUserIdAndDate(Long userId, LocalDate date);

    /**
     * 查找用户某周的签到记录
     */
    UserCheckinRecord findByUserIdAndWeek(Long userId, Integer weekOfYear);

    /**
     * 查找用户最新的签到记录
     */
    UserCheckinRecord findLatestByUserId(Long userId);

    /**
     * 保存签到记录
     */
    void save(UserCheckinRecord record);
}
