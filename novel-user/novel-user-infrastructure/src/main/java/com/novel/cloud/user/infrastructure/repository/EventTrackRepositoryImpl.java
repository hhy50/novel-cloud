package com.novel.cloud.user.infrastructure.repository;

import com.novel.cloud.user.domain.entity.EventTrack;
import com.novel.cloud.user.domain.repository.EventTrackRepository;
import com.novel.cloud.user.infrastructure.dataobject.EventTrackDO;
import com.novel.cloud.user.infrastructure.mapper.EventTrackMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventTrackRepositoryImpl implements EventTrackRepository {

    private final EventTrackMapper eventTrackMapper;

    @Override
    public void save(EventTrack eventTrack) {
        EventTrackDO eventTrackDO = new EventTrackDO();
        BeanUtils.copyProperties(eventTrack, eventTrackDO);
        eventTrackMapper.insert(eventTrackDO);
    }
}
