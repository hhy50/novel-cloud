package com.novel.cloud.user.domain.repository;

import com.novel.cloud.user.domain.entity.UserFeedback;

/**
 * User feedback repository interface
 */
public interface UserFeedbackRepository {

    void save(UserFeedback feedback);
}
