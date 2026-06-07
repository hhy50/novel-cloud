package com.novel.cloud.activity.domain.repository;

import com.novel.cloud.activity.domain.entity.UserCheckinWeekStatus;

/**
 * 用户周签到状态仓储接口
 */
public interface UserCheckinWeekStatusRepository {

    /**
     * 查找用户某周的签到状态
     */
    UserCheckinWeekStatus findByUserIdAndWeek(Long userId, Integer weekOfYear);

    /**
     * 保存周签到状态
     */
    void save(UserCheckinWeekStatus status);

    /**
     * 更新周签到状态
     */
    void updateById(UserCheckinWeekStatus status);
}
