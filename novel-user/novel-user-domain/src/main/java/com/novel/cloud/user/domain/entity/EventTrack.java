package com.novel.cloud.user.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 事件追踪领域实体
 */
@Data
public class EventTrack {

    private Long id;

    private Long userId;

    private String event;

    private String eventData;

    private Long timestamp;

    private String deviceId;

    private String appVersion;

    private LocalDateTime createTime;
}
