package com.novel.cloud.user.infrastructure.repository;

import com.novel.cloud.user.domain.entity.UserFeedback;
import com.novel.cloud.user.domain.repository.UserFeedbackRepository;
import com.novel.cloud.user.infrastructure.dataobject.UserFeedbackDO;
import com.novel.cloud.user.infrastructure.mapper.UserFeedbackMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFeedbackRepositoryImpl implements UserFeedbackRepository {

    private final UserFeedbackMapper userFeedbackMapper;

    @Override
    public void save(UserFeedback feedback) {
        UserFeedbackDO feedbackDO = new UserFeedbackDO();
        BeanUtils.copyProperties(feedback, feedbackDO);
        userFeedbackMapper.insert(feedbackDO);
    }
}
