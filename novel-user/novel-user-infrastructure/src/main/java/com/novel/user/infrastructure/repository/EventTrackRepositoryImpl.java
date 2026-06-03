package com.novel.user.infrastructure.repository;

import com.novel.user.domain.entity.EventTrack;
import com.novel.user.domain.repository.EventTrackRepository;
import com.novel.user.infrastructure.dataobject.EventTrackDO;
import com.novel.user.infrastructure.mapper.EventTrackMapper;
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
