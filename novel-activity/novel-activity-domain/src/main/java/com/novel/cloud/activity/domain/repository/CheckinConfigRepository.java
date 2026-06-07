package com.novel.cloud.activity.domain.repository;

import com.novel.cloud.activity.domain.entity.CheckinConfig;

/**
 * 签到配置仓储接口
 */
public interface CheckinConfigRepository {

    /**
     * 获取当前生效的配置
     */
    CheckinConfig findActiveConfig();

    /**
     * 根据ID查找配置
     */
    CheckinConfig findById(Long id);

    /**
     * 保存配置
     */
    void save(CheckinConfig config);

    /**
     * 更新配置
     */
    void updateById(CheckinConfig config);
}
