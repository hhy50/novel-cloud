package com.novel.user.domain.repository;

import com.novel.user.domain.entity.EventTrack;

/**
 * 事件追踪仓储接口
 */
public interface EventTrackRepository {

    void save(EventTrack eventTrack);
}
