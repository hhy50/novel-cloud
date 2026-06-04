package com.novel.user.app;

import cn.hutool.core.util.IdUtil;
import com.novel.user.domain.entity.EventTrack;
import com.novel.user.domain.repository.EventTrackRepository;
import com.novel.user.dto.EventReportDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/**
 * 事件追踪应用服务
 */
@Service
@RequiredArgsConstructor
public class EventTrackAppService {

    private final EventTrackRepository eventTrackRepository;

    public Mono<Void> reportEvent(Long userId, String deviceId, String appVersion, EventReportDto dto) {
        return Mono.<Void>fromCallable(() -> {
            EventTrack eventTrack = new EventTrack();
            eventTrack.setId(IdUtil.getSnowflakeNextId());
            eventTrack.setUserId(userId);
            eventTrack.setEvent(dto.getEvent());
            eventTrack.setEventData(dto.getEventData());
            eventTrack.setTimestamp(dto.getTimestamp());
            eventTrack.setDeviceId(deviceId);
            eventTrack.setAppVersion(appVersion);
            eventTrack.setCreateTime(LocalDateTime.now());
            eventTrackRepository.save(eventTrack);
            return null;
        });
    }
}
